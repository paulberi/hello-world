import { Component, Input, OnChanges, OnInit, SimpleChanges } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { NisKalla, ProjektTyp, Version } from "../../../../../../../generated/markkoll-api";
import { XpErrorService } from "../../../../../../lib/error/error.service";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { uuid } from "../../../model/uuid";
import { DialogService } from "../../../services/dialog.service";
import { MkInstallningarService } from "../../../services/installningar.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkUserService } from "../../../services/user.service";
import { MkUploadFile } from "../upload-file/upload-file.presenter";

@Component({
  selector: "mk-versionimport",
  templateUrl: "./versionimport.container.html"
})
export class MkVersionImportContainerComponent implements OnInit, OnChanges {
  @Input() projektId: uuid;

  @Input() projektTyp: ProjektTyp;
  @Input() nisKalla: NisKalla;

  versioner: Version[] = [];

  isImporting = false;

  constructor(private projektService: ProjektService,
    private dialogService: DialogService,
    private translate: TranslocoService,
    private notificationService: XpNotificationService,
    private mkUserService: MkUserService,
    private installningarService: MkInstallningarService,
    private router: Router,
    private errorService: XpErrorService) { }

  async ngOnInit() {
    this.versioner = await this.projektService.getVersions(this.projektId).toPromise();

    const kundId = this.mkUserService.getMarkkollUser().kundId;
    this.installningarService.getNisKalla(kundId)
      .subscribe(nis => this.nisKalla = nis);
  }

  async ngOnChanges(changes: SimpleChanges) {
    if (changes.projektId) {
      this.ngOnInit();
    }
  }

  async onDeleteVersion(version: Version) {
    try {
      await this.projektService.deleteVersion(this.projektId, version.id).toPromise();
      this.notificationService.success(this.translate.translate("mk.redigeraProjekt.versionDeleted"));
      this.navigateToProjektsida();
    } catch (e) {
      this.errorService.notifyError(e);
    }
  }

  onImportVersion(versionFile: MkUploadFile) {
    if (this.versioner.length > 0) {
      this.dialogService.confirmProjektVersionDialog().subscribe(() => this.doImport(versionFile));
    } else {
      this.doImport(versionFile);
    }
  }

  async onRestoreVersion(version: Version) {
    try {
      await this.projektService.restoreVersion(this.projektId, version.id).toPromise();
      this.notificationService.success(this.translate.translate("mk.redigeraProjekt.versionRestored"));
      this.navigateToProjektsida();
    } catch (e) {
      this.errorService.notifyError(e);
    }
  }

  private navigateToProjektsida() {
    this.router.navigate(["projekt/", this.projektTyp, this.projektId, "avtal"])
      .then(() => {
        window.location.reload();
      });
  }

  private async doImport(versionFile: MkUploadFile) {
    try {
      this.isImporting = true;
      await this.projektService.updateVersion(this.projektId, versionFile.indataTyp,
        versionFile.files[0], versionFile.buffert).toPromise();

      this.notificationService.success(this.translate.translate("mk.redigeraProjekt.versionImported"));
      this.navigateToProjektsida();
    } catch (e) {
      this.errorService.notifyError(e);
    } finally {
      this.isImporting = false;
    }
  }
}
