import { Component, EventEmitter, Input, Output, Version } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { MkUploadFile } from "../upload-file/upload-file.presenter";

@Component({
  selector: "mk-versionimport-ui",
  templateUrl: "./versionimport.component.html",
  styleUrls: ["./versionimport.component.scss"],
  providers: []
})
export class MkVersionImportComponent {
  /** Lista med projektversioner */
  @Input() versioner: Version[] = [];

  /** Om import av ny version pågår */
  @Input() isImporting = false;

  /** Projekttyp */
  @Input() projektTyp: ProjektTyp;

  /** NIS-Källa */
  @Input() nisKalla: NisKalla;

  /** Event när användaren importerar en ny version */
  @Output() importVersion = new EventEmitter<MkUploadFile>();

  /** Event när användaren återställer en version */
  @Output() restoreVersion = new EventEmitter<Version>();

  /** Event när användaren importerar tar bort en version */
  @Output() deleteVersion = new EventEmitter<Version>();

  isVersionFileValid = false;

  private selectedVersionFile: MkUploadFile = null;

  constructor() { }

  onFilesChange(file: MkUploadFile) {
    this.selectedVersionFile = file;
  }

  onValidChange(isVersionFileValid: boolean) {
    this.isVersionFileValid = isVersionFileValid;
  }

  onNewVersion() {
    if (this.isVersionFileValid) {
      this.importVersion.emit(this.selectedVersionFile);
    }
  }
}
