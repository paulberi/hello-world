import {Injectable} from "@angular/core";
import {extend, isEmpty} from "ol/extent";
import Feature from "ol/Feature";
import GeoJSON from "ol/format/GeoJSON";
import Geometry from "ol/geom/Geometry";
import Point from "ol/geom/Point";
import VectorLayer from "ol/layer/Vector";
import ImageWMS from "ol/source/ImageWMS";
import TileWMS from "ol/source/TileWMS";
import {default as Vector, default as VectorSource} from "ol/source/Vector";
import {Fill, Icon, Stroke, Style} from "ol/style";
import {forkJoin, from, Observable, of, Subscription} from "rxjs";
import {flatMap, tap} from "rxjs/operators";
import {ConfigService} from "../../config/config.service";
import {FeatureInfo} from "../../map-core/feature-info.model";
import {containsFeature, GeoJson} from "../../map-core/geojson.util";
import {LayerService} from "../../map-core/layer.service";
import {MapService} from "../../map-core/map.service";
import {ViewService} from "../../map-core/view.service";
import {bufferGeometry, calculateExtent, intersects} from "../util/feature.util";
import {QueryService} from "./query.service";
import {Place, SokGeoJsonResult, SokResultStatus, SokService, SpatialPredicate} from "./sok.service";
import {StateService} from "./state.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {StyleService} from "./style.service";
import union from "@turf/union";
import difference from "@turf/difference";
import { Subject } from "rxjs";
import MultiPolygon from "ol/geom/MultiPolygon";
import MultiPoint from "ol/geom/MultiPoint";
import { Coordinate } from "ol/coordinate";
import { CoordinatePanelComponent } from "../components/featureinfo/coordinate-panel.component";

const isMapPin = f => f.layer.get("layerDef").id === "map_pin";
const isNotMapPin = f => f.layer.get("layerDef").id !== "map_pin";
const isWmsLayer = fi => fi.layer.getSource() instanceof TileWMS || fi.layer.getSource() instanceof ImageWMS;

@Injectable({
  providedIn: "root"
})
export class SelectionService {
  public maxBuffer = 1000;

  private bufferLayer;
  private placeLayer: VectorLayer<any>;
  private selectedDelomradenLayer: VectorLayer<any>;
  private selectedDelomradenLayerStyle: string;
  private selectedFeaturesLayer: VectorLayer<any>;
  private selectedFeaturesLayerStyle: string;
  private fastighetsgranserOpacity;
  private inverteraDelomraden;
  private unionDelomraden;

  private defaultSelectedDelomradenLayerStyle = [
    new Style({
      stroke: new Stroke({
        color: "#d31c31",
        width: 4
      }),
      fill: new Fill({
        color: "rgba(255, 255, 255, " + this.stateService.getUiStates().fastighetsgranserOpacity + ")"
      })
    }),
    new Style({
      stroke: new Stroke({
        color: "white",
        width: 4,
        lineDash: [20, 28]
      }),
    })
  ];

  private defaultSelectedFeaturesLayerStyle = [
    new Style({
      stroke: new Stroke({
        color: "#0076ff",
        width: 3
      }),
      fill: new Fill({
        color: "rgba(0, 118, 255, 0.25)"
      })
    })
  ];

  private invertedDelomradeStyle = new Style({
    stroke: null,
    fill: new Fill({
      color: "rgba(0, 0, 0, 0.5)"
    })
  });




  private geoJsonReader = new GeoJSON();

  private mapPinCounter = 0;

  private lastFeatureSelection: Feature[] = [];

  private _clearEgenritatomradeSubject = new Subject<void>();
  readonly clearEgenritatomradeSubject$ = this._clearEgenritatomradeSubject.asObservable();

  constructor(private mapService: MapService, private layerService: LayerService, private stateService: StateService,
              private configService: ConfigService,
              private snackBar: MatSnackBar,
              private sokService: SokService, private viewService: ViewService, private queryService: QueryService) {

    this.createBufferLayer();
    this.createPlaceLayer();
    this.createSelectedDelomradenLayer();
    this.createSelectedFeaturesLayer();

    this.stateService.uiStates.subscribe(uiStates => {
      this.selectedDelomradenLayer.setVisible(uiStates.visaFastighetsgranser);

      if (uiStates.fastighetsgranserOpacity !== this.fastighetsgranserOpacity ||
        uiStates.inverteraFastigheter !== this.inverteraDelomraden ||
        uiStates.unionDelomraden !== this.unionDelomraden
      ) {
        let recalculate = false;

        if (this.unionDelomraden  !== uiStates.unionDelomraden) {
          recalculate = true;
        }

        if (this.inverteraDelomraden === false && uiStates.inverteraFastigheter === true) {
          if (uiStates.fastighetsgranserOpacity === 0) {
            uiStates.fastighetsgranserOpacity = 0.5;
          }

          recalculate = true;
        }

        this.unionDelomraden = uiStates.unionDelomraden;
        this.inverteraDelomraden = uiStates.inverteraFastigheter;
        this.fastighetsgranserOpacity = uiStates.fastighetsgranserOpacity;

        if (uiStates.inverteraFastigheter) {
          this.invertedDelomradeStyle.setFill(new Fill({color: "rgba(0,0,0," + uiStates.fastighetsgranserOpacity + ")"}));

          this.selectedDelomradenLayer.getStyle()[0].getFill()
            .setColor("rgba(255,255,255,0)");
        } else {
          this.invertedDelomradeStyle.setFill(null);

          this.selectedDelomradenLayer.getStyle()[0].getFill()
            .setColor("rgba(255,255,255," + uiStates.fastighetsgranserOpacity + ")");
        }

        if (recalculate) {
          this.setSelectedDelomradenLayer(uiStates.valdaDelomraden);
        }

        this.selectedDelomradenLayer.changed();
      }
    });

    this.layerService.getLayerChange().subscribe( () => {
      const activeLayerDefs = this.layerService.getActiveLayersConfiguration();
      let newSelectedDelomradenLayerStyle: string;
      let newSelectedFeaturesLayerStyle: string;


      for (const layerDef of activeLayerDefs) {
        if (layerDef.selectionStyleDelomraden) {
          newSelectedDelomradenLayerStyle = layerDef.selectionStyleDelomraden;
        }

        if (layerDef.selectionStyleFeatures) {
          newSelectedFeaturesLayerStyle = layerDef.selectionStyleFeatures;
        }
      }

      if (newSelectedDelomradenLayerStyle !== this.selectedDelomradenLayerStyle) {
        if (newSelectedDelomradenLayerStyle) {
          const styleDef = this.configService.config.styles[newSelectedDelomradenLayerStyle][0];
          if (styleDef) {
            const newStyle = StyleService.createStyleArray(styleDef);
            this.selectedDelomradenLayer.setStyle(newStyle);

            this.selectedDelomradenLayer.getStyle()[0].getFill()
              .setColor("rgba(255,255,255," + Math.abs(this.stateService.getUiStates().fastighetsgranserOpacity) + ")");

          } else {
            console.log("Okänd stil " + newSelectedDelomradenLayerStyle);
          }
        } else {
          this.selectedDelomradenLayer.setStyle(this.defaultSelectedDelomradenLayerStyle);
        }

        this.selectedDelomradenLayerStyle = newSelectedDelomradenLayerStyle;
      }

      if (newSelectedFeaturesLayerStyle !== this.selectedFeaturesLayerStyle) {
        if (newSelectedFeaturesLayerStyle) {
          const styleDef = this.configService.config.styles[newSelectedFeaturesLayerStyle][0];
          if (styleDef) {
            const newStyle = StyleService.createStyleArray(styleDef);
            this.selectedFeaturesLayer.setStyle(newStyle);
          } else {
            console.log("Okänd stil " + newSelectedFeaturesLayerStyle);
          }
        } else {
          this.selectedFeaturesLayer.setStyle(this.defaultSelectedFeaturesLayerStyle);
        }

        this.selectedFeaturesLayerStyle = newSelectedFeaturesLayerStyle;
      }
    });

    this.queryService.getInfoClick().subscribe(
      ([fastigheter, featureInfos, click]) => {
        let sokFastighetOmAndraTraffar = false;
        if (ConfigService.appConfig.selectFastighetOnlyIfLayerIsActive) {
          const fastighetsytor = this.layerService.getLayer("fastighetsytor");
          if (fastighetsytor) {
            sokFastighetOmAndraTraffar = true;
          }
        }

        // If selectFastighetOnlyIfLayerIsActive is folse:
        // FASTIGHETER from the static FASTIGHETSLAGER are only handled if there are no
        // featureInfos present, otherwise the user could never select anything without
        // also getting the FASTIGHET as well (except if zooming out far enough).

        if (ConfigService.appConfig.multiSelect && click.keys.platformModifier) {
          if (!sokFastighetOmAndraTraffar && featureInfos.length > 0) {
            this.toggleFeatureInfos(featureInfos);
          } else {
            if (sokFastighetOmAndraTraffar) {
              this.toggleFeatureInfos(featureInfos);
            }

            const coordinate = click.coordinate;

            if (coordinate) {
              const point: Point = new Point(coordinate);
              this.queryService.getFeaturesByPolygon(point, SpatialPredicate.INTERSECTS).pipe(
                tap(results => {
                  if (results.sokGeoJsonResult) {
                    this.toggleFastigheter(results.sokGeoJsonResult);
                  }
                })).subscribe();
            }
          }
        } else {
          if (!sokFastighetOmAndraTraffar && featureInfos.length > 0) {

            /*The variables and the loop below ensure that if the user clicks an object in a selected fastighet
             (or more accurately, if a clicked object that has at least a point within a selected fastighet)
              the fastighet's borders do not fade away.
            */
            let objectBelongsToSelectedFastighet  = false;

            const valdaDelomraden = this.stateService.getUiStates().valdaDelomraden;

            for (let omrade of valdaDelomraden) {//breakable for
                let multipolygon: MultiPolygon = new MultiPolygon(omrade.geometry.coordinates);
                let geometry: Geometry =  featureInfos[0]?.feature?.getGeometry();

        /*
          If the object does not have a particular geometry (e.g. Potentiellt förorenade områden), the containsXY seems to be working fine.
          Otherwise the intersects() is the proper way, as we want to check whether a clicked object (with a geometry) has at least
          one point in the fastighet, even if the user clicked on a point outside the fastighet itself.
          TODO: Check if there are cases where an object without a geometry is only partially within a fastighet, and how
          it can be handled if the user clicks on the outside part. Can one manually create a geometry with the same number of dimensions
          as a multipolygon and use intersects()?
        */
                if ((geometry !=null && intersects(multipolygon, geometry))
                || (geometry == null && multipolygon.containsXY(click.coordinate[0],click.coordinate[1]) )){
                  objectBelongsToSelectedFastighet = true;
                  break;
                }
            }

            if(!objectBelongsToSelectedFastighet){
              this.resetSelectedDelomraden();
            }

            this.selectFeatures(featureInfos);
          } else {
            this.resetBuffer();
            const coordinate = click.coordinate;

            if (coordinate) {
              this.removeMapPin();
              this.resetSelectedDelomraden();
              this.resetSelectedFeatures();

              if (sokFastighetOmAndraTraffar) {
                this.selectFeatures(featureInfos);
              }

              const point: Point = new Point(coordinate);
              this.queryService.getFeaturesByPolygon(point, SpatialPredicate.INTERSECTS).pipe(
                tap(results => {
                  if (results.sokGeoJsonResult) {
                    this.addFastigheter(results.sokGeoJsonResult);
                  }
                })).subscribe();
            }
          }
        }
      }
    );
  }

  /**
   * Selekterar alla Fastigheter och features som berör/är inom de inskickade features geometrier.
   * Selektionsläget (berör/inom) beror på featureselectionmode, se getFeaturesByPolygon i query.service.ts
   *
   * Features-parametern kan utelämnas. De features som sparades från förra selektering kommer då att användas.
   *
   * @param features array med OpenLayers Features
   * @param predicate intersects, within...
   * @param bufferDistance Optionellt kan alla geometries buffras
   */
  selectFeaturesByPolygon(features = this.lastFeatureSelection, predicate: SpatialPredicate, bufferDistance): Observable<any> {
    // Remember the feature for later when only the buffer is updated
    this.lastFeatureSelection = features;
    if (!this.lastFeatureSelection || !this.lastFeatureSelection.length) {
      return of();
    }
    this.bufferLayer.getSource().clear();
    this.resetSelectedDelomraden();
    this.resetSelectedFeatures();

    return from(this.lastFeatureSelection).pipe(
      flatMap(feature => {
        let geometry: Geometry = feature.getGeometry();

        if (bufferDistance > 0) {
          geometry = bufferGeometry(geometry, bufferDistance);
          this.bufferLayer.getSource().addFeature(new Feature(geometry));
        } else if (geometry.getType() === "Polygon" || geometry.getType() === "MultiPolygon") {
          // Contract area if INTERSECTS to avoid getting neighbours and
          // expand area if WITHIN to avoid loosing currently selected features
          const d = 0.00000001;
          geometry = bufferGeometry(geometry, predicate === SpatialPredicate.INTERSECTS ? -d : d);
          this.bufferLayer.getSource().addFeature(new Feature(geometry));
        }

        return this.queryService.getFeaturesByPolygon(geometry, predicate).pipe(
          tap(results => {
            if (results.featureInfos) {
              this.addFeatureInfos(results.featureInfos);
            }

            if (results.sokGeoJsonResult) {
              this.addFastigheter(results.sokGeoJsonResult);
            }
        }));
      })
    );
  }

  /**
   * Selekterar alla featureInfos från input arrayen och sätter uistate.
   *
   * Optionellt kan kartan zooma till de valda featureInfos
   *
   * @param featureInfos array med FeatureInfo
   * @param zoomToFit boolean
   */
  selectFeatures(featureInfos: FeatureInfo[], zoomToFit?: boolean, searchMode = false) {
    const selection = [...featureInfos];
    if (searchMode) {
      selection.push(...this.getSelectedFeatureInfos());
    }

    this.resetBuffer();

    this.setSelectedFeaturesLayer(selection);

    let newUiState;
    if (!selection.find(isMapPin)) {
      this.removeMapPin();
      newUiState = {valdaFeatures: selection};
    } else {
      const existingMapPins = this.getSelectedFeatureInfos().filter(isMapPin);
      newUiState = {valdaFeatures: [...existingMapPins, ...selection.filter(isNotMapPin)]};
    }

    this.stateService.setUiStates(newUiState);

    if (zoomToFit) {
      const features = selection.map(f => f.feature);
      const extent = calculateExtent(features);
      if (!isEmpty(extent)) {
        this.mapService.zoomToFit(extent, this.configService.config.zoomToFitMinResolution);
      }
    }
  }

  /**
   * Hämtar och selekterar alla delområden för givna fastigheter
   *
   * Optionellt kan kartan zooma till de resulterande fastighetsdelområdena
   *
   * @param zoomToFit boolean
   */
  setKomplettFastighet(objektIds: string[], zoomToFit?: boolean, searchMode = false) {
    const promise = new Promise<void>((resolve, reject) => {
      const requests: Observable<GeoJson[]>[] = [];

      for (const objektId of objektIds) {
        requests.push(this.sokService.getDelomradenForFastighet(objektId));
      }

      forkJoin(requests).subscribe(hitsArray => {
        const hits: GeoJson[] = [];

        for (const h of hitsArray) {
          hits.push(...h);
        }

        this.setSelectedDelomraden(hits, zoomToFit, searchMode);

        resolve();
      });
    });

    return promise;
  }

  /**
   * Placera en kartnål vid ett plats (Place)
   *
   * Optionellt kan kartan zooma till platsen
   *
   * @param place
   * @param zoomToPlace
   */
  setPlace(place: Place, zoomToPlace: boolean, searchMode = false) {
    const selectedPinFeatures: any[] = [...this.getSelectedFeatureInfos()].filter(isMapPin);
    const selectedPins = this.placeLayer.getSource().getFeatures();

    this.resetSelectedDelomraden();
    this.removeMapPin();

    if (place.feature) {
      const features = [place.feature];
      if (searchMode) {
        features.push(...selectedPinFeatures);
      }

      this.setSelectedDelomradenLayer(features);
      if (zoomToPlace) {
        this.zoomAndPanToSelectedDelomraden();
      }
    } else {
      const iconFeature = new Feature({
        geometry: new Point(place.coordinate),
        name: place.name
      });
      iconFeature.setId("map_pin." + this.mapPinCounter++);

      const iconFeatures = [iconFeature];
      if (searchMode) {
        iconFeatures.push(...selectedPins);
      }

      iconFeatures.forEach(f => this.placeLayer.getSource().addFeature(f));

      const featureInfo = new FeatureInfo(iconFeature, this.placeLayer);
      this.setMapPinFeatures([featureInfo]);

      if (zoomToPlace) {
        const coord = place.coordinate;
        let zoom;
        if (place.type === "1119" || place.type === "ANLTX") {
          zoom = 6;
        } else if (place.type === "3136") {
          zoom = 10;
        } else if (!+place.type) {
          zoom = 5;
        } else {
          zoom = 12;
        }
        this.viewService.zoomToCoordinate(coord, zoom);
      }
    }
  }

  /**
   * Placera flera kartnålar vid platser (Place)
   *
   * Optionellt kan kartan zooma till platsen
   *
   * @param place
   * @param zoomToPlace
   */
  setPlaces(places: Place[], zoomToPlace: boolean) {
    this.resetSelectedDelomraden();
    this.removeMapPin();

    const featureInfos: FeatureInfo[] = [];

    for (const place of places) {
      if (place.feature) {
        this.setSelectedDelomradenLayer([place.feature]);
      } else {
        const iconFeature = new Feature({
          geometry: new Point(place.coordinate),
          name: place.name
        });
        iconFeature.setId("XXXmap_pin." + this.mapPinCounter++);
        this.placeLayer.getSource().addFeature(iconFeature);

        featureInfos.push(new FeatureInfo(iconFeature, this.placeLayer));

      }
    }

    this.setMapPinFeatures(featureInfos);

    if (zoomToPlace) {
      const delomradeExtent = this.selectedDelomradenLayer.getSource().getExtent();
      const placeExtent = this.placeLayer.getSource().getExtent();

      const extent = extend(delomradeExtent, placeExtent);

      this.mapService.zoomToFit(extent, this.configService.config.zoomToFitMinResolution);
    }
  }


  /**
   * Avselekterar alla valda featureinfos
   */
  resetSelectedFeatures() {
    this.selectedFeaturesLayer.getSource().clear();
    this.stateService.setUiStates({valdaFeatures: []});
  }

  deselectFeature(id: string) {
    this.deselectWithObjektId(this.selectedFeaturesLayer.getSource(), id);
    const valdaFeatures = this.stateService.getUiStates().valdaFeatures.filter(f => f.feature.get("objekt_id") !== id);
    this.stateService.setUiStates({valdaFeatures: valdaFeatures});
    this.deselectEgenRitatOmrade();
  }

  /**
   * Avselekterar alla valda fastigheter/delområden
   */
  resetSelectedDelomraden() {
    this.selectedDelomradenLayer.getSource().clear();
    this.stateService.setUiStates({valdaDelomraden: []});
  }

  deselectOmrade(objektId: string) {
    this.deselectWithObjektId(this.selectedDelomradenLayer.getSource(), objektId);
    const valdaDelomraden = this.stateService.getUiStates().valdaDelomraden.filter(d => d.properties.objekt_id !== objektId);

    this.setSelectedDelomraden(valdaDelomraden);
    this.stateService.setUiStates({valdaDelomraden: valdaDelomraden});
  }

  deselectDelomrade(externId: string) {
    this.deselectWithExternId(this.selectedDelomradenLayer.getSource(), externId);
    const valdaDelomraden = this.stateService.getUiStates().valdaDelomraden.filter(d => d.properties.externid !== externId);
    this.setSelectedDelomraden(valdaDelomraden);
    this.stateService.setUiStates({valdaDelomraden: valdaDelomraden});
  }

  deselectEgenRitatOmrade() {
    const valdaDelomraden = this.deselectEgenRitatOmradeFromSpecifiedSources(
        this.selectedDelomradenLayer.getSource(),
        this.stateService.getUiStates().valdaDelomraden);
    this.setSelectedDelomraden(valdaDelomraden);
  }

  private deselectEgenRitatOmradeFromSpecifiedSources(source: VectorSource, delomraden: GeoJson[]) {
    this.deselectEgetRitatOmrade(source);
    const valdaDelomraden = delomraden.filter(d => d.id !== "RITAD_YTA");
    this.stateService.setUiStates({valdaDelomraden: valdaDelomraden});
    this._clearEgenritatomradeSubject.next();
    return valdaDelomraden;
  }

  private deselectWithObjektId(source: VectorSource, objektId: string) {
    const features = source.getFeatures().filter(f => f.get("objekt_id") === objektId);
    features.forEach(f => source.removeFeature(f));
  }

  private deselectWithExternId(source: VectorSource, externId: string) {
    const features = source.getFeatures().filter(f => f.get("externid") === externId);
    features.forEach(f => source.removeFeature(f));
  }

  private deselectEgetRitatOmrade(source: VectorSource) {
    const features = source.getFeatures().filter(f => f.getId() === "RITAD_YTA");
    features.forEach(f => source.removeFeature(f));
  }

  /**
   * Tömmer bufferlagret och glömmer bort den föregående geometri selekteringen
   */
  resetBuffer() {
    this.bufferLayer.getSource().clear();
    this.lastFeatureSelection = [];
  }

  /**
   * Zoomar kartan till valda delområden
   */
  zoomAndPanToSelectedDelomraden() {
    this.mapService.zoomToFit(this.selectedDelomradenLayer.getSource().getExtent(), this.configService.config.zoomToFitMinResolution);
  }

  setSelectedDelomraden(delomraden: GeoJson[], zoomToFit?: boolean, searchMode = false ) {
    const currentValdaDelomraden = this.stateService.getUiStates().valdaDelomraden;
    const currentValdaDelomradenIds = currentValdaDelomraden.map(f => f.id);
    const newDelomradenIds = delomraden.map(f => f.id);

    let newDelomraden = currentValdaDelomraden;

    if (!searchMode) {
      // remove deselected features
      newDelomraden = currentValdaDelomraden.filter(f => newDelomradenIds.includes(f.id));
    }

    // remove duplicates
    const removedDuplicates = delomraden.filter(f => !currentValdaDelomradenIds.includes(f.id));

    newDelomraden = newDelomraden.concat(removedDuplicates);

    // finns det mer än ett delområde så får inget av dem vara ett ritat område (id RITAD_YTA)
    // ta bort ritade områden om det finns mer än ett delområde
    const valdaDelomraden = newDelomraden.length > 1 && newDelomraden.filter(f => f.id === "RITAD_YTA").length > 0 ?
        this.deselectEgenRitatOmradeFromSpecifiedSources(this.selectedDelomradenLayer.getSource(), newDelomraden) :
        newDelomraden;

    this.setSelectedDelomradenLayer(valdaDelomraden);

    this.stateService.setUiStates({valdaDelomraden: valdaDelomraden});

    if (zoomToFit) {
      this.zoomAndPanToSelectedDelomraden();
    }
  }

  private addFeatureInfos(featureInfos: FeatureInfo[]) {
    // Since it is known limitation that WMS-features are not queryable with geometries, we
    // intentionally toggle here instead or otherwise they would become deselected themselves
    this.toggleFeatureInfos(featureInfos);
  }

  private setMapPinFeatures(mapPins: FeatureInfo[]) {
    this.resetBuffer();

    const existingFeatures = this.getSelectedFeatureInfos();
    const notMapPinFeatures = existingFeatures
      .filter(isNotMapPin);

    const newFeaturesList = [...notMapPinFeatures, ...mapPins];

    this.setSelectedFeaturesLayer(newFeaturesList);
    this.stateService.setUiStates({valdaFeatures: newFeaturesList});
  }

  toggleFeatureInfos(features: FeatureInfo[]) {
    if (features && features.length) {
      const existingFeatures = this.getSelectedFeatureInfos();
      const filteredFeatures = existingFeatures.concat(features)
        .filter(isNotMapPin)
        .filter(fi => !(this.containsFeatureInfo(features, fi) && this.containsFeatureInfo(existingFeatures, fi)));

      // Normally the map pin is removed when clicking anywhere outside the pin, but during toggle mode the
      // behaviour is suppressed. Perhaps the map pins should be treated as a normal features which can be
      // selected or deselected as any other feature?
      const existingMapPins = existingFeatures.filter(isMapPin);
      if (existingMapPins) {
        filteredFeatures.push(...existingMapPins);
      }

      this.selectFeatures(filteredFeatures);
    }
  }

  private containsFeatureInfo(featureInfos: FeatureInfo[], featureInfo: FeatureInfo): boolean {
    return featureInfos.find(fi => fi.feature.getId() === featureInfo.feature.getId()) !== undefined;
  }

  private addFastigheter(sokGeoJsonResult: SokGeoJsonResult) {
    const delomraden: GeoJson[] = sokGeoJsonResult.features;

    if (sokGeoJsonResult.status === SokResultStatus.TO_MANY_RESULTS) {
      this.snackBar.open("För många fastigheter, endast en delmängd visas.", "OK", {
        verticalPosition: "top"
      });
    }

    if (delomraden && delomraden.length) {
      const selectedDelomraden = this.getSelectedDelomraden();

      // Since the map only knows of FASTIGHETSDELOMRÅDEN and we select either those OR whole FASTIGHETER,
      // it is necessary to handle both cases here to keep the behaviour consistent...

      if (this.getValjHelaFastigheter() && sokGeoJsonResult.status !== SokResultStatus.TO_MANY_RESULTS) {
        const fastigheterToAdd: string[] = [];

        // Get all FASTIGHETSDELOMRÅDEN for all the new FASTIGHETER
        delomraden.forEach(f => {
          if (f.properties && f.properties.objekt_id && !fastigheterToAdd.find(objektId => objektId === f.properties.objekt_id)) {
            fastigheterToAdd.push(f.properties.objekt_id);
          }
        });

        // Add all FASTIGHETSDELOMRÅDEN that don't belong to a FASTIGHET marked for addition.
        // This will also include those missing OBJEKT_ID in case they aren't present.
        // A FASTIGHETSDELOMRÅDE which was already present and also marked for addition will hence be removed,
        // but later on be added back after the full FASTIGHET has been returned.
        const filteredFeatures = selectedDelomraden.concat(delomraden
          .filter(f => !containsFeature(selectedDelomraden, f))
          .filter(f => !f.properties || !fastigheterToAdd.find(objektId => objektId === f.properties.objekt_id)));

        if (fastigheterToAdd.length) {
          // Fetch the full FASTIGHET for all the new FASTIGHETSDELOMRÅDEN
          this.sokService.getDelomradenForFastigheter(fastigheterToAdd).subscribe(d => {
            this.addSelectedDelomraden(d);
          });
        } else {
          this.setSelectedDelomraden(filteredFeatures);
        }
      } else {
        // Add all existing FASTIGHETSDELOMRÅDEN that are only present in one of the sets
        const filteredFeatures = selectedDelomraden.concat(delomraden
          .filter(f => !containsFeature(selectedDelomraden, f)));
        this.setSelectedDelomraden(filteredFeatures);
      }
    }
  }

  private toggleFastigheter(sokGeoJsonResult: SokGeoJsonResult) {
    const delomraden: GeoJson[] = sokGeoJsonResult.features;

    if (delomraden && delomraden.length) {
      const selectedDelomraden = this.getSelectedDelomraden();

      // Since the map only knows of FASTIGHETSDELOMRÅDEN and we select either those OR whole FASTIGHETER,
      // it is necessary to handle both cases here to keep the behaviour consistent...

      if (this.getValjHelaFastigheter()) {
        const fastigheterToAdd: string[] = [];
        const fastigheterToRemove: string[] = [];

        // If a user clicks on a selected FASTIGHETSDELOMRÅDE (= deselects it), then all other
        // FASTIGHETSDELOMRÅDEN belonging to the same FASTIGHET will also be deselected. Similarily
        // if the user clicks on a FASTIGHETSDELOMRÅDE that isn't already selected then all the other
        // FASTIGHETSDELOMRÅDEN belonging to the same FASTIGHET will also be selected.
        delomraden.forEach(f => {
          if (f.properties && f.properties.objekt_id) {
            if (containsFeature(selectedDelomraden, f)) {
              fastigheterToRemove.push(f.properties.objekt_id);
            } else {
              fastigheterToAdd.push(f.properties.objekt_id);
            }
          }
        });

        // Add all FASTIGHETSDELOMRÅDEN that don't belong to a FASTIGHET marked for either addition or removal.
        // This will also include those missing OBJEKT_ID in case they aren't present in both sets.
        // A FASTIGHETSDELOMRÅDE which was already present and also marked for addition will hence be removed,
        // but later on be added back after the full FASTIGHET has been returned.
        const filteredFeatures = selectedDelomraden.concat(delomraden)
          .filter(f => !(containsFeature(delomraden, f) && containsFeature(selectedDelomraden, f)))
          .filter(f => !f.properties || !fastigheterToAdd.find(objektId => objektId === f.properties.objekt_id))
          .filter(f => !f.properties || !fastigheterToRemove.find(objektId => objektId === f.properties.objekt_id));

        if (fastigheterToAdd.length) {
          // Fetch the full FASTIGHET for all the new FASTIGHETSDELOMRÅDEN
          this.sokService.getDelomradenForFastigheter(fastigheterToAdd).subscribe(d => {
            this.addSelectedDelomraden(d);
          });
        } else {
          this.setSelectedDelomraden(filteredFeatures);
        }
      } else {
        // Add all existing FASTIGHETSDELOMRÅDEN that are only present in one of the sets
        const filteredFeatures = selectedDelomraden.concat(delomraden)
          .filter(f => !(containsFeature(delomraden, f) && containsFeature(selectedDelomraden, f)));
        this.setSelectedDelomraden(filteredFeatures);
      }
    }
  }

  private getSelectedFeatureInfos() {
    return this.stateService.getUiStates().valdaFeatures;
  }

  private getValjHelaFastigheter() {
    return this.stateService.getUiStates().valjHelaFastigheter;
  }

  private getSelectedDelomraden() {
    return this.stateService.getUiStates().valdaDelomraden;
  }

  removeMapPin() {
    this.placeLayer.getSource().clear();
  }

  private addSelectedDelomraden(newDelomraden: GeoJson[], zoomToFit?: boolean) {
    const existingDelomraden = this.getSelectedDelomraden();
    const filteredDelomraden = newDelomraden.filter(f1 => !existingDelomraden.find(f2 => f1.id === f2.id));
    this.setSelectedDelomraden(existingDelomraden.concat(filteredDelomraden));
  }

  private setSelectedDelomradenLayer(delomraden: GeoJson[]) {
    this.selectedDelomradenLayer.getSource().clear();

    let calculateUnion;

    if (this.inverteraDelomraden) {
      calculateUnion = true;
    } else {
      calculateUnion = this.unionDelomraden;
    }

    if (delomraden && delomraden.length) {
      if (!this.unionDelomraden) {
        const features = delomraden.map(f => this.geoJsonReader.readFeature(
          f,
          {dataProjection: "EPSG:3006", featureProjection: this.configService.config.projectionCode}));
        features.forEach(f => f.setStyle(undefined));

        this.selectedDelomradenLayer.getSource().addFeatures(features);
      }

      let unionDelomrade;

      if (calculateUnion) {
        for (const delomr of delomraden) {
          if (!unionDelomrade) {
            unionDelomrade = delomr;
          } else {
            unionDelomrade = union(unionDelomrade, <any>delomr);
          }
        }
      }

      if (this.unionDelomraden) {
        const feature = this.geoJsonReader.readFeature(
          unionDelomrade,
          {dataProjection: "EPSG:3006", featureProjection: this.configService.config.projectionCode});
        feature.setStyle(undefined);
        this.selectedDelomradenLayer.getSource().addFeatures([feature]);
      }

      if (this.inverteraDelomraden) {
        // En polygon som täcker hela kartan, vi gör hål i  dom delar som inte ska blir mörka.
        const invertedGeoBase: GeoJson = {
          type: "Feature", id: "inveretedDelomraden",
          geometry_name: "geom",
          geometry: {
            type: "MultiPolygon",
            coordinates: [
              [
                [
                  [190000, 6100000],
                  [190000, 7700000],
                  [950000, 7700000],
                  [950000, 6100000],
                  [190000, 6100000]
                ]
              ]
            ]
          },
        };

        const invertedGeo = difference(<any>invertedGeoBase, unionDelomrade);

        const invertedFeature = this.geoJsonReader.readFeature(
          invertedGeo,
          {dataProjection: "EPSG:3006", featureProjection: this.configService.config.projectionCode});

        invertedFeature.setStyle(this.invertedDelomradeStyle);

        this.selectedDelomradenLayer.getSource().addFeatures([invertedFeature]);
      }
    }
  }

  private setSelectedFeaturesLayer(featureInfos: FeatureInfo[]) {
    this.selectedFeaturesLayer.getSource().clear();
    featureInfos
      .filter(isWmsLayer)
      .forEach(fi => this.selectedFeaturesLayer.getSource().addFeature(fi.feature));
  }

  private createBufferLayer() {
    this.bufferLayer = new VectorLayer({
      source: new VectorSource(),
      style: new Style({
        fill: new Fill({
            color: "rgba(255,255,255,0.4)"
          }),
          stroke: new Stroke({
            color: "#3399CC",
            width: 2
          }),
        })
    });
    this.layerService.addInternalLayer(this.bufferLayer);
    this.bufferLayer.setZIndex(6000);
  }

  private createPlaceLayer() {
    const iconStyle = new Style({
      image: new Icon({
        anchor: [0.5, 31],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        opacity: 0.75,
        src: "assets/lib/icons/map_pin.png"
      })
    });
    this.placeLayer = new VectorLayer(<any>{
      source: new VectorSource(),
      style: iconStyle,
      layerDef: {
        id: "map_pin",
        queryable: true
      }
    });
    this.layerService.addInternalLayer(this.placeLayer);
  }

  private createSelectedDelomradenLayer() {
    this.selectedDelomradenLayer = new VectorLayer(<any>{
      source: new VectorSource(),
      style: this.defaultSelectedDelomradenLayerStyle,
      visible: false,
      layerDef: {
        id: "selected_features",
        queryable: true
      }
    });
    this.layerService.addInternalLayer(this.selectedDelomradenLayer);
  }

  private createSelectedFeaturesLayer() {
    this.selectedFeaturesLayer = new VectorLayer({
      source: new Vector(),
      style: this.defaultSelectedFeaturesLayerStyle
    });
    this.layerService.addInternalLayer(this.selectedFeaturesLayer);
  }
}
