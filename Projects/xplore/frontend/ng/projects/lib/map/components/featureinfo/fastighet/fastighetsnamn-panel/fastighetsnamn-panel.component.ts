import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from "@angular/core";
import {Delomrade} from "../fsokpanel/fsok-panel.component";
import {EnvironmentConfigService} from "../../../../../config/environment-config.service";
import {extendBoundingBox} from "../../../../../map-core/geojson.util";
import {MapService} from "../../../../../map-core/map.service";
import {SokService} from "../../../../services/sok.service";
import {StateService} from "../../../../services/state.service";
import {transformExtent, transform} from "ol/proj";
import {ConfigProperty, ConfigService} from "../../../../../config/config.service";
import {GoogleService} from "../../../../services/google.service";
import polylabel from "polylabel";
import {DeviceInfoService, Device} from "../../../../../device/deviceInfo.service";
import {BehaviorSubject} from "rxjs";

import {MatCheckboxChange} from "@angular/material/checkbox";
import {Injectable} from "@angular/core";
import {CastorIntegrationService} from "../../../../services/castor-integration.service";
import {RealEstateIdentifier} from "../../../../../../../generated/castor-api/model/realEstateIdentifier";
import {CoordsystemService} from "../../../../services/coordsystem.service";

@Component({
  selector: "xp-fastighetsnamn-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: "./fastighetsnamn-panel.component.html",
  styleUrls: ["./fastighetsnamn-panel.component.scss"]
})

@Injectable()
export class FastighetsnamnPanelComponent {
  @Input() objektId: string;
  @Input() beteckning: string;
  @Input() delomraden: Delomrade[];
  @Input() visaDelomraden = false;
  @Input() detaljtyp: string;
  @Input() externId: string;

  @Input() expanded: boolean;
  @Output() expandedChange = new EventEmitter<boolean>();

  @Output() removeFastighet = new EventEmitter<string>();
  @Output() removeDelomrade = new EventEmitter<string>();
  @Output() removeEgetRitatOmrade = new EventEmitter<string>();

  fastighetsNavigering = false;
  tempRealEstate: RealEstateIdentifier;

  onResize$: BehaviorSubject<Device>;
  castorIsAvailable = false;

  coordSystemText = "";

  constructor(private mapService: MapService, private environmentConfigService: EnvironmentConfigService,
              private configService: ConfigService, private sokService: SokService, private stateService: StateService,
              private googleService: GoogleService, private deviceInfoService: DeviceInfoService,
              private castorIntegrationService: CastorIntegrationService, private coordSystemService: CoordsystemService) {
    this.fastighetsNavigering = this.shouldNavigationBeActivated();
    this.onResize$ = deviceInfoService.onResize$;

    if (this.configService.getProperty("app.components.castor_panel.visible", false)) {
      this.castorIsAvailable = true;
    }

    this.coordSystemText = this.coordSystemService.getAppCoordSystemText();
  }

  onZoom(event: Event) {
    event.stopPropagation();
    const bbox = extendBoundingBox(this.delomraden.map(d => d.geojson));
    const bboxTransformed = transformExtent(bbox, "EPSG:3006", this.configService.config.projectionCode);
    this.mapService.zoomToFit(bboxTransformed, this.configService.config.zoomToFitMinResolution);
  }

  onRemove(event: Event) {
    const isEgetritatOmrade = this.delomraden.length === 1 && this.delomraden[0].beteckning === "EGENRITAT OMRÅDE";
    const externId = (!this.visaDelomraden && this.delomraden.length === 1) ? this.delomraden[0].geojson.properties.externid : null;

    if (isEgetritatOmrade) {
      this.removeEgetRitatOmrade.emit();
    } else if (externId) {
      // Delområde
      this.removeDelomrade.emit(externId);
    } else {
      // fastighet inklusive eventuella delområden
      this.removeFastighet.emit(this.objektId);
    }
  }

  onRemoveWithExternId(externId: string) {
    this.removeDelomrade.emit(externId);
  }

  onNavigation(event: Event) {
    event.stopPropagation();

    const coords: any = this.delomraden.map(d => d.geojson.geometry.coordinates[0])[0];

    //Polylabel räknar ut en punkt i polygonen, returnerar den
    //punkt som ligger längst ifrån kanten men även inuti polygonen.
    const center = transform(polylabel(coords), "EPSG:3006", this.configService.config.projectionCode);

    window.open(this.googleService.getMapsSearchUrlFromCoordinates(center), "_blank");
  }

  getCoordinate(): string {
    const coords: any = this.delomraden.map(d => d.geojson.geometry.coordinates[0])[0];

    //Polylabel räknar ut en punkt i polygonen, returnerar den
    //punkt som ligger längst ifrån kanten men även inuti polygonen.
    const center = transform(polylabel(coords), "EPSG:3006", this.configService.config.projectionCode);


    return `N ${Math.round(center[1])}, E ${Math.round(center[0])} ${this.coordSystemText}`;
  }

  isMobileDevice = () => this.deviceInfoService.isMobileDevice();

  getBeteckning() {
    if (this.objektId) {
      return `<a target="_blank" href="${this.environmentConfigService.getConfig().fsokUrl}${this.objektId}">${this.beteckning}</a>`;
    } else {
      return this.beteckning;
    }
  }

  visaAntaletDelomraden() {
    if (this.visaDelomraden && this.stateService.getUiStates().grupperaFastigheter && this.objektId) {
      const antalDelomraden = this.sokService.getAntalDelomradenForFastighet(this.objektId);
      return !antalDelomraden || antalDelomraden > 1;
    }

    return false;
  }

  hasKomplettFastighet(): boolean {
    if (this.objektId) {
      const antalDelomraden = this.sokService.getAntalDelomradenForFastighet(this.objektId);
      return antalDelomraden && antalDelomraden === this.delomraden.length;
    }

    return false;
  }

  getTooltip(): string {
    let tooltip = "Antalet fastighetsdelområden";
    if (this.objektId) {
      const antalDelomraden = this.sokService.getAntalDelomradenForFastighet(this.objektId);
      if (!antalDelomraden) {
        tooltip = tooltip + " (okänt antal)";
      } else if (antalDelomraden === this.delomraden.length) {
        tooltip = tooltip + " (alla valda)";
      } else {
        tooltip = tooltip + ` (totalt ${antalDelomraden} st)`;
      }
    }

    return tooltip;
  }

  getDetaljTyp = (detaljtyp: string) => DetaljTyp[detaljtyp];

  private shouldNavigationBeActivated(): boolean {
    const applicationLevel = ConfigService.appConfig.fastighetsNavigering;
    const customerLevel = this.configService.getConfigProperty(ConfigProperty.FASTIGHETS_NAVIGERING);

    if (applicationLevel) {
      return customerLevel !== false;
    }

    return customerLevel;
  }

  updateTempUrval($event: MatCheckboxChange) {
    if ($event.checked) {
      this.tempRealEstate = {uuid: this.objektId, beteckning: this.beteckning};
      this.castorIntegrationService.addToTempRealEstateList(this.tempRealEstate);
    } else {
      this.tempRealEstate = {uuid: this.objektId, beteckning: this.beteckning};
      this.castorIntegrationService.removeFromTempRealEstateList(this.tempRealEstate);
    }
  }

  getType() {
    if (this.visaDelomraden && this.delomraden.length === 1) {
      return "omrade";
    }
    return this.visaDelomraden ? "huvudomrade" : "delomrade";
  }
}

export enum DetaljTyp {
  FASTIGHET = "Fastighet",
  SAMF = "Samfällighet",
  FASTO = "Outredd Fastighet",
  SAMFO = "Outredd Samfällighet",
}
