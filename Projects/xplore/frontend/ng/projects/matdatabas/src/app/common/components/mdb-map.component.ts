import {
  AfterViewChecked,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output, TemplateRef,
  ViewChild
} from "@angular/core";
import {MatobjektMapinfo} from "../../services/matobjekt.service";
import {LayerService} from "../../../../../lib/map-core/layer.service";
import VectorImageLayer from "ol/layer/VectorImage";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import {MapComponent} from "../../../../../lib/map-core/map.component";
import {Subject} from "rxjs";
import {distinctUntilChanged, takeUntil} from "rxjs/operators";
import {ViewService} from "../../../../../lib/map-core/view.service";
import {GeolocationService} from "../../../../../lib/map-core/geolocation.service";
import {MapService} from "../../../../../lib/map-core/map.service";
import Overlay from "ol/Overlay";
import {getStyleImageUrl, matobjektStyleFn} from "./styles";
import {MatDrawer} from "@angular/material/sidenav";
import {never} from "ol/events/condition";
import {Draw, Select} from "ol/interaction";
import Polygon, {fromExtent} from "ol/geom/Polygon";
import {KartlagerService, toLayerList} from "../../services/kartlager.service";
import {ConfigService} from "../../../../../lib/config/config.service";
import {ZoomRequestEvent} from "./mdb-layer.component";
import {getExtentFromLayer} from "../../../../../lib/map-core/layer.util";
import {UserService} from "../../services/user.service";
import {HttpClient} from "@angular/common/http";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {GeoJSON} from "ol/format";
import difference from "@turf/difference";
import {Fill, Style} from "ol/style";
import {containsExtent, extend, Extent} from "ol/extent";


@Component({
  selector: "mdb-map",
  template: `
    <mat-drawer-container class="map" [class.fullscreen]="fullscreen" [hasBackdrop]="false">
      <mat-drawer class="mat-elevation-z2" #drawer mode="side" position="end"
                  [(opened)]="fullscreen && layerPanelOpened">
        <mdb-layer-panel
          [layers]="layersInLayerPanel"
          (zoomRequest)="onZoomRequest($event)"
          [showLabels]="labelsVisible"
          (showLabelsChange)="onShowLabels($event)"
        ></mdb-layer-panel>
      </mat-drawer>
      <mat-drawer-content #drawerContent>
        <xp-map #map>
          <div class="right-panel">
            <button *ngIf="showOutsideExtentWarning"
                    matTooltip="Zooma/panorera kartan så att alla mätobjekt som matchar filtreringen syns"
                    mat-raised-button (click)="showAll()">
              Visa filtrerade
            </button>

            <button mat-raised-button matTooltip="Fullskärm" (click)="toggleFullscreen()">
              <mat-icon>fullscreen</mat-icon>
            </button>

            <button mat-raised-button *ngIf="selectMatobjektInArea"
                    matTooltip="Markera område"
                    (click)="toggleAreaSelection()">
              <mat-icon svgIcon="select_objects"></mat-icon>
            </button>

            <div *ngIf="fullscreen" class="drawer-handle mat-elevation-z2"
                 (click)="layerPanelOpened = !layerPanelOpened">
              <i class="material-icons">{{drawer.opened ? 'arrow_right' : 'arrow_left'}}</i>
            </div>
          </div>

          <div #popupDiv class="ol-popup">
            <a (click)="closeOlPopup()" class="ol-popup-closer"></a>
            <ng-container *ngTemplateOutlet="popupTemplate"></ng-container>
          </div>
        </xp-map>
      </mat-drawer-content>
    </mat-drawer-container>

    <ng-template #matPopupDialogTemplate>
      <div style="position: relative;">
        <a (click)="matPopupDialog.close()" class="mat-popup-closer"></a>
      </div>
      <mat-dialog-content>
        <ng-container *ngTemplateOutlet="popupTemplate"></ng-container>
      </mat-dialog-content>
    </ng-template>

    <ng-template #popupTemplate>
      <div *ngIf="mapinfo">
        <mdb-matobjekt-mapinfo [mapinfo]="mapinfo"
                               [selection]="selectInMap==null ? selection : selectInMap && selection"
                               [selected]="matobjektIsSelectedCallback ? matobjektIsSelectedCallback(mapinfo) : false"
                               [rapporteraLink]="rapporteraLink"
                               (selectedChanged)="selectedChangedReceived($event)">
        </mdb-matobjekt-mapinfo>

        <div *ngIf="mapinfoList && mapinfoList.length > 1">
          <br/>
          <mat-divider></mat-divider>
          Byt till mätobjekt nära markeringen:
          <div class="nearButtons" *ngFor="let m of mapinfoList">
            <button mat-stroked-button *ngIf="m.id !== mapinfo.id"
                    (click)="selectMapInfo(m)">
              <img class="inButtonIcon" [src]="getImageUrl(m)">
              {{m.namn}} - {{m.typNamn}}
            </button>
          </div>
        </div>
      </div>
    </ng-template>
  `,
  styles: [`
    .map {
      height: 100%;
    }
    .map.fullscreen {
      position: fixed;
      top: 0;
      height: 100%;
      left: 0;
      right: 0;
      z-index: 2;
    }

    .right-panel {
      display: flex;
      position: absolute;
      top: 3px;
      right: 0;
      z-index: 10;
    }

    .right-panel > button {
      margin-right: 5px;
    }

    .drawer-handle {
      display: flex;
      cursor: pointer;
      flex-direction: column;
      justify-content: center;
      background: white;
      border-bottom-left-radius: 3px;
      border-top-left-radius: 3px;
    }

    .ol-popup {
      position: absolute;
      background-color: white;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
      padding: 15px;
      border-radius: 10px;
      border: 1px solid #cccccc;
      bottom: 12px;
      left: -50px;
      min-width: 280px;
    }

    .ol-popup:after, .ol-popup:before {
      top: 100%;
      border: solid transparent;
      content: " ";
      height: 0;
      width: 0;
      position: absolute;
      pointer-events: none;
    }

    .ol-popup:after {
      border-top-color: white;
      border-width: 10px;
      left: 48px;
      margin-left: -10px;
    }

    .ol-popup:before {
      border-top-color: #cccccc;
      border-width: 11px;
      left: 48px;
      margin-left: -11px;
    }

    .ol-popup-closer {
      text-decoration: none;
      position: absolute;
      top: 2px;
      right: 8px;
    }

    .ol-popup-closer:after {
      content: "✖";
    }

    .mat-popup-closer {
      text-decoration: none;
      position: absolute;
      top: -17px;
      right: -13px;
    }

    .mat-popup-closer:after {
      content: "✖";
    }

    .nearButtons {
      margin-bottom: 2px;
    }

    .inButtonIcon {
      max-height: 12px;
    }
  `],
})
export class MdbMapComponent implements AfterViewChecked, OnDestroy, OnInit {
  @Input() fullscreen = false;
  @Input() selection = false;
  @Input() selectInMap;
  @Input() rapporteraLink = false;
  @Input() selectMatobjektInArea = false;
  @Output() matobjektInArea = new EventEmitter<number[]>();

  @ViewChild("map", {static: true}) mapComponent: MapComponent;
  @ViewChild("drawer", {static: true}) drawerComponent: MatDrawer;
  @ViewChild("drawerContent", {static: true, read: ElementRef}) drawerContentElement: ElementRef;
  @ViewChild("popupDiv", {static: true}) popupDiv: ElementRef;
  @ViewChild("matPopupDialogTemplate") matPopupDialogTemplate: TemplateRef<any>;

  private ngUnsubscribe = new Subject<void>();

  sizeChange = new Subject();

  layerPanelOpened = false;
  olPopupVisible = false;

  layersInLayerPanel: any[] = [];
  private popup: Overlay;
  drawInteraction;
  drawingLayer;
  drawingSource: VectorSource;
  areaSelectActive = false;
  select: Select;
  polygon: Polygon;

  mapinfo: MatobjektMapinfo;
  mapinfoList: MatobjektMapinfo[];

  selectedInMap: number[] = [];
  selectedFeatures;
  selectedFeaturesExtent: Extent;

  private mapHeight: number;

  matobjektIsSelectedChangeCallback: (mapinfo: MatobjektMapinfo, state: boolean) => void;
  matobjektIsSelectedCallback?: (mapinfo: MatobjektMapinfo) => boolean;
  private viewServiceFitCalled = false;
  private matobjektLayer: any;
  labelsVisible = true;
  matPopupDialog: MatDialogRef<any, any>;

  private geoJsonReader = new GeoJSON();

  private invertedDelomradeStyle = new Style({
    stroke: null,
    fill: new Fill({
      color: "rgba(0, 0, 0, 0.3)"
    })
  });

  private readonly fitOptions = {maxZoom: 8, padding: [15, 15, 15, 15]};

  showOutsideExtentWarning = false;

  constructor(private layerService: LayerService,
              private viewService: ViewService,
              private mapService: MapService,
              private geolocationService: GeolocationService,
              private config: ConfigService,
              private http: HttpClient,
              private userService: UserService,
              private matDialog: MatDialog,
              private kartlagerService: KartlagerService) {

    // Lagren skapas asynkront men "Mätobjekt" behöver vi ha tillgång till direkt, mätobjekt kan komma
    // innan lagerlistan har hämtats
    this.matobjektLayer = new VectorImageLayer({
      source: new VectorSource(),
      style: (feature, resolution) => matobjektStyleFn(feature.get("matobjekt"), resolution, !this.selection, this.labelsVisible),
    });

    // Hämta lagerlistan asynkront (men använd det redan skapade mätobjektlagret på rätt ställe)

    this.kartlagerService.getLayerTree().subscribe(tree => {
      const layerList = toLayerList(http, tree, config.defaultTileGrid, this.matobjektLayer, this.config);
      this.layersInLayerPanel = layerList;
      this.layerService.layers.getLayers().clear();
      this.layerService.layers.getLayers().extend(layerList);

      const defaultKartlagerId = userService.userDetails.defaultKartlagerId;

      if (defaultKartlagerId !== null && defaultKartlagerId !== undefined) {
        const lagret = this.layerService.findLayer(defaultKartlagerId);

        if (lagret !== null && lagret !== undefined) {
          const fitViewportFunction = (event) => {
            lagret.removeEventListener("change", fitViewportFunction);

            if (!this.viewService.initialPositionSet && !this.viewServiceFitCalled) {
              this.viewService.initialPositionSet = true;
              this.viewServiceFitCalled = true;
              this.viewService.fit(getExtentFromLayer(lagret), this.fitOptions);
            }

            return true;
          };
          lagret.addEventListener("change", fitViewportFunction);
        }
      }
    });

    this.sizeChange
      .pipe(distinctUntilChanged((a, b) => a[0] === b[0] && a[1] === b[1]))
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(([width, height]) => {
        this.mapHeight = height;
        this.mapComponent.updateSize();
      });

    geolocationService.startTrackMyLocation({goToLocation: false});
  }

  ngOnInit(): void {
    const map = this.mapService.map;

    this.popup = new Overlay({
      element: this.popupDiv.nativeElement,
      autoPan: {
        animation: {
          duration: 250
        }
      }
    });

    this.select = new Select(
      {
        style: (feature, resolution) => matobjektStyleFn(feature.get("matobjekt"), resolution, true, this.labelsVisible),
        condition: never
      });

    map.addInteraction(this.select);

    this.selectedFeatures = this.select.getFeatures();

    /* Add drawing vector source */
    this.drawingSource = new VectorSource({
      useSpatialIndex: false
    });

    /* Add drawing layer */
    this.drawingLayer = new VectorLayer({
      source: this.drawingSource,
      updateWhileAnimating: true,
      updateWhileInteracting: true
    });
    map.addLayer(this.drawingLayer);

    if (this.polygon) {
      // selectionPolygon was set to early, do it once more
      this.setSelectionPolygon(this.polygon);
    }

    // Drawing interaction
    this.drawInteraction = new Draw({
      source: this.drawingSource,
      type: "Polygon",
    });

    this.drawInteraction.on("drawstart", (event) => {
      this.drawingSource.clear();
    });

    this.drawInteraction.on("drawend", (event) => {
      this.mapService.map.removeInteraction(this.drawInteraction);
      this.areaSelectActive = false;

      this.setSelectionPolygon(event.feature.getGeometry());
    });

    this.mapService.mapExtentChanged$.pipe(takeUntil(this.ngUnsubscribe)).subscribe((evt) => {
      this.calculateShowOutsideExtentWarning();
    });

    this.mapService.click$.pipe(takeUntil(this.ngUnsubscribe)).subscribe((evt) => {
      if (this.areaSelectActive) {
        // Do nothing
      } else {
        const features = this.mapService.map.getFeaturesAtPixel(evt.pixel,
          {
            layerFilter: l => l === this.matobjektLayer,
            hitTolerance: 10
          }
        );

        if (features && features.length > 0) {
          this.mapinfoList = features.map(f => f.get("matobjekt"));

          if (this.mapinfoList.length > 4) {
            this.mapinfoList.length = 4; // Visa bara dom 4 första träffarna
          }

          this.mapinfo = this.mapinfoList[0];

          const featureCoordinate = [
            this.mapinfo.posE,
            this.mapinfo.posN
          ];

          // Hide popup while popup is updated so that the autoPan works
          this.popup.setPosition(undefined);

          if (this.matPopupDialog) {
            this.matPopupDialog.close();
            this.matPopupDialog = null;
          }

          if (this.mapHeight < 300) {
            // Modal popup
            this.matPopupDialog = this.matDialog.open(this.matPopupDialogTemplate, {autoFocus: false});

            if (this.olPopupVisible) {
              this.closeOlPopup();
            }
          } else {
            // Popup i kartan
            if (!this.olPopupVisible) {
              map.addOverlay(this.popup);
              this.olPopupVisible = true;
            }

            // Run this after change detection so that openlayers is able to compute
            // the size of the popup. Otherwise autoPan doesn't work properly.
            setTimeout(() => {
              this.popup.setPosition(featureCoordinate);
            });
          }
        } else if (this.olPopupVisible) {
          this.closeOlPopup();
        }
      }
    });
  }

  getSelectionPolygon(): Polygon {
    return this.polygon;
  }

  setSelectionPolygon(polygon: Polygon) {
    this.polygon = polygon;

    const invertedMapFeature = new Feature(fromExtent(this.config.config.extent));
    invertedMapFeature.setId("invetedMap");

    const invertedGeoBase = this.geoJsonReader.writeFeatureObject(invertedMapFeature);

    const selectionPolygonGeojson = this.geoJsonReader.writeFeatureObject(new Feature(polygon));

    const invertedGeo = difference(<any>invertedGeoBase, <any>selectionPolygonGeojson);

    const invertedFeature = this.geoJsonReader.readFeature(
      invertedGeo);

    invertedFeature.setStyle(this.invertedDelomradeStyle);

    if (this.drawingSource != null) {
      this.drawingSource.clear();
      this.drawingSource.addFeature(invertedFeature);

      this.selectFeaturesInPolygon();
    }
  }

  private selectFeaturesInPolygon() {
    if (this.polygon) {
      this.selectedInMap = [];

      const polygon = this.polygon;
      const polygonExtent = polygon.getExtent();

      this.selectedFeaturesExtent = null;

      this.matobjektLayer.getSource().forEachFeatureInExtent(polygonExtent, (feature) => {
        const featureExtent = feature.getGeometry().getExtent();
        if (polygon.intersectsExtent(featureExtent)) {
          const matobjekt: MatobjektMapinfo = feature.get("matobjekt");

          if (matobjekt) {
            this.selectedInMap.push(matobjekt.id);

            if (this.selectedFeaturesExtent == null) {
              this.selectedFeaturesExtent = featureExtent;
            } else {
              extend(this.selectedFeaturesExtent, featureExtent);
            }
          }
        }
      });
      this.matobjektInArea.emit(this.selectedInMap);
    }
  }

  ngOnDestroy(): void {
    if (this.olPopupVisible) {
      this.closeOlPopup();
    }

    this.mapService.map.removeLayer(this.drawingLayer);
    this.mapService.map.removeInteraction(this.select);

    if (this.areaSelectActive) {
      this.mapService.map.removeInteraction(this.drawInteraction);
    }

    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  setMatobjekt(matobjekt: MatobjektMapinfo[],
               fitExtentIfNoPolygon: boolean,
               fitExtentAndPolygon: boolean = false,
               matobjektIsSelectedCallback?: (mapinfo: MatobjektMapinfo) => boolean,
               matobjektIsSelectedChangeCallback?: (mapinfo: MatobjektMapinfo, state: boolean) => void) {
    this.matobjektIsSelectedChangeCallback = matobjektIsSelectedChangeCallback;
    this.matobjektIsSelectedCallback = matobjektIsSelectedCallback;

    const source = this.matobjektLayer.getSource();

    source.clear();
    this.selectedFeatures.clear();
    this.selectedFeaturesExtent = null;

    for (const m of matobjekt) {
      if (m.posN && m.posE) {
        const iconFeature = new Feature({
          geometry: new Point([
            m.posE,
            m.posN
          ]),
          matobjekt: m
        });

        try {
          source.addFeature(iconFeature);
        } catch (e) {
          console.error("Fel vid addFeature");
          console.error(e);
          console.error(m);
        }
      }
    }

    if ((fitExtentIfNoPolygon && !this.polygon) || fitExtentAndPolygon) {
      try {
        let extent = source.getExtent();

        if (fitExtentAndPolygon && this.polygon) {
          extent = extend(extent, this.polygon.getExtent());
        }

        if (!extent.isEmpty) {
          this.viewServiceFitCalled = true;
          this.viewService.fit(extent, this.fitOptions);
        }
      } catch (e) {
        console.error("Fel vid extent fit");
        console.error(e);
      }
    }

    this.selectedMatobjektUpdated();
    this.selectFeaturesInPolygon();

    this.calculateShowOutsideExtentWarning();
  }

  calculateShowOutsideExtentWarning() {
    this.showOutsideExtentWarning = false;

    if (this.selectedFeaturesExtent != null ) {
      this.showOutsideExtentWarning = !containsExtent(this.mapService.getExtent(), this.selectedFeaturesExtent);
      return;
    }

    const source = this.matobjektLayer.getSource();

    if (source == null) {
      return;
    }

    const extent = source.getExtent();

    if (extent == null) {
      return;
    }

    this.showOutsideExtentWarning = !containsExtent(this.mapService.getExtent(), extent);
  }

  selectedMatobjektUpdated() {
    if (!this.selection) {
      return;
    }

    this.matobjektLayer.getSource().changed();

    this.selectedFeatures.clear();

    const source = this.matobjektLayer.getSource();

    for (const feature of source.getFeatures()) {
      const matobjekt: MatobjektMapinfo = feature.get("matobjekt");

      if (matobjekt) {
        const isSelected = this.matobjektIsSelectedCallback(matobjekt);
        if (isSelected === null || isSelected === true) {
          this.selectedFeatures.push(feature);
        }
      }
    }

  }

  clearMatobjekt() {
    if (this.matobjektLayer) {
      this.matobjektLayer.getSource().clear();
    }
  }

  toggleFullscreen() {
    this.fullscreen = !this.fullscreen;

    // We don't really know the real size at this point but the only thing that matters
    // is that the size has changed. That will trigger the call to refresh the map size.
    this.sizeChange.next([0, 0]);
  }

  ngAfterViewChecked(): void {
    this.sizeChange.next([this.drawerContentElement.nativeElement.offsetWidth, this.drawerContentElement.nativeElement.offsetHeight]);
  }

  closeOlPopup() {
    this.mapService.map.removeOverlay(this.popup);
    this.olPopupVisible = false;
  }

  selectedChangedReceived($event: boolean) {
    this.matobjektIsSelectedChangeCallback(this.mapinfo, $event);
    this.selectedMatobjektUpdated();
  }

  toggleAreaSelection() {
    if (this.areaSelectActive) {
      this.mapService.map.removeInteraction(this.drawInteraction);
      this.areaSelectActive = false;
    } else {
      this.drawingLayer.getSource().clear();
      this.selectedInMap = null;
      this.polygon = null;
      this.selectedFeaturesExtent = null;

      this.calculateShowOutsideExtentWarning();
      this.matobjektInArea.emit(this.selectedInMap);

      this.mapService.map.addInteraction(this.drawInteraction);
      this.areaSelectActive = true;
    }
  }

  clearAreaSelection() {
    if (this.areaSelectActive) {
      this.mapService.map.removeInteraction(this.drawInteraction);
      this.areaSelectActive = false;
    }

    this.selectedFeaturesExtent = null;

    this.drawingLayer.getSource().clear();
    this.selectedInMap = null;
    this.polygon = null;
    this.matobjektInArea.emit(this.selectedInMap);
  }

  onZoomRequest(event: ZoomRequestEvent) {
    this.viewService.fit(event.extent, this.fitOptions);
  }

  onShowLabels(showLabels: boolean) {
    this.labelsVisible = showLabels;
    this.matobjektLayer.getSource().changed(); // Tvinga omritning av lagret
  }

  selectMapInfo(m: MatobjektMapinfo) {
    // Hide popup while popup is updated so that the autoPan works
    this.popup.setPosition(undefined);

    this.mapinfo = m;

    if (this.olPopupVisible) {
      const featureCoordinate = [
        m.posE,
        m.posN
      ];
      this.popup.setPosition(featureCoordinate);
    }
  }

  getImageUrl(m: MatobjektMapinfo) {
    return getStyleImageUrl(m);
  }

  showAll() {
    if (this.selectedFeaturesExtent == null) {
      this.viewService.fit(getExtentFromLayer(this.matobjektLayer), this.fitOptions);
    } else {
      this.viewService.fit(this.selectedFeaturesExtent, this.fitOptions);
    }
  }
}
