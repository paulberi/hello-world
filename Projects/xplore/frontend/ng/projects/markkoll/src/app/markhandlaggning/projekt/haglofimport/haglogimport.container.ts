import { Component, Input } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { HaglofImportVarningar } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { uuid } from "../../../model/uuid";
import { HaglofService } from "../../../services/haglof.service";

@Component({
  selector: "mk-haglofimport",
  templateUrl: "./haglofimport.container.html"
})
export class MkHaglofImportContainer {
  @Input() projektId: uuid;

  warnings: HaglofImportVarningar = null;

  constructor(private haglofService: HaglofService,
              private notificationService: XpNotificationService,
              private translate: TranslocoService) {}

  onImport(file: File) {
    this.haglofService.importJson(this.projektId, file).subscribe(warnings => {
      this.notificationService.success(this.translate.translate("mk.projektimporter.haglofSuccess"));
      this.warnings = warnings;
    });
  }

  onCloseWarnings() {
    this.warnings = null;
  }
}
