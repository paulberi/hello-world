import {Injectable} from "@angular/core";
import {toLonLat} from "ol/proj";
import {ConfigService} from "../../config/config.service";
import {Coordinate} from "ol/coordinate";


@Injectable({
  providedIn: "root"
})
export class GoogleService {
  projectionCode: string;

  mapsBaseUrl = "https://www.google.com/maps/";
  mapsSearch = "search/?api=1&query=";
  mapsDirections = "dir/?api=1&destination=";

  earthBaseUrl = "https://earth.google.com/web/search/";

  constructor(private configService: ConfigService) {
    this.projectionCode = configService.config.projectionCode;
  }

  /**
   * Returns a Google Maps Search URL to a specific coordinate.
   * @param coordinate (Number[]) The coordinates to the location.
   */
  getMapsSearchUrlFromCoordinates = (coordinate: Coordinate): string => this.getMapsUrl(this.mapsSearch, coordinate);

  /**
   * Returns a Google Maps Directions URL to a specific coordinate.
   * @param coordinate (Number[]) The coordinates to the location.
   */
  getMapsDirectionsUrlFromCoordinates = (coordinate: Coordinate): string => this.getMapsUrl(this.mapsDirections, coordinate);

  /**
   * Returns a Google Earth Search URL to a specific coordinate.
   * @param coordinate (Number[]) The coordinates to the location.
   */
  getEarthSearchUrlFromCoordinates = (coordinate: Coordinate): string => this.getEarthUrl(coordinate);

  /**
   * Forms a Google Maps URL for the The given coordinates.
   * @param api (string) Desired API to target
   * @param coordinate (Number[]) The coordinates to the location.
   */
  private getMapsUrl = (api: string, coordinate: Coordinate): string => `${this.mapsBaseUrl}${api}${this.coordinateToLatLong(coordinate).join()}`;

  /**
   * Forms a Google Earth URL for the The given coordinates.
   * @param coordinate (Number[]) The coordinates to the location.
   */
  private getEarthUrl = (coordinate: Coordinate): string => `${this.earthBaseUrl}${this.coordinateToLatLong(coordinate).join()}`;

  /**
   * Transform coordinates from the configured coordinate system to longitude and latitude.
   * @param coordinate (number[]) coordinates to transform
   */
  private coordinateToLatLong = (coordinate: Coordinate) => toLonLat(coordinate, this.projectionCode).reverse(); // We want [lat, lon]
}
