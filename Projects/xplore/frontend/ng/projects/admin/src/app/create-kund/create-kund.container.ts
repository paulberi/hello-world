import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { Observable } from "rxjs";
import { Kund, KundService } from "../../../../../generated/kundconfig-api";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { AdmCreateKundEvent } from "./create-kund.presenter";

@Component({
  selector: "adm-create-kundcontainer",
  templateUrl: "./create-kund.container.html"
})
export class AdmCreateKundContainerComponent implements OnInit {
  kunder$: Observable<Kund[]>;
  nyaKunder: Kund[] = [];

  constructor(
    private apiService: KundService,
    private notificationService: XpNotificationService,
    private translate: TranslocoService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getKunder$();
  }

  back() {
    this.router.navigateByUrl("/kund");
  }

  getKunder$() {
    this.kunder$ = this.apiService.getKunder();
  }

  onSubmit(kund: AdmCreateKundEvent) {
    if (kund.kund) {
      this.apiService.createKund(kund.kund).subscribe(newKund => {
        this.notificationService.success(this.translate.translate("adm.notifications.kundCreated"));
        this.getKunder$();
        if (kund.createMore) {
          this.nyaKunder.push(newKund);
        } else {
          this.router.navigateByUrl("/kund");
        }
      });
    }
  }
}
