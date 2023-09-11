import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import {
  ConfigService,
  MapConfig,
} from "../../../../../lib/config/config.service";
import { TranslocoService } from "@ngneat/transloco";
import { Feature, Map, View } from "ol";
import ScaleLine from "ol/control/ScaleLine";
import {
  defaults,
  DragAndDrop,
  Interaction,
  MouseWheelZoom,
  Select,
} from "ol/interaction";
import LayerGroup from "ol/layer/Group";
import { ClickEvent, Keys } from "../../../../../lib/map-core/map.service";
import { MAC } from "ol/has";
import Collection from "ol/Collection";
import { forkJoin, Observable, Subject } from "rxjs";
import { map } from "rxjs/operators";
import { platformModifierKeyOnly } from "ol/events/condition";
import Layer from "ol/layer/Layer";
import VectorSource from "ol/source/Vector";
import VectorLayer from "ol/layer/Vector";
import { GeoJSON } from "ol/format";
import { Stroke, Style } from "ol/style";
import { Geometry, GeometryCollection } from "ol/geom";
import TileWMS from "ol/source/TileWMS";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { Extent, getCenter } from "ol/extent";
import { Coordinate } from "ol/coordinate";
import { SrMapService } from "../../services/map.service";
import BaseLayer from "ol/layer/Base";

interface MapThumbnail {
  id: string;
  title: string;
  url: SafeUrl;
}

export interface FeatureInfoResult {
  layerId: string;
  features: Feature[];
}

export interface FeatureSelectedResult {
  layerId: string;
  features: Feature[];
}

@Component({
  selector: "sr-map",
  templateUrl: "./map.component.html",
  styleUrls: ["./map.component.scss"],
})
export class SrMapComponent implements OnInit, AfterViewInit {
  id: string;
  map: Map;
  mapView: View;
  mapLayers: LayerGroup = new LayerGroup();
  mapThumbnails: MapThumbnail[] = [];
  mapConfig: MapConfig;
  _click$: Subject<ClickEvent> = new Subject();

  isFullscreen = false;
  fullscreen: string;
  leaveFullscreen: string;

  private mapSelectInteraction: Select;
  private lastMapSelectInteractionFeatures: Feature<Geometry>[] = [];

  private geojsonFormat = new GeoJSON();

  /**
   * Lager som kartan ska visa.
   */
  @Input() layers: Layer[] = [];

  /**
   * Bakgrundslager som kartan ska visa.
   */
  @Input() backgroundLayers: Layer[] = [];

  /**
   * Extent som kartan ska anpassa sig efter.
   */
  @Input() extent: Extent;

  /**
   * @param padding [top, right, bottom, left]
   */
  @Input() paddingInMap: Array<number> = [10, 10, 10, 10];

  /**
   * Sätt om kartan ska vara statisk, det vill säga om det ska gå att interagera med den
   */
  @Input() isStatic: boolean = false;

  @Input() allowFileDropImport = false;

  @Output() singleClick = new EventEmitter<Coordinate>();

  /**
   * Skickar ett event vid infoklick i kartan.
   */
  @Output() onInfoClick = new EventEmitter<FeatureInfoResult[]>();

  /**
   * Skickar ett event när geometrier valts i kartan.
   */
  @Output() onSelectionChange = new EventEmitter<FeatureSelectedResult[]>();

  /**
   * Skickar ett event när features laddats från fil.
   */
  @Output() onFeaturesLoaded = new EventEmitter<number>();

  /**
   * Skickar ett event när en fil laddats in i kartan.
   */
  @Output() onFileLoaded = new EventEmitter<string>();

  public importedFeatures = [];
  private dragAndDropInteraction;
  private importedFeatureVectorLayer: VectorLayer<any>;
  private importedFeatureVectorSource: VectorSource;

  constructor(
    transloco: TranslocoService,
    private configService: ConfigService,
    private mapService: SrMapService,
    private http: HttpClient,
  ) {
    this.fullscreen = "Fullskärm";
    this.leaveFullscreen = "Stäng fullskärm";
    this.mapConfig = this.configService.config;
    this.id = "map-" + this.mapService.generateId();
    this.importedFeatureVectorSource = new VectorSource();
  }

  ngOnInit() {
    this.map = this.createMap();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  zoomIn() {
    this.smoothZoom(1);
  }

  zoomOut() {
    this.smoothZoom(-1);
  }

  toggleFullscreen() {
    this.isFullscreen = !this.isFullscreen;
    setTimeout(function () {
      window.dispatchEvent(new Event("resize"));
    }, 200);
  }

  private initMap() {
    this.map.setTarget(this.id);

    this.mapView.setCenter(this.mapConfig.center);
    if (this.extent) {
      this.fitToExtent(this.extent);
    }

    // Map feature selection
    this.mapSelectInteraction = new Select({
      hitTolerance: 0,
      multi: true,
      style: null,
      layers: (l) => l.get("map_selectable") && l.getVisible(),
    });
    this.map.addInteraction(this.mapSelectInteraction);

    this.mapSelectInteraction.on("select", (evt) => {
      // Unselect last selected
      this.clearLastSelection();

      const selectionResult: { [id: string]: FeatureSelectedResult } = {};

      this.mapSelectInteraction.getFeatures().forEach((feature) => {
        feature.set("map_selected", true);
        this.lastMapSelectInteractionFeatures.push(feature);

        const layerId =
          this.mapSelectInteraction.getLayer(feature)?.get("id") || "unknown";

        if (layerId in selectionResult) {
          selectionResult[layerId].features.push(feature);
        } else {
          selectionResult[layerId] = { layerId: layerId, features: [feature] };
        }
      });

      this.onSelectionChange.emit(Object.values(selectionResult));
    });

    // Fånga upp klick i kartan
    this.map.on("singleclick", (event) => {
      this.singleClick.emit(event.coordinate);

      this._click$.next({
        coordinate: <[number, number]>event.coordinate,
        pixel: event.pixel,
        projection: this.mapView.getProjection(),
        resolution: this.mapView.getResolution(),
        keys: this.mapKeys(event),
      });
    });

    this._click$.subscribe((event) => {
      this.handleInfoClickInMap(event);
    });

    if (this.allowFileDropImport) {
      this.dragAndDropInteraction = new DragAndDrop({
        formatConstructors: [GeoJSON as any],
      });
      this.dragAndDropInteraction.on("addfeatures", (event) => {
        this.importedFeatures = [];
        this.map.removeLayer(this.importedFeatureVectorLayer);

        event.features.forEach((f) => {
          this.importedFeatures.push(f);
        });

        this.importedFeatureVectorSource.clear();
        this.importedFeatureVectorSource = new VectorSource({
          features: this.importedFeatures,
        });
        const stroke = new Stroke({
          color: "#CC3300",
          width: 2.25,
        });
        const styles = [
          new Style({
            stroke: stroke,
          }),
        ];
        this.importedFeatureVectorLayer = new VectorLayer({
          source: this.importedFeatureVectorSource,
          style: styles,
        });

        this.map
          .getView()
          .fit(this.importedFeatureVectorSource.getExtent(), {
            padding: this.paddingInMap,
          });
        this.map.addLayer(this.importedFeatureVectorLayer);
        this.onFeaturesLoaded.emit(this.importedFeatures.length);
        this.onFileLoaded.emit(<any>event.file.name);
      });
      this.map.addInteraction(this.dragAndDropInteraction);
    }
  }

  private handleInfoClickInMap(event: ClickEvent): void {
    const layers = this.map.getLayers().getArray();

    const infoCalls = layers
      .filter((layer) => {
        if (!layer.getVisible() || !layer.get("map_info_click")) {
          return false;
        }

        const sourceType = layer.get("source_type") as string;

        if (sourceType == "imagewms" || sourceType == "tilewms") {
          return true;
        }
        return false;
      })
      .map((layer) => {
        return this.getWMSFeatureInfo$(layer, event);
      });

    forkJoin(infoCalls)
      .pipe(
        map((results) => {
          this.onInfoClick.emit(results);
        })
      )
      .subscribe();
  }

  private getWMSFeatureInfo$(
    layer: BaseLayer,
    event: ClickEvent
  ): Observable<FeatureInfoResult> {
    const opts = { queryLayers: null };

    const queryOpts = {
      INFO_FORMAT: "application/json",
      FEATURE_COUNT: 1,
    };
    if (opts.queryLayers) {
      queryOpts["QUERY_LAYERS"] = opts.queryLayers;
    }
    const source = (<any>layer).getSource();

    const url = source.getFeatureInfoUrl(
      event.coordinate,
      event.resolution,
      event.projection,
      queryOpts
    );

    return this.http.get<string>(url).pipe(
      map((featureCollection: string) => {
        return {
          layerId: layer.get("id"),
          features: this.geojsonFormat.readFeatures(featureCollection),
        } as FeatureInfoResult;
      })
    );
  }

  getLayer(layerId): Layer {
    return <Layer>this.mapLayers
      .getLayers()
      .getArray()
      .find((layer) => layer.get("id") === layerId);
  }

  zoomToFeatures(features: Feature[]) {
    const geometries = new GeometryCollection(
      features.map((f) => f.getGeometry())
    );
    const extent = geometries.getExtent();
    if (!extent.some((val) => val === Infinity)) {
      this.mapView.fit(extent, { padding: this.paddingInMap });
    }
  }

  private clearLastSelection() {
    this.lastMapSelectInteractionFeatures.forEach((feature) => {
      feature.set("map_selected", false);
    });
    this.lastMapSelectInteractionFeatures.splice(0);
  }

  clearSelection() {
    this.clearLastSelection();
    this.mapSelectInteraction?.getFeatures().clear();
    this.onSelectionChange.emit([]);
  }

  /**
   * Skapa en kartinstans
   */
  private createMap(): Map {
    let interactions = new Collection<Interaction>();

    // Läs in bakgrundskartor
    this.backgroundLayers.forEach((layer) => {
      this.mapLayers.getLayers().push(layer);
    });

    // Lager från @Input
    this.layers.forEach((layer) => {
      this.mapLayers.getLayers().push(layer);
    });

    if (!this.isStatic) {
      interactions = defaults({
        pinchRotate: false,
        mouseWheelZoom: false,
      }).extend([new MouseWheelZoom({ condition: platformModifierKeyOnly })]);
    }

    /**
     * Allow a 1.5 times standard resolution max. Will affect WMS requests (tile sizes and DPI parameter).
     * TODO: should probably be configurable.
     */
    let pixelRatio = window.devicePixelRatio || 1;
    pixelRatio = Math.min(1.5, pixelRatio);

    this.mapView = this.createView();

    return new Map({
      controls: [new ScaleLine()],
      interactions: interactions,
      pixelRatio: pixelRatio,
      layers: this.mapLayers,
      view: this.mapView,
    });
  }

  /**
   * Skapa en View i OpenLayer
   */
  private createView(): any {
    return new View({
      projection: this.mapConfig.projectionCode,
      resolutions: this.mapConfig.resolutions,
      zoom: this.mapConfig.zoom,
      extent: this.mapConfig.extent,
      constrainOnlyCenter: false,
      showFullExtent: true,
    });
  }

  /**
   * Zooma in eller ut i karta
   */
  private smoothZoom(delta: number) {
    this.mapView.animate({
      zoom: this.mapView.getZoom() + delta,
      duration: 250,
    });
  }

  /**
   * Mappa tangentbordstryck
   */
  private mapKeys(event): Keys {
    return {
      shift: event.originalEvent.shiftKey,
      alt: event.originalEvent.altKey,
      ctrl: event.originalEvent.ctrlKey,
      meta: event.originalEvent.metaKey,
      platformModifier: MAC
        ? event.originalEvent.metaKey
        : event.originalEvent.ctrlKey,
    };
  }

  /**
   * Hämta url för tumbnagel med bakgrundskarta.
   */
  getTileUrl(layer: Layer, coordinate: Coordinate, resolution: number) {
    const source = <TileWMS>layer.getSource();

    const tileGrid = source.getTileGrid();
    const tileCoord = tileGrid.getTileCoordForCoordAndResolution(
      coordinate,
      resolution
    );
    const urlFunction = source.getTileUrlFunction();

    const projection = source.getProjection() || this.mapView.getProjection();

    return urlFunction.bind(source)(tileCoord, 1, projection);
  }

  /**
   * Byt bakgrundskarta.
   */
  switchBackgroundLayer(layerId: string) {
    this.backgroundLayers.forEach((layer) => {
      layer.setVisible(false);
    });
    const selectedBackgroundLayer = this.backgroundLayers.find(
      (layer) => layer.getProperties().id === layerId
    );
    selectedBackgroundLayer.setVisible(true);
  }

  /**
   * Tänd eller släck ett lager
   */
  setLayerVisibility(layerId: string, visible: boolean) {
    const selectedLayer = this.layers.find(
      (layer) => layer.getProperties().id === layerId
    );
    selectedLayer.setVisible(visible);
  }

  /**
   * Anpassa kartan efter extent.
   */
  fitToExtent(extent) {
    this.mapView.fit(extent, { padding: this.paddingInMap });
  }
}
