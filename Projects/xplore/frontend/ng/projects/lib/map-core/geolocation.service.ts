import {Injectable} from "@angular/core";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import Style from "ol/style/Style";
import Stroke from "ol/style/Stroke";
import Fill from "ol/style/Fill";
import Circle from "ol/style/Circle";
import Geolocation from "ol/Geolocation";
import {ViewService} from "./view.service";
import {LayerService} from "./layer.service";

const positionStyle = new Style({
  image: new Circle({
    radius: 6,
    fill: new Fill({
      color: "#3399CC"
    }),
    stroke: new Stroke({
      color: "#fff",
      width: 3
    })
  })
});

@Injectable({
  providedIn: "root"
})
export class GeolocationService {
  private readonly positionLayer: VectorLayer<any>;
  private readonly positionFeature: Feature;
  private readonly geolocation: Geolocation;

  constructor(private viewService: ViewService, private layerService: LayerService) {
    this.positionFeature = new Feature();
    this.positionFeature.setStyle(positionStyle);

    this.positionLayer = new VectorLayer({
      source: new VectorSource({
        features: [this.positionFeature]
      })
    });
    this.positionLayer.setZIndex(9999);
    this.layerService.addInternalLayer(this.positionLayer);

    this.geolocation = new Geolocation({
      projection: this.viewService.view.getProjection(),
      trackingOptions: {
        timeout: 5000,
        enableHighAccuracy: true
      }
    });
    this.geolocation.on("change:position", () => {
      this.updateMylocation();
    });
    this.geolocation.on("error", (error) => {
      console.log(error);
    });

  }

  private goToGeolocation() {
    const coordinates = this.geolocation.getPosition();
    if (coordinates != null) {
      this.updateMylocation();
      this.viewService.zoomToCoordinate(coordinates, 8);
    }
  }

  private updateMylocation() {
    this.positionFeature.setGeometry(this.geolocation.getPosition() != null ? new Point(this.geolocation.getPosition()) : null);
  }

  public startTrackMyLocation(opts?: TrackingOptions) {
    opts = {goToLocation: true, ...opts};

    this.geolocation.setTracking(true);

    if (opts.goToLocation) {
      this.geolocation.once("change", () => {
        this.goToGeolocation();
      });
    }

    this.positionLayer.setVisible(true);
  }

  public stopTrackMyLocation() {
    this.geolocation.setTracking(false);
    this.positionLayer.setVisible(false);
  }

  public isTracking() {
    return this.geolocation.getTracking();
  }
}

export interface TrackingOptions {
  goToLocation: boolean;
}
