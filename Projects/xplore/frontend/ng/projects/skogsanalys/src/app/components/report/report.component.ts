import {AfterContentInit, Component, ElementRef, ViewChild} from "@angular/core";
import {ViewService} from "../../../../../lib/map-core/view.service";
import {ExportRequest, ExportService, ExtendedCanvas} from "../../../../../lib/map/export/export.service";
import {MapService} from "../../../../../lib/map-core/map.service";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {SkogligRequest} from "../../model/skoglig-request.model";
import JSURL from "jsurl/lib/jsurl";
import {SelectionService} from "../../../../../lib/map/services/selection.service";
import {LayerService} from "../../../../../lib/map-core/layer.service";
import WKT from "ol/format/WKT";
import {Feature} from "ol";
import GeoJSON from "ol/format/GeoJSON";
import {StateService} from "../../../../../lib/map/services/state.service";
import {GeoJson} from "../../../../../lib/map-core/geojson.util";
import {createFeatureName, FeatureType, isRitadFeature} from "../../../../../lib/map/util/fastighet.util";
import moment from "moment";
import {SokService} from "../../../../../lib/map/services/sok.service";


@Component({
  selector: "xp-report",
  templateUrl: "./report.component.html",
  styleUrls: ["./report.component.scss"]
})
export class ReportComponent implements AfterContentInit {
  private WKT: WKT = new WKT();
  private geoJson = new GeoJSON();

  @ViewChild("printKarta") printKarta: ElementRef;

  public title: string;
  public skogligRequest: SkogligRequest;

  public resultat: Resultat;


  constructor(private viewService: ViewService,
              private stateService: StateService,
              private mapService: MapService,
              private sokService: SokService,
              private httpClient: HttpClient,
              private layerService: LayerService,
              private selectionService: SelectionService,
              private route: ActivatedRoute,
              private exportService: ExportService) {
    this.skogligRequest = JSURL.parse(this.route.snapshot.queryParams["skoglig"]);
    let title = this.route.snapshot.queryParams["title"];

    if (title == null || title === "") {
      title = "Skogsanalys";
    }

    this.title = title;
  }

  ngAfterContentInit(): void {
    if (this.skogligRequest != null) {
      if (this.route.snapshot.queryParams["inverteraFastigheter"] === "true") {
        this.stateService.setUiStates({inverteraFastigheter: true});
      } else {
        this.stateService.setUiStates({inverteraFastigheter: false});
      }

      this.stateService.setUiStates({fastighetsgranserOpacity: this.route.snapshot.queryParams["fastighetsgranserOpacity"]});

      const extent = JSON.parse(this.route.snapshot.queryParams["ext"]);

      if (this.skogligRequest.delomrade != null) {
        const delomraden = this.skogligRequest.delomrade
          .replace(/'/g, "")
          .split(",")
          .map(value => "externid:" + value);

        this.sokService.getDelomradenForFastigheter(delomraden).subscribe(value => {
          this.selectionService.setSelectedDelomraden(value);

          const valdaDelomraden = this.stateService.getUiStates().valdaDelomraden;

          this.buildResult(valdaDelomraden);

          this.generateMap(extent);
        });
      }

      if (this.skogligRequest.wkt != null) {
        const gemoetry = this.WKT.readGeometry(this.skogligRequest.wkt);

        const feature = new Feature({
          geometry: gemoetry,
          name: "RITAD_YTA"
        });

        const geoJson = this.geoJson.writeFeatureObject(feature);

        this.selectionService.setSelectedDelomraden([<any>geoJson]);

        this.generateMap(extent);
      }
    }
  }

  generateMap(extent) {
    const request: ExportRequest = {
      paperSize: "a4", orientation: "portrait", extent: extent,
      format: "PDF",
      highlightFeatures: false,
      dpi: 150
    };

    const margins = this.exportService.getPdfMargins();
    const horizMargin = margins[0];
    const vertMargin = margins[1];

    const opts = this.exportService.createOptions(request);
    opts.widthMm -= horizMargin;
    opts.heightMm -= vertMargin;

    this.exportService.createPrintout(this.mapService.map, opts).then(async (printResult: ExtendedCanvas) => {
      const canvas = printResult.canvas;

      this.exportService.decorateImageWithScaleline(printResult.canvas, printResult.metersPerPx);
      this.exportService.decorateImageWithCopyright(printResult.canvas);
      this.exportService.decorateImageWithFastighetsgransInfo(canvas);

      this.printKarta.nativeElement.appendChild(canvas);
    }).then(value => true);
  }

  private buildResult(delomraden: GeoJson[]): Resultat {
    this.resultat = {fastigheter: [], delomraden: []};

    if (!delomraden.length) {
      return;
    }

    delomraden.forEach(feature => {
        if (!isRitadFeature(feature)) {
          const objektId = feature.properties.objekt_id;
          const externId = feature.properties.externid;
          const detaljTyp = feature.properties.detaljtyp;
          let existing = this.resultat.fastigheter.find((fastighet: Fastighet) =>
            (objektId && objektId.length && objektId === fastighet.objektId));

          if (!existing) {
            existing = {
              objektId: objektId,
              beteckning: createFeatureName(feature, FeatureType.FASTIGHET),
              delomraden: [],
              externId,
              detaljTyp
            };

            this.resultat.fastigheter.push(existing);
          }

          existing.delomraden.push({
            objektId: objektId,
            beteckning: createFeatureName(feature, FeatureType.FASTIGHETSDELOMRADE),
            geojson: feature
          });
        } else {
          const ritadYta = {
            objektId: null,
            beteckning: createFeatureName(feature, FeatureType.RITAD_YTA),
            delomraden: [
              {
                objektId: null,
                beteckning: createFeatureName(feature, FeatureType.RITAD_YTA),
                geojson: feature
              }
            ]
          };

          this.resultat.fastigheter.push(ritadYta);
        }
      }
    );

    this.resultat.fastigheter.forEach(fastighet => {
      this.resultat.delomraden = this.resultat.delomraden.concat(fastighet.delomraden);
    });
  }

  dateString(): string {
    const now = moment();
    return now.format("YYYY-MM-DD");
  }

  getDetaljTyp(detaljtyp: string) {
    return DetaljTyp[detaljtyp];
  }

  shortDelomrade(fastighet: Fastighet) {
    if (fastighet.delomraden) {
      let short = ">";
      let first = true;

      for (const delomrade of fastighet.delomraden) {
        if (delomrade.beteckning.startsWith(fastighet.beteckning + ">")) {
          if (first) {
            first = false;
          } else {
            short = short + ",";
          }

          short = short + delomrade.beteckning.substr(fastighet.beteckning.length + 1);
        } else {
          return null;
        }
      }

      return short;
    } else {
      return null;
    }
  }
}

export interface Resultat {
  fastigheter: Fastighet[];
  delomraden: Delomrade[];
  ritadYta?: RitadYta;
}

export interface Fastighet {
  objektId: string;
  beteckning: string;
  delomraden: Delomrade[];
  externId?: string;
  detaljTyp?: string;
}

export interface Delomrade {
  objektId: string;
  beteckning: string;
  geojson: GeoJson;
}

export interface RitadYta {
  beteckning: string;
}

export enum DetaljTyp {
  FASTIGHET = "Fastighet",
  SAMF = "Samfällighet",
  FASTO = "Outredd Fastighet",
  SAMFO = "Outredd Samfällighet",
}
