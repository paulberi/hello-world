import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";
import {LayerService} from "../../../../map-core/layer.service";
import {MapService} from "../../../../map-core/map.service";
import JSURL from "jsurl/lib/jsurl.js";

@Component({
  selector: "xp-share-dialog",
  templateUrl: "./share-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./share-dialog.component.scss"],
})
export class ShareDialogComponent {
  link: string;

  constructor(public dialogRef: MatDialogRef<ShareDialogComponent>,
              private layerService: LayerService, private mapService: MapService) {
    this.link = this.generateUrl();
  }

  copyLink(element): void {
    element.select();
    document.execCommand("copy");
  }

  close(): void {
    this.dialogRef.close();
  }

  generateUrl(): string {
    let url = window.location.protocol + "//" + window.location.host;

    const location = this.mapService.getLocation();
    url += `/?x=${location.x}&y=${location.y}&zoom=${location.zoom}`;

    // Skapa ett objekt med grupp som nyckel och en array med lagren
    // som visas (aktiva) för varje grupp. Det blir ett kompakt objekt som
    // läggs in som parameter i den genererade URLen

    url = url + "&lay=" + this.layerService.getActiveLayersConfigurationAsJsUrl();
    return url;
  }
}
