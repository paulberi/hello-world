import { Component, Input, OnInit } from "@angular/core";
import { Abonnemang, AbonnemangTyp, Produkt } from "../../../../../generated/kundconfig-api";
import { AbonnemangService } from "../services/abonnemang.service";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { TranslocoService } from "@ngneat/transloco";

@Component({
  selector: "adm-abonnemang",
  templateUrl: "./abonnemang.container.html"
})
export class AdmAbonnemangContainer implements OnInit {
  @Input() abonnemang: Abonnemang[];
  @Input() kundId: string;
  showForm = false;

  readonly produkter: string[] = Object.values(Produkt);
  readonly abonnemangTyper: string[] = Object.values(AbonnemangTyp);

  constructor(private abonnemangService: AbonnemangService,
              private translate: TranslocoService,
              private notificationService: XpNotificationService) {}

  ngOnInit() {
  }

  onAbonnemangAdd(abonnemang: Abonnemang) {
    this.abonnemangService.addAbonnemang(this.kundId, abonnemang).subscribe(a => {
      this.abonnemang = [...this.abonnemang, a];
      this.notificationService.success(this.translate.translate("adm.notifications.abonnemangAdded"));
      this.showForm = false;
    });
  }

  onAbonnemangDelete(abonnemang: Abonnemang) {
    this.abonnemangService.deleteAbonnemang(abonnemang.id).subscribe(() => {
      this.abonnemang = this.abonnemang.filter(a => a.id !== abonnemang.id);
      this.notificationService.success(this.translate.translate("adm.notifications.abonnemangDeleted"));
    });
  }
}
