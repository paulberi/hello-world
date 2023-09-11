import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { flatMap, switchMap } from "rxjs/operators";
import { AvtalsjobbProgress, AvtalsjobbStatus, Avtalsstatus, Dokumentmall, InfobrevsjobbProgress, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { ActionTyp, SelectionTyp } from "../../../model/actions";
import { MkAvtalsAction } from "../../../model/avtalAction";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { uuid } from "../../../model/uuid";
import { AvtalsjobbService } from "../../../services/avtalsjobb.service";
import { DialogService } from "../../../services/dialog.service";
import { DokumentService } from "../../../services/dokument.service";
import { DokumentjobbService } from "../../../services/dokumentjobb.service";
import { FastighetService } from "../../../services/fastighet.service";
import { InfobrevsjobbService } from "../../../services/infobrevsjobb.service";
import { KundService } from "../../../services/kund.service";
import { ExecuteActionEvent } from "./avtal-actions.component";

export type EditingContentsEvent = { editingContents: boolean, selectionTyp?: SelectionTyp };
@Component({
  selector: "mk-avtal-actions",
  templateUrl: "./avtal-actions.container.html"
})
export class MkAvtalActionsContainer implements OnInit {
  @Input() avtalsAction: MkAvtalsAction = this.emptyAction();
  @Input() filter: MkAvtalsfilter;
  @Input() projektId: uuid;
  @Input() numOfAvtalSelected: number;
  @Input() numOfFastigheter: number;
  @Input() registerenhetIds = [];
  @Input() projektTyp: ProjektTyp;

  @Output() selectionStatusChange = new EventEmitter<void>();
  @Output() resetFilter = new EventEmitter<void>();
  @Output() editingContentsChange = new EventEmitter<EditingContentsEvent>();

  avtalsjobbProgress: AvtalsjobbProgress = {
    id: null,
    generated: null,
    total: null,
    status: null
  };
  infobrevsjobbProgress: InfobrevsjobbProgress = {
    id: null,
    generated: null,
    total: null,
    status: null
  };
  editingContents = false;

  private _dokumentmallar: Dokumentmall[] = [];

  constructor(private avtalsjobbService: AvtalsjobbService,
    private dialogService: DialogService,
    private dokumentService: DokumentService,
    private fastighetService: FastighetService,
    private infobrevsjobbService: InfobrevsjobbService,
    private kundService: KundService,
    private notificationService: XpNotificationService,
    private translation: TranslocoService) { }

  ngOnInit() {
    this.kundService.getDokumentmallar()
      .subscribe(dokument => this._dokumentmallar = dokument);

    this.avtalsjobbService.getProgress(this.projektId)
      .subscribe(progress => this.avtalsjobbProgress = progress);

    this.infobrevsjobbService.getProgress(this.projektId)
      .subscribe(progress => this.infobrevsjobbProgress = progress);
  }

  actionsDisabled(): boolean {
    switch (this.avtalsAction?.actionTyp) {
      case ActionTyp.INFOBREV:
        return this.infobrevsjobbProgress.status == AvtalsjobbStatus.INPROGRESS;
      case ActionTyp.MARKUPPLATELSEAVTAL:
        return this.avtalsjobbProgress.status == AvtalsjobbStatus.INPROGRESS;
      default:
        return false;
    }
  }

  submitButtonText(): string {
    const action = this.avtalsAction.actionTyp;

    switch (action) {
      case ActionTyp.HAGLOF_HMS:
        return this.translation.translate("mk.generateButton.HAGLOF_HMS");
      case ActionTyp.EDIT_CONTENTS:
        return this.editContentsText();
      case ActionTyp.MARKUPPLATELSEAVTAL:
        return this.generateDokumentJobbText(this.avtalsAction.actionTyp, this.avtalsjobbProgress,
          this.numOfAvtalSelected);
      case ActionTyp.INFOBREV:
        return this.generateDokumentJobbText(this.avtalsAction?.actionTyp, this.infobrevsjobbProgress,
          this.numOfAvtalSelected);
      case ActionTyp.STATUS:
      default:
        return this.translation.translate("xp.common.execute");
    }
  }

  get dokumentmallar(): Dokumentmall[] {
    return this._dokumentmallar;
  }

  jobbStatus(): string {
    switch (this.avtalsAction?.actionTyp) {
      case ActionTyp.MARKUPPLATELSEAVTAL:
        return this.avtalsjobbProgress?.status;
      case ActionTyp.INFOBREV:
        return this.infobrevsjobbProgress?.status;
      default:
        return AvtalsjobbStatus.NONE;
    }
  }

  onExecuteAction(event: ExecuteActionEvent) {
    switch (event.action.actionTyp) {
      case ActionTyp.HAGLOF_HMS:
        this.onHaglofHMS(event.filter);
        break;
      case ActionTyp.INFOBREV:
        this.onDokumentJobb(event, this.infobrevsjobbProgress, this.infobrevsjobbService);
        break;
      case ActionTyp.MARKUPPLATELSEAVTAL:
        this.onDokumentJobb(event, this.avtalsjobbProgress, this.avtalsjobbService);
        break;
      case ActionTyp.STATUS:
        this.onSelectionStatusChange(event.action.avtalsstatus, event.filter, event.action.selection);
        break;
      case ActionTyp.EDIT_CONTENTS:
        this.toggleEditingContents(event.action.selection);
        break;
      case ActionTyp.FORTECKNING:
        this.onGetForteckning(event);
        break;
      default:
        throw Error("Okänd ActionTyp: " + event.action.actionTyp);
    }
  }

  onGetForteckning(event: ExecuteActionEvent) {
    this.dokumentService.generateForteckning(this.projektId, event.action.dokumentmallId, event.filter)
        .subscribe();
  }

  onResetAction(actionTyp: ActionTyp) {
    switch (actionTyp) {
      case ActionTyp.MARKUPPLATELSEAVTAL:
        this.resetDokumentJobb(this.avtalsjobbProgress, this.avtalsjobbService);
        break;
      case ActionTyp.INFOBREV:
        this.resetDokumentJobb(this.infobrevsjobbProgress, this.infobrevsjobbService);
        break;
      default:
        this.resetFilter.emit();
        this.avtalsAction = this.emptyAction();
    }
  }

  resetButtonText() {
    const clear = this.translation.translate("xp.common.clear");
    const cancel = this.translation.translate("xp.common.cancel");

    switch (this.avtalsAction?.actionTyp) {
      case ActionTyp.INFOBREV:
        return this.infobrevsjobbProgress.status == AvtalsjobbStatus.INPROGRESS ? cancel : clear;
      case ActionTyp.MARKUPPLATELSEAVTAL:
        return this.avtalsjobbProgress.status == AvtalsjobbStatus.INPROGRESS ? cancel : clear;
      default:
        return clear;
    }
  }

  private capitalizeFirstLetter(text: string): string {
    return text.charAt(0).toUpperCase() + text.slice(1);
  }

  private emptyAction(): MkAvtalsAction {
    return {
      actionTyp: null,
      dokumentmallId: "",
      selection: null,
      avtalsstatus: null
    }
  }

  private generateDokumentJobbText(action: string, progress: AvtalsjobbProgress, numOfAvtalSelected: number): string {
    const typ = action ? this.translation.translate("mk.generateButton." + action) : "";
    switch (progress?.status) {
      case AvtalsjobbStatus.NONE:
        return this.translation.translate(
          "mk.generateButton.create", { numOfAvtal: numOfAvtalSelected, typ: typ }
        );
      case AvtalsjobbStatus.DONE:
        return this.translation.translate("mk.generateButton.download", { typ: typ });
      case AvtalsjobbStatus.ERROR:
        return this.translation.translate("mk.generateButton.error", { typ: typ });
      case AvtalsjobbStatus.INPROGRESS:
        return this.translation.translate("mk.generateButton.inProgress",
          { generated: progress.generated, total: progress.total, typ: this.capitalizeFirstLetter(typ) }
        );
      default:
        return this.translation.translate("mk.generateButton.create", { typ: typ });
    }
  }

  private editContentsText() {
    if (!this.editingContents) {
      return this.translation.translate("mk.generateButton.EDIT_CONTENTS");
    } else {
      return this.translation.translate("mk.generateButton.EDITING_CONTENTS");
    }
  }

  toggleEditingContents(selectionTyp: SelectionTyp) {
    this.editingContents = !this.editingContents;
    this.editingContentsChange.emit({ editingContents: this.editingContents, selectionTyp: selectionTyp });
  }

  private getStatus(status: AvtalsjobbStatus) {
    return status ? status : AvtalsjobbStatus.NONE;
  }

  private notifySuccessTranslate(messageTranslate: string, args?: any) {
    this.notificationService.success(this.translation.translate(messageTranslate, args));
  }

  private onHaglofHMS(selectedFilter: MkAvtalsfilter) {
    this.dokumentService
      .getVarderingSkogsmarkProjektXlsx(this.projektId, selectedFilter)
      .subscribe();
  }

  private onDokumentJobb(event: ExecuteActionEvent,
    progress: AvtalsjobbProgress,
    dokumentjobbService: DokumentjobbService) {

    switch (this.getStatus(event.jobbstatus)) {
      case AvtalsjobbStatus.NONE:
      case AvtalsjobbStatus.CANCELLED:
        let number = this.numOfAvtalSelected

        if (event.action.selection === SelectionTyp.SELECTION) {
          number = this.registerenhetIds.length
        }

        this.dialogService.confirmAvtalSelectionDialog(number).pipe(
          flatMap(() => dokumentjobbService.create(this.projektId, event.filter, event.action.dokumentmallId, 2000))
        ).subscribe(_progress => Object.assign(progress, _progress));
        break;
      case AvtalsjobbStatus.DONE:
        const jobbId = event.action.actionTyp == ActionTyp.MARKUPPLATELSEAVTAL ?
          this.avtalsjobbProgress.id : this.infobrevsjobbProgress.id;

        dokumentjobbService.getData(this.projektId, jobbId);
        dokumentjobbService.reset(this.projektId);
        progress.status = AvtalsjobbStatus.NONE;
        break;
      case AvtalsjobbStatus.ERROR:
        progress.status = AvtalsjobbStatus.NONE;
        this.avtalsjobbService.reset(this.projektId);
        break;
    }
  }

  private onSelectionStatusChange(avtalsstatus: Avtalsstatus, selectedFilter: MkAvtalsfilter, selectionTyp: SelectionTyp) {
    let number = this.numOfFastigheter

    if (selectionTyp === SelectionTyp.SELECTION) {
      number = this.registerenhetIds.length
    }

    this.dialogService.confirmAllAvtalstatusDialog(number, avtalsstatus)
      .pipe(switchMap(_ =>
        this.fastighetService.setFastighetStatusForSelection(this.projektId, avtalsstatus, selectedFilter)))
      .subscribe(numOfFastigheter => {
        this.selectionStatusChange.emit();
        this.notifySuccessTranslate("mk.fastighetslista.sattStatusNotifikation", { antal: numOfFastigheter });
      });
  }

  private resetDokumentJobb(progress: AvtalsjobbProgress, dokumentjobbService: DokumentjobbService) {
    if (progress.status == AvtalsjobbStatus.INPROGRESS) {
      this.dialogService.confirmCancelAvtalsjobbDialog()
        .pipe(switchMap(() => dokumentjobbService.cancel(this.projektId, progress.id)))
        .subscribe(() => {
          this.avtalsAction = this.emptyAction();
          progress.status = AvtalsjobbStatus.NONE;
        });
    }
    else {
      this.resetFilter.emit();
      this.avtalsAction = this.emptyAction();
      progress.status = AvtalsjobbStatus.NONE;
    }
  }
}
