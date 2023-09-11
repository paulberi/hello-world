import {Injectable} from "@angular/core";
import View, {FitOptions} from "ol/View";
import Geolocation from "ol/Geolocation";
import {containsExtent, isEmpty} from "ol/extent";
import {ConfigService} from "../config/config.service";
import {Subject} from "rxjs";



@Injectable({
  providedIn: "root"
})
export class ViewService {
  public viewResolutionChanged$: Subject<void> = new Subject<void>();

  view: View;

  public initialPositionSet = false;

  constructor(private configService: ConfigService) {
    const config = configService.config;

    this.view = new View({
      projection: config.projectionCode,
      resolutions: config.resolutions,
      zoom: config.zoom,
      extent: config.extent,
      constrainOnlyCenter: true,
      showFullExtent: true
    });

    const location = this.configService.getInitialLocation();
    if (location.x && location.y && location.zoom) {
      this.initialPositionSet = true;
      this.view.setCenter([location.x, location.y]);
      this.view.setZoom(location.zoom);
    } else if (config.geolocateOnStartup) {
      const geolocation = new Geolocation({
        projection: this.view.getProjection(),
        tracking: true,
        trackingOptions: {
          timeout: 5000
        }
      });

      geolocation.on("change", () => {
        this.view.setCenter(geolocation.getPosition());
        this.view.setZoom(5);
        geolocation.setTracking(false);
      });

      geolocation.on("error", () => {
        this.view.setCenter(config.center);
        geolocation.setTracking(false);
      });
    } else {
      this.view.setCenter(config.center);
    }

    this.view.on("change:resolution", () => {
      this.viewResolutionChanged$.next();
    });
  }

  public getCurrentZoom(): number {
    return this.view.getZoom();
  }

  public getCurrentResolution(): number {
    return this.view.getResolution();
  }

  public zoomToStart() {
      this.zoomToCoordinate(this.configService.config.center, this.configService.config.zoom);
  }

  public zoomToCoordinate(coordinate, zoom) {
      this.view.setCenter(coordinate);
      this.view.setZoom(zoom);
  }

  fit(extent, opt_options?: FitOptions) {
    if (isEmpty(extent)) {
      return;
    }

    const mapExtent = this.configService.config.extent;

    let newExtent;

    if (containsExtent(mapExtent, extent)) {
      newExtent = extent;
    } else {
      newExtent = mapExtent;
    }

    this.view.fit(newExtent, opt_options);
  }

  smoothZoom(delta: number) {
    this.view.animate({
      zoom: this.view.getZoom() + delta,
      duration: 250
    });
  }

  bindToMap(map) {
    map.setView(this.view);
  }

  public getProjection(): string {
    return this.configService.config.projectionCode;
  }
}
