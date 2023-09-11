import {
  Component, EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from "@angular/core";
import { MkAvtalActionsPresenter } from "./avtal-actions.presenter";
import { MkAvtalsAction } from "../../../model/avtalAction";
import { XpSelectOption } from "../../../../../../lib/ui/model/selectOption";
import { AvtalsjobbStatus, Avtalsstatus, Dokumentmall, DokumentTyp, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { TranslocoService } from "@ngneat/transloco";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { ActionTyp, SelectionTyp } from "../../../model/actions";

export interface ExecuteActionEvent {
  filter: MkAvtalsfilter;
  jobbstatus?: AvtalsjobbStatus;
  action?: MkAvtalsAction;
}

@Component({
  providers: [MkAvtalActionsPresenter],
  selector: "mk-avtal-actions-ui",
  templateUrl: "./avtal-actions.component.html",
  styleUrls: ["./avtal-actions.component.scss"]
})
export class MkAvtalActionsComponent implements OnChanges, OnInit {

  @Input() registerenhetIds = [];
  @Input() avtalsAction: MkAvtalsAction = {
    actionTyp: null,
    selection: null,
    dokumentmallId: "",
    avtalsstatus: null
  };

  actionOptions: XpSelectOption[];
  statusOptions: XpSelectOption[];
  templateOptions: XpSelectOption[] = [];
  selectionOptions: XpSelectOption[] = [
    {
      label: this.translate.translate("mk.selectionOptions.all"),
      value: SelectionTyp.ALL
    },
    {
      label: this.translate.translate("mk.selectionOptions.filtered"),
      value: SelectionTyp.FILTERED
    },
    {
      label: this.translate.translate("mk.selectionOptions.selection") + "(" + this.registerenhetIds.length + ")",
      value: SelectionTyp.SELECTION,
    }
  ];;

  @Input() filter: MkAvtalsfilter;
  @Input() dokumentmallar: Dokumentmall[] = [];
  @Input() jobbStatus: AvtalsjobbStatus = AvtalsjobbStatus.NONE;
  @Input() submitButtonText = this.translate.translate("mk.generateButton.defaultText");
  @Input() resetButtonText = this.translate.translate("xp.common.clear");
  @Input() actionsDisabled = false;
  @Input() projektTyp: ProjektTyp;

  @Output() avtalsActionChange = new EventEmitter<MkAvtalsAction>();
  @Output() executeAction = new EventEmitter<ExecuteActionEvent>();
  @Output() resetAction = new EventEmitter<ActionTyp>();

  showTemplateSelection = false;
  showStatusSelection = false;

  constructor(private presenter: MkAvtalActionsPresenter, private translate: TranslocoService) {
    this.presenter.initializeForm(this.avtalsAction);

    this.presenter.change.subscribe(change => {
      const oldAction = this.avtalsAction?.actionTyp;

      this.avtalsAction = change;

      this.avtalsActionChange.emit(this.avtalsAction);

      if (change.actionTyp !== oldAction) {
        this.setVisibleSelections(change);

        switch (change.actionTyp) {
          case ActionTyp.MARKUPPLATELSEAVTAL:
          case ActionTyp.INFOBREV:
          case ActionTyp.FORTECKNING:
            this.populateTemplateOptions();

            const defaultTemplate = this.templateOptions.find(option => option.isSelected);
            if (!this.avtalsAction.dokumentmallId && defaultTemplate) {
              this.form.controls["dokumentmallId"].setValue(defaultTemplate.value);
            }

            break;
          case ActionTyp.STATUS:
          case ActionTyp.HAGLOF_HMS:
          case ActionTyp.EDIT_CONTENTS:
            break;
          default:
            console.log("Unknown Action: ", change.actionTyp);
        }
      }
    });
  }

  private setVisibleSelections(action: MkAvtalsAction) {
    this.showStatusSelection = false;
    this.showTemplateSelection = false;

    switch (action.actionTyp) {
      case ActionTyp.MARKUPPLATELSEAVTAL:
      case ActionTyp.INFOBREV:
      case ActionTyp.FORTECKNING:
        this.showTemplateSelection = true;
        break;
      case ActionTyp.STATUS:
        this.showStatusSelection = true;
        break;
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.avtalsAction) {
      this.presenter.initializeForm(this.avtalsAction);
      this.setVisibleSelections(this.avtalsAction);
    }

    if (changes.actionsDisabled) {
      this.presenter.setDisabled(this.actionsDisabled);
    }

    if (changes.filter) {
      if (this.filter?.status || this.filter?.search) {
        this.form.controls["selection"].setValue(SelectionTyp.FILTERED);
      }
    }
    if (changes.registerenhetIds) {
      this.updateSelectionOption();
    }

    if (changes.dokumentmallar) {
      if (this.dokumentmallar !== null) {
        this.createActionOptions();
      }
    }
  }

  updateSelectionOption() {
    this.selectionOptions[2].isDisabled = (this.registerenhetIds.length === 0)
    this.selectionOptions[2].label = this.translate.translate("mk.selectionOptions.selection") + " (" + this.registerenhetIds.length + "st)";
  }

  createActionOptions() {
    const options = [];
    options.push(this.createActionOption(ActionTyp.MARKUPPLATELSEAVTAL));
    options.push(this.createActionOption(ActionTyp.INFOBREV));
    options.push(
        {
            label: this.translate.translate("mk.avtalActions.forteckningLabel"),
            value: ActionTyp.FORTECKNING
        }
    )
    options.push(
      {
        label: this.translate.translate("mk.avtalActions.statusOptionLabel"),
        value: ActionTyp.STATUS
      }
    );
    options.push(
      {
        label: this.translate.translate("mk.avtalActions.haglofHMSOptionLabel"),
        value: ActionTyp.HAGLOF_HMS
      }
    );
    if (this.projektTyp === "LOKALNAT" || this.projektTyp === "REGIONNAT") {
      options.push(
        {
          label: this.translate.translate("mk.avtalActions.editContents"),
          value: ActionTyp.EDIT_CONTENTS,
        }
      );
    }

    this.actionOptions = options;
  }

  get form() {
    return this.presenter.form;
  }

  reset() {
    this.resetAction.emit(this.avtalsAction.actionTyp);
  }

  showButtons(): boolean {
    return !!this.avtalsAction?.actionTyp;
  }

  submit() {
    this.executeAction.emit({
      filter: this.getSelection(),
      jobbstatus: this.jobbStatus,
      action: this.avtalsAction
    });
  }

  ngOnInit(): void {
    this.createActionOptions();
    this.populateStatus();
  }

  private createActionOption(actionTyp: ActionTyp): XpSelectOption {
    const disabled = this.dokumentmallar?.filter(d => d.dokumenttyp === actionTyp).length <= 0;
    // Work around tills vi stödjer fler dokumenttyper
    const key = actionTyp === DokumentTyp.INFOBREV ? "OTHER" : actionTyp;
    const dokumentTypeLabel = this.translate.translate(`mk.dokumenttyp.${key}`);
    return {
      label: this.translate.translate("mk.avtalActions.createDokumentLabel", { dokumentTyp: dokumentTypeLabel }),
      value: actionTyp,
      isDisabled: disabled
    };
  }

  private getSelection(): MkAvtalsfilter {
    if (this.avtalsAction?.selection === SelectionTyp.ALL) {
      return { status: null, search: null, registerenhetsIds: null };
    } else if (this.avtalsAction?.selection === SelectionTyp.SELECTION) {
      return { status: null, search: null, registerenhetsIds: this.registerenhetIds };
    }

    return { ...this.filter, registerenhetsIds: null };
  }

  populateTemplateOptions() {
    if (this.avtalsAction?.actionTyp !== ActionTyp.STATUS) {
      this.templateOptions = this.dokumentmallar.filter(d => d.dokumenttyp === this.avtalsAction.actionTyp).map(d =>
        ({ label: d.namn, value: d.id, isSelected: d.selected } as XpSelectOption));
    }
  }

  populateStatus() {
    this.statusOptions = Object.values(Avtalsstatus).map(value => {
      return {
        value: value,
        label: this.translate.translate("mk.avtal." + value)
      };
    });
  }

  isResetDisabled(): boolean {
    return false;
  }

  isSubmitDisabled(): boolean {
    switch (this.avtalsAction?.actionTyp) {
      case ActionTyp.STATUS:
        return this.avtalsAction.selection?.length === 0 || this.avtalsAction.avtalsstatus?.length === 0;
      case ActionTyp.INFOBREV:
      case ActionTyp.MARKUPPLATELSEAVTAL:
        return this.jobbStatus === AvtalsjobbStatus.INPROGRESS ||
          this.avtalsAction.selection?.length === 0 || this.avtalsAction.dokumentmallId?.length === 0;
      case ActionTyp.HAGLOF_HMS:
        return this.avtalsAction.selection?.length === 0;
      case ActionTyp.FORTECKNING:
        return this.avtalsAction.selection?.length === 0 || this.avtalsAction.avtalsstatus?.length === 0;
      case ActionTyp.EDIT_CONTENTS:
        return this.avtalsAction.selection?.length === 0;
      default:
        return true;
    }
  }
}
