import {ConfigService} from "../../config/config.service";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class CoordsystemService {
  coordSystems: Map<string, [string, string]>;

  appCoordSystemText: string;
  appCoordOrderText: string;

  constructor(private configService: ConfigService) {
    // TODO: This should be populated from some resource rather than manually.
    this.coordSystems = new Map<string, [string, string]>();
    this.coordSystems.set("EPSG:3006", ["SWEREF 99 TM", "N, E"]);
    this.coordSystems.set("EPSG:3007", ["SWEREF 99 12 00", "N, E"]);
    this.coordSystems.set("EPSG:3008", ["SWEREF 99 13 30", "N, E"]);
    this.coordSystems.set("EPSG:3009", ["SWEREF 99 15 00", "N, E"]);
    this.coordSystems.set("EPSG:3010", ["SWEREF 99 16 30", "N, E"]);
    this.coordSystems.set("EPSG:3018", ["SWEREF 99 23 15", "N, E"]);
    this.coordSystems.set("EPSG:3857", ["WGS 84 / Pseudo-Mercato", "N, E"]);

    if (this.coordSystems.has(this.configService.config.projectionCode)) {
      this.appCoordSystemText = this.coordSystems.get(this.configService.config.projectionCode)[0];
      this.appCoordOrderText = this.coordSystems.get(this.configService.config.projectionCode)[1];
    } else {
      this.appCoordSystemText = this.configService.config.projectionCode;
      this.appCoordOrderText = "(undefined system)";
      console.error("Projection code " + this.configService.config.projectionCode + " not defined in CoordsystemService!");
    }
  }

  /**
   * Get coordinate system text for current app coordinate system if defined, else get projection code.
   */
  getAppCoordSystemText(): string {
    return this.appCoordSystemText;
  }

  /**
   * Get coordinate system text for specific projection code.
   * @param code (string) Projection code.
   */
  getCoordSystemTextFromCode(code: string): string {
    if (this.coordSystems.has(code)) {
      return this.coordSystems.get(code)[0];
    } else {
      console.error("Projection code " + this.configService.config.projectionCode + " not defined in CoordsystemService!");
      return this.configService.config.projectionCode;
    }
  }

  /**
   * Get text declaring order of coordinates for current app coordinate system.
   */
  getAppCoordOrderText(): string {
    return this.appCoordOrderText;
  }

  /**
   * Get coordinate order text for specific projection code.
   * @param code (string) Projection code.
   */
  getCoordOrderTextFromCode(code: string): string {
    if (this.coordSystems.has(code)) {
      return this.coordSystems.get(code)[1];
    } else {
      console.error("Projection code " + this.configService.config.projectionCode + " not defined in CoordsystemService!");
      return "(undefined system)";
    }
  }
}
