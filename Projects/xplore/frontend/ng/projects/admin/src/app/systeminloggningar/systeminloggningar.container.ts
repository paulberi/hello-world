import { Component, Input, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { forkJoin, Observable } from "rxjs";
import { filter, flatMap } from "rxjs/operators";
import { FastighetsokAuth, MetriaMapsAuth, System } from "../../../../../generated/kundconfig-api";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { CredentialsService } from "../services/credentials.service";
import { AdmSysteminloggningarComponent, AdmCredentials, AdmCredentialsEvent, AdmResetCredentialsEvent } from "./systeminloggningar.component";

@Component({
  selector: "adm-systeminloggningar",
  templateUrl: "./systeminloggningar.container.html"
})
export class AdmSysteminloggningarContainer implements OnInit {
  @Input() kundId: string;

  @ViewChild(AdmSysteminloggningarComponent, { static: false })
  component: AdmSysteminloggningarComponent;

  indexSelected: number = null;

  auth: AdmCredentials[];

  @ViewChild("resetCredentialsDialog") resetCredentialsDialog: TemplateRef<any>;

  constructor(private credentialsService: CredentialsService,
    private notificationService: XpNotificationService,
    private translate: TranslocoService,
    private matDialog: MatDialog) { }

  ngOnInit() {
    forkJoin({
      metriaMaps: this.credentialsService.getMetriaMapsAuth(this.kundId),
      fastighetSok: this.credentialsService.getFastighetsokAuth(this.kundId)
    }).subscribe(obj => this.auth = [obj.fastighetSok, obj.metriaMaps]);
  }

  onIndexSelectedChange(index: number) {
    // Ge oss färska och nollställda formulär varje gång vi öppnar ett formulär
    if (index !== null) {
      this.component.resetForms();
    }

    this.indexSelected = index;
  }

  onAuthChange(event: AdmCredentialsEvent) {
    this.changeAuth(event.system, event.credentials)
      .subscribe(() => {
        this.auth = this.replaceAtIndex(this.auth, event.index, event.credentials);
        const system = this.translate.translate("adm.system." + event.system)
        this.notificationService.success(
          this.translate.translate("adm.notifications.credentialsSaved", { system: system }));
        this.onIndexSelectedChange(null);
      });
  }

  onAuthReset(event: AdmResetCredentialsEvent) {
    this.matDialog.open(this.resetCredentialsDialog, { autoFocus: false }).afterClosed().pipe(
      filter(confirm => confirm === true),
      flatMap(() => this.resetAuth(event.system, event.id)))
      .subscribe(() => {
        const newCredentials = this.resettedCredentials(event.system, event.id);
        this.notificationService.success(
          this.translate.translate("adm.notifications.credentialsDeleted", { system: event.system }));
        this.auth = this.replaceAtIndex(this.auth, event.index, newCredentials);

        if (event.index === this.indexSelected) {
          this.onIndexSelectedChange(null);
        }
      });
  }

  private replaceAtIndex<T>(array: T[], index: number, item: T): T[] {
    return array.map((c, i) => i === index ? item : c);
  }

  private changeAuth(system: System, credentials: AdmCredentials): Observable<void> {
    switch (system) {
      case System.FASTIGHETSOK:
        return this.credentialsService.editFastighetsokAuth(this.kundId, credentials as FastighetsokAuth);
      case System.METRIAMAPS:
        return this.credentialsService.editMetriaMapsAuth(this.kundId, credentials);
    }
  }

  private resetAuth(system: System, authId: string): Observable<void> {
    switch (system) {
      case System.FASTIGHETSOK:
        return this.credentialsService.resetFastighetsokAuth(authId, this.kundId);
      case System.METRIAMAPS:
        return this.credentialsService.resetMetriaMapsAuth(authId, this.kundId);
    }
  }

  private resettedCredentials(system: System, authId: string): AdmCredentials {
    switch (system) {
      case System.FASTIGHETSOK:
        const fsok: FastighetsokAuth = {
          id: authId,
          kundId: this.kundId,
          username: null,
          password: null,
          kundmarke: null
        };
        return fsok;
      case System.METRIAMAPS:
        const mmaps: MetriaMapsAuth = {
          id: authId,
          kundId: this.kundId,
          username: null,
          password: null
        };
        return mmaps;
    }
  }
}
