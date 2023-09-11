import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import FileSaver from "file-saver";
import { Observable } from "rxjs";
import { Dokumentmall, DokumentTyp } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { OptionItem } from "../../../common/filter-option/filter-option.component";
import { uuid } from "../../../model/uuid";
import { DialogService } from "../../../services/dialog.service";
import { DokumentService } from "../../../services/dokument.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkProjektdokumentComponent, SaveProjektdokument } from "./projektdokument.component";

@Component({
  selector: "mk-projektdokument",
  templateUrl: "./projektdokument.container.html"
})
export class MkProjektdokumentContainerComponent implements OnInit {

  @Input() kundId: string;

  optionItems: OptionItem[];
  dokument: Dokumentmall[];

  @ViewChild(MkProjektdokumentComponent) projektdokumentComponent: MkProjektdokumentComponent;

  constructor(private projektService: ProjektService,
              private dokumentService: DokumentService,
              private translate: TranslocoService,
              private notificationService: XpNotificationService,
              private dialogService: DialogService) {}

  ngOnInit() {
    this.getOptionItems();
    this.getProjektdokumentInfo();
  }

  createDokumentmall(projektdokument: SaveProjektdokument) {
    this.projektService.createDokumentmall(this.kundId, projektdokument)
    .subscribe(() => {
      this.notificationService.success(this.translate.translate("mk.projektdokument.success"));
      this.getProjektdokumentInfo();
      this.projektdokumentComponent.resetDokumentStepper();
    });
  }

  updateDokumentmall(dokument: Dokumentmall) {
    this.projektService.updateDokumentmall(this.kundId, dokument).subscribe(res => {
      this.notificationService.success(this.translate.translate("mk.projektdokument.changeSelectedSuccessful"));
      this.getProjektdokumentInfo();
    });
  }

  deleteDokumentmall(dokument: Dokumentmall) {
    this.dialogService.confirmDeleteDokument(dokument.namn).subscribe(result => {
      if (result === true) {
        this.projektService.deleteDokument(this.kundId, dokument.id).subscribe(() => {
          this.notificationService.success(this.translate.translate("mk.projektdokument.deleteSuccessful"));
          this.getProjektdokumentInfo();
        });
      }
    });
  }

  get dokumenttyp(): string[] {
    return Object.values(DokumentTyp);
  }

  getProjektdokumentInfo(): void {
    this.projektService.getDokumentmallarInfo(this.kundId).subscribe(dokument => {
      this.dokument = dokument;
    });
  }

  getOptionItems() {
    const items: OptionItem[] = [];

    this.dokumenttyp.forEach(typ => {
      // Work around tills vi stödjer andra dokumenttyper
      const key = typ === "INFOBREV" ? "OTHER" : typ;
      const item: OptionItem = {
        value: typ,
        label: this.translate.translate(`mk.dokumenttyp.${key}`)
      };
      items.push(item);
    });
    this.optionItems = items;
  }

  onDokumentPrepare(file: File) {
    this.dokumentService.prepareDokumentmall(this.kundId, file).subscribe(res => {
      FileSaver.saveAs(res.blob, res.name);
      this.projektdokumentComponent.resetDokumentStepper();
    });
  }
}
