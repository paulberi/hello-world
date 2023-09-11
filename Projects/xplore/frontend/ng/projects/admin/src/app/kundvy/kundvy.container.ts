import { TemplateRef } from "@angular/core";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { Observable } from "rxjs";
import { Kund } from "../../../../../generated/kundconfig-api";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { XpPage } from "../../../../lib/ui/paginated-table/page";
import { XpUserService } from "../../../../lib/user/user.service";
import { KundService } from "../services/kund.service";
import { PageEvent } from "@angular/material/paginator";
import { CredentialsService } from "../services/credentials.service";

@Component({
  selector: "adm-kundvycontainer",
  templateUrl: "./kundvy.container.html"
})
export class AdmKundvyContainerComponent implements OnInit {
  kunder: XpPage<Kund>;
  userRoles$: Observable<string[]>;
  selectedIndex = null;

  pageIndex = 0;
  pageSize = 10;

  @ViewChild("deleteDialog") deleteDialog: TemplateRef<any>;

  constructor(
    private xpUserService: XpUserService,
    private kundService: KundService,
    private matDialog: MatDialog,
    private notificationService: XpNotificationService,
    private translate: TranslocoService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.userRoles$ = this.xpUserService.getUserRoles$();
    this.updatePage();
  }

  updatePage() {
    this.kundService.getKundPage(this.pageIndex, this.pageSize).subscribe(kunder => {
      this.kunder = kunder;
    });
  }

  onKundChange(kund: Kund) {
    this.kundService.editKund(kund).subscribe(() => {
      this.notificationService.success(this.translate.translate("adm.notifications.kundEdited"));
      this.updatePage();
    })
  }

  onKundDelete(kundId: string) {
    this.matDialog.open(this.deleteDialog, {autoFocus: false}).afterClosed().subscribe(res => {
      if (res) {
        this.kundService.deleteKund(kundId).subscribe(() => {
          this.notificationService.success(this.translate.translate("adm.notifications.kundDeleted"));
          this.selectedIndex = null;
          this.updatePage();
        });
      }
    });
  }

  pageChange(pageEvent: PageEvent) {
    this.pageIndex = pageEvent.pageIndex;
    this.pageSize = pageEvent.pageSize;
    this.selectedIndex = null;

    this.updatePage();
  }

  goToAddKund() {
    this.router.navigateByUrl("kund/skapa");
  }

  onResetGeofenceRulesClick() {
    this.kundService.resetGeofenceRules().subscribe(result => {
      this.notificationService.success(
        this.translate.translate("adm.notifications.geoRulesReset"));    
    });
  }
}
