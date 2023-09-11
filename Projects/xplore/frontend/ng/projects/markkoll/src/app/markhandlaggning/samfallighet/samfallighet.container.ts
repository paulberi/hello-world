import { Component, OnInit, Output, ViewChild } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { MkAvtal } from "../../model/avtal";
import { AgareService } from "../../services/agare.service";
import { AvtalService } from "../../services/avtal.service";
import { DialogService } from "../../services/dialog.service";
import { DokumentService } from "../../services/dokument.service";
import { FastighetService } from "../../services/fastighet.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { MkSamfallighetComponent } from "./samfallighet.component";
import { ActivatedRoute } from "@angular/router";
import { MkRegisterenhetContainer } from "../registerenhet/registerenhet.container";
import { ProjektService } from "../../services/projekt.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";
import { XpErrorService } from "../../../../../lib/error/error.service";
import { MkProjektkartaService } from "../../services/projektkarta.service";

@Component({
  selector: "mk-samfallighet",
  templateUrl: "./samfallighet.container.html"
})
export class MkSamfallighetContainerComponent extends MkRegisterenhetContainer implements OnInit {
  /** Event när samfälligheten tas bort. */
  @Output() samfallighetRemove = this.registerenhetRemove;

  /** Event när samfälligheten ändras. */
  @Output() samfallighetChange = this.registerenhetChange;

  @ViewChild(MkSamfallighetComponent) samfallighetComponent: MkSamfallighetComponent;

  isMittlinjeRedovisad = false;

  constructor(avtalService: AvtalService,
              agareService: AgareService,
              fastighetService: FastighetService,
              dialogService: DialogService,
              notificationService: XpNotificationService,
              translation: TranslocoService,
              dokumentService: DokumentService,
              projektService: ProjektService,
              varderingsprotokollService: ElnatVarderingsprotokollService,
              fiberVarderingsprotokollService: MkFiberVarderingsprotokollService,
              activatedRoute: ActivatedRoute,
              errorService: XpErrorService,
              mkProjektkartaService: MkProjektkartaService) {
      super(activatedRoute, agareService, avtalService, dialogService, dokumentService,
        fastighetService, notificationService, translation, projektService,
        varderingsprotokollService, fiberVarderingsprotokollService, errorService, mkProjektkartaService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  onOmbudCreated() {
    this.samfallighetComponent.resetOmbudForm();
  }
}
