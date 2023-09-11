import {ElementRef, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

import Map from "ol/Map";
import ScaleLine from "ol/control/ScaleLine";
import DragPan from "ol/interaction/DragPan";
import Kinetic from "ol/Kinetic";
import {defaults} from "ol/interaction";

import {MAC} from "ol/has";

import {ViewService} from "./view.service";
import {LayerService} from "./layer.service";

import {merge, Observable, of, Subject} from "rxjs";
import {catchError, filter, map, share, switchMap, throttleTime} from "rxjs/operators";

import {StorageItem, StorageService} from "./storage.service";
import {FeatureInfo} from "./feature-info.model";
import {GeoJson} from "./geojson.util";
import {Extent} from "ol/extent";

export interface ClickEvent {
  coordinate: [number, number];
  pixel: any;
  projection;
  resolution: number;
  keys?: Keys;
}

export interface Keys {
  shift: boolean;
  alt: boolean;
  ctrl: boolean;
  meta: boolean;
  platformModifier: boolean;
}

export interface FeatureCollection {
  features: GeoJson[];
}

export interface ClickResult {
  fc?: FeatureCollection;
  fi?: FeatureInfo[];
  click: ClickEvent;
}

@Injectable({
  providedIn: "root"
})
export class MapService {
  private _map;
  private _click$: Subject<ClickEvent> = new Subject();
  public mapExtentChanged$: Subject<Extent> = new Subject<Extent>();

  private _padding = [0, 0, 0, 0];

  constructor(private http: HttpClient, private viewService: ViewService, private layerService: LayerService,
              private storageService: StorageService) {
    const interactions = defaults({
      dragPan: false,
      pinchRotate: false,
      doubleClickZoom: false
    });
    // A slightly less slippy drag pan interaction. The idea is to generate fewer requests.
    interactions.push(new DragPan({
      kinetic: new Kinetic(-0.0080, 0, 200)
    }));

    /**
     * Allow a 1.5 times standard resolution max. Will affect WMS requests (tile sizes and DPI parameter).
     * TODO: should probably be configurable.
     */
    let pixelRatio = window.devicePixelRatio || 1;
    pixelRatio = Math.min(1.5, pixelRatio);

    this._map = new Map({
      controls: [new ScaleLine()],
      interactions: interactions,
      pixelRatio: pixelRatio
    });

    layerService.bindToMap(this._map);
    viewService.bindToMap(this._map);

    this._map.on("moveend", () => {
      this.storageService.updateItem(StorageItem.QUERY_PARAMETERS, this.getLocation());

      this.mapExtentChanged$.next(this.getExtent());
    });

    // Set up the click observable. See public method for description.
    this._map.on("click", (event) => {
      this._click$.next({
        coordinate: event.coordinate,
        pixel: event.pixel,
        projection: this.viewService.view.getProjection(),
        resolution: this.viewService.view.getResolution(),
        keys: this.mapKeys(event)
      });
    });
  }

  private mapKeys(event): Keys {
    return {
      shift: event.originalEvent.shiftKey,
      alt: event.originalEvent.altKey,
      ctrl: event.originalEvent.ctrlKey,
      meta: event.originalEvent.metaKey,
      platformModifier: (MAC ? event.originalEvent.metaKey : event.originalEvent.ctrlKey)
    };
  }

  setTarget(element: ElementRef) {
    if (element) {
      this._map.setTarget(element.nativeElement);
    } else {
      this._map.setTarget(null);
    }
  }

  updateSize() {
    this._map.updateSize();
  }

  get map() {
    return this._map;
  }

  setFitPadding(fitPadding: [number, number, number, number]) {
    this._padding = fitPadding;
  }

  /**
   * An observable emitting a ClickEvent for each click in the map. Can be used for whatever.
   */
  get click$() {
    return this._click$.pipe(
      throttleTime(500)
    );
  }

  /**
   * Returns a new hot observable for quering a WMS layer.
   * If the layer is invisible and ignoreInvisible is true, an empty response is
   * immediately returned. Otherwise the layer is queried anyway.
   *
   * If you want to query a layer that is a part of a layer group defined in the WMS server, the options queryLayers can be used.
   * For example, getInfoClick$(layer, ['Layer4', 'Layer5']) will only return results from Layer4 and Layer5.
   */
  getInfoClick$(wmsLayer, opts?): Observable<ClickResult> {
    opts = {queryLayers: null, ...opts};

    const emptyResponse = this._click$.pipe(
      filter(() => !wmsLayer.getVisible()),
      map(click => {
        return {fc: {features: []}, click: click};
      })
    );

    const featureInfoResponse = this.click$.pipe(
      filter(() => wmsLayer.getVisible()),
      switchMap(click => {
        const queryOpts = {
          "INFO_FORMAT": "application/json",
          "FEATURE_COUNT": 1
        };
        if (opts.queryLayers) {
          queryOpts["QUERY_LAYERS"] = opts.queryLayers;
        }
        const source = (<any>wmsLayer).getSource();
        const url = source.getFeatureInfoUrl(click.coordinate, click.resolution, click.projection, queryOpts);
        return this.http.get<FeatureCollection>(url).pipe(
          map((fc: FeatureCollection) => ({fc: fc, click: click})),
          catchError((err, caught) => {
            return of({fc: {features: []}, click: click});
          })
        );
      })
    );

    return merge(emptyResponse, featureInfoResponse).pipe(share());
  }

  addInteraction(iAction) {
    this._map.addInteraction(iAction);
  }

  removeInteraction(iAction) {
    this._map.removeInteraction(iAction);
  }

  zoomToFit(extent, minResolution?) {
    this.viewService.fit(extent, {padding: this._padding, duration: 600, minResolution: minResolution});
  }

  getLocation() {
    const coords = this.map.getView().getCenter();
    const zoom = this.map.getView().getZoom();
    return {x: coords[0], y: coords[1], zoom: zoom};
  }

  getExtent(): Extent {
    return this._map.getView().calculateExtent(this._map.getSize());
  }
}
