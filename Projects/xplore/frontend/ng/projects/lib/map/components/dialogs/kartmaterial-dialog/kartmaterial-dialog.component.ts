import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import kommunData from "./kommuner.json";
import lanData from "./lan.json";
import {ConfigService, LaddaNedKartmaterialInfo} from "../../../../config/config.service";

@Component({
  selector: 'xp-kartmaterial-dialog',
  templateUrl: "./kartmaterial-dialog.component.html",
  styleUrls: ["./kartmaterial-dialog.component.scss"],
})
export class KartmaterialDialogComponent {
  lan;
  filter: string;
  laddaNedKartmaterial: LaddaNedKartmaterialInfo[];
  laddaNedKartmaterial_Exists: boolean[] = [];
  helaSverigeRaster: boolean[] = [];
  helaSverigeVector: boolean[] = [];
  raster: boolean[] = [];
  vector: boolean[] = [];
  counter: number = 0;

  constructor(private configService: ConfigService, public dialogRef: MatDialogRef<KartmaterialDialogComponent>) {
    this.lan = lanData.map(lan => {
      return {
        name: lan.name,
        kommuner: kommunData.filter(kommun => kommun.lansKod == lan.lansKod)
      }
    });
    this.setValuesFromConfigService();
  }

  close(): void {
    this.dialogRef.close();
  }

  toFileName(name: string) {
    if (name== "Håbo") {
      return "HAABO"
    }
    return name.replace(" ", "_").toLocaleUpperCase()
      .replace(/Å/g, "A")
      .replace(/Ä/g, "A")
      .replace(/Ö/g, "O")
  }

  list() {
    if (this.filter == null || this.filter == "") {
      return this.lan;
    } else {
      return this.lan.filter(l => l.name.toLocaleLowerCase().startsWith(this.filter.toLocaleLowerCase()));
    }
  }

  setValuesFromConfigService() {
    if (this.configService.config.app.rightBottomPanelElements != undefined && this.configService.config.app.rightBottomPanelElements.laddaNedKartmaterial != undefined) {
      this.laddaNedKartmaterial = this.configService.config.app.rightBottomPanelElements.laddaNedKartmaterial;
      this.laddaNedKartmaterial.forEach(i => this.checkElement(i));
    }
  }

  checkElement(element: LaddaNedKartmaterialInfo) {
    if (element.mapName != undefined &&
      element.title != undefined &&
      element.lanCatalogPath != undefined &&
      element.kommunCatalogPath != undefined) {
      this.laddaNedKartmaterial_Exists[this.counter] = true;
      if (element.raster != undefined) {
        this.raster[this.counter] = element.raster
      };
      if (element.vector != undefined) {
        this.vector[this.counter] = element.vector
      }
      if (element.helaSverigeZipFileRasterPath !== undefined && element.helaSverigeZipFileRasterPath !== "") {
        this.helaSverigeRaster[this.counter] = true
      }
      if (element.helaSverigeZipFileVectorPath !== undefined && element.helaSverigeZipFileVectorPath !== "") {
        this.helaSverigeVector[this.counter] = true
      }
    } else {
      this.laddaNedKartmaterial_Exists[this.counter] = false;
    }
    this.counter = this.counter + 1;
  }
}
