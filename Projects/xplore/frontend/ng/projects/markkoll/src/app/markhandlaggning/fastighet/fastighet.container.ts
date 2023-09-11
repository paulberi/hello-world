import { Component, OnInit, Output, ViewChild } from "@angular/core";
import { AvtalService } from "../../services/avtal.service";
import { FastighetService } from "../../services/fastighet.service";
import { DialogService } from "../../services/dialog.service";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { TranslocoService } from "@ngneat/transloco";
import { MkFastighetComponent } from "./fastighet.component";
import { AgareService } from "../../services/agare.service";
import { MkAvtal } from "../../model/avtal";
import { DokumentService } from "../../services/dokument.service";
import { ActivatedRoute } from "@angular/router";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { MkRegisterenhetContainer } from "../registerenhet/registerenhet.container";
import { ProjektService } from "../../services/projekt.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";
import { XpErrorService } from "../../../../../lib/error/error.service";
import { MkProjektkartaService } from "../../services/projektkarta.service";

/**
 * Containerkomponent för fastighetsinformationen i avtalslistan
 */
@Component({
  selector: "mk-fastighet",
  templateUrl: "./fastighet.container.html"
})
export class MkFastighetContainerComponent extends MkRegisterenhetContainer implements OnInit {
  /** Event när samfälligheten tas bort. */
  @Output() fastighetRemove = this.registerenhetRemove;

  /** Event när samfälligheten ändras. */
  @Output() fastighetChange = this.registerenhetChange;

  @ViewChild(MkFastighetComponent) fastighetComponent: MkFastighetComponent;

  constructor(agareService: AgareService,
              avtalService: AvtalService,
              dialogService: DialogService,
              notificationService: XpNotificationService,
              translation: TranslocoService,
              fastighetService: FastighetService,
              dokumentService: DokumentService,
              varderingsprotokollService: ElnatVarderingsprotokollService,
              fiberVarderingsprotokollService: MkFiberVarderingsprotokollService,
              projektService: ProjektService,
              activatedRoute: ActivatedRoute,
              errorService: XpErrorService,
              mkProjektkartaService: MkProjektkartaService) {
    super(activatedRoute, agareService, avtalService, dialogService, dokumentService,
      fastighetService, notificationService, translation, projektService,
      varderingsprotokollService, fiberVarderingsprotokollService, errorService, mkProjektkartaService);
  }

  onGetAvtal(_avtal: MkAvtal) {}

  onOmbudCreated() {
    this.fastighetComponent.resetOmbudForm();
  }
}
