import { Component, Input, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { forkJoin, Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { FiberVarderingConfig, FiberVarderingConfigNamnAgare } from "../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { KundService } from "../../services/kund.service";
import { FiberVarderingConfigCreateData } from "./ersattning-fiber.presenter";

@Component({
  selector: "mk-ersattning-fiber",
  templateUrl: "./ersattning-fiber.container.html",
})
export class MkErsattningFiberContainerComponent implements OnInit {
  kundDefaultConfig: FiberVarderingConfigNamnAgare;
  kundAvtalspartConfig: FiberVarderingConfigNamnAgare[];
  @Input() activeKund;
  @ViewChild("deleteDialog") deleteDialog: TemplateRef<any>;

  constructor(private kundService: KundService,
              private notificationService: XpNotificationService,
              private matDialog: MatDialog,
              private transloco: TranslocoService) { }

  ngOnInit(): void {
    const kundDefault = this.kundService.getFiberVarderingConfigForKund(this.activeKund);
    const kundAvtalspart = this.kundService.getAllFiberVarderingConfigsForKund(this.activeKund);
    forkJoin([kundDefault, kundAvtalspart]).subscribe(result => {
      this.kundDefaultConfig = {fiberVarderingConfig: result[0], namn: null, personnummer: null};
      this.kundAvtalspartConfig = result[1];
    });
  }

  updateKundAvtalspartConfig() {
    this.kundService.getAllFiberVarderingConfigsForKund(this.activeKund).subscribe(res => {
      this.kundAvtalspartConfig = res;
    });
  }

  onDefaultFiberVarderingConfigChange(fiberConfig: FiberVarderingConfig) {
    this.kundService.updateFiberVarderingConfigForKund(this.activeKund, fiberConfig).subscribe(res => {
      this.kundDefaultConfig = {fiberVarderingConfig: res, namn: null, personnummer: null};
      this.notificationService.success(this.transloco.translate("mk.admin.ersattningFiber.updateSuccess"));
    });
  }

  onFiberVarderingConfigCreate(data: FiberVarderingConfigCreateData) {
    this.kundService.createFiberVarderingConfig(this.activeKund, data)
      .subscribe(() => {
        this.updateKundAvtalspartConfig();
        this.notificationService.success(this.transloco.translate("mk.admin.ersattningFiber.createSuccess"));
    });
  }
  
  onFiberVarderingConfigChange(fiberConfig: FiberVarderingConfigNamnAgare) {
    this.kundService.updateFiberVarderingConfig(this.activeKund, fiberConfig)
      .subscribe(() => {
        this.updateKundAvtalspartConfig();
        this.notificationService.success(this.transloco.translate("mk.admin.ersattningFiber.updateSuccess"));
      });

  }

  onFiberVarderingConfigDelete(fiberConfig: FiberVarderingConfigNamnAgare) {
    this.matDialog.open(this.deleteDialog).afterClosed().subscribe(res => {
      if (res) {
        this.kundService.deleteFiberVarderingConfig(this.activeKund, fiberConfig.fiberVarderingConfig.id)
          .subscribe(() => {
            this.updateKundAvtalspartConfig();
            this.notificationService.success(this.transloco.translate("mk.admin.ersattningFiber.deleteSuccess"));
          });
      }
    });
  }
}
