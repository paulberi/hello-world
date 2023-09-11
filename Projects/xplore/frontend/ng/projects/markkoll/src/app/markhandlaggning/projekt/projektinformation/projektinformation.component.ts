import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from "@angular/core";
import {MkUploadFile} from "../upload-file/upload-file.presenter";
import {XpMessageSeverity} from "../../../../../../lib/ui/feedback/message/message.component";
import {Avtalsinstallningar, Beredare, ProjektInfo} from "../../../../../../../generated/markkoll-api";
import {State} from "../../../model/loadState";
import { MkProjektinformationPresenter } from "./projektinformation.presenter";
import { MkUserService } from "../../../services/user.service";
import { Projekt } from "../../../model/projekt";
import { uuid } from "../../../model/uuid";
import { UserRoleEntry, UserOption } from "../user-roles/user-roles.component";
import { ProjektPanelService } from "../../../services/projektpanel.service";

@Component({
  selector: "mk-projektinformation-ui",
  templateUrl: "./projektinformation.component.html",
  styleUrls: ["./projektinformation.component.scss"],
  providers: [MkProjektinformationPresenter]
})
export class MkProjektinformationComponent implements OnInit, OnChanges {

  isUploadFileValid = false;
  selectedUploadFile: MkUploadFile;
  isImporting = false;

  information = XpMessageSeverity.Information;

  @Input() beredare: Beredare;

  @Input() projekt: Projekt;

  @Input() ledningsagareOptions: string[] = [];

  @Input() isDeletingProjekt = false;

  @Input() projektState: State = State.Loading;

  @Input() showVersionMessage = false;

  @Input() projektRoleEntries: UserRoleEntry[];

  @Input() userOptions: UserOption[];

  @Input() avtalsinstallningar: Avtalsinstallningar;

  @Output() updateProjekt = this.presenter.updateProjekt;

  @Output() deleteProjekt = new EventEmitter<uuid>();

  State = State;

  readonly acceptedFileEndings = [".xlsx"];

  constructor(private presenter: MkProjektinformationPresenter,private projektpanelService: ProjektPanelService) {}

  ngOnInit() {
    this.presenter.initializeForm(this.projekt, this.projektRoleEntries, this.beredare, this.avtalsinstallningar);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.projektRoleEntries || changes.projekt) {
      this.ngOnInit();
    }
  }

  markAsPristine() {
    this.presenter.markAsPristine();
  }

  onAction() {
    this.showVersionMessage = !this.showVersionMessage;
  }

  submit() {
    this.presenter.onSubmit();
  }

  onDeleteProjekt() {
    this.deleteProjekt.emit(this.projekt.projektInfo.id);
  }

  onProjektInfoChange(projektInfo: ProjektInfo) {
    this.projekt.projektInfo = projektInfo;
  }

  get form() {
    return this.presenter.form;
  }

  hasUnsavedChanges(): boolean {
    return this.presenter.canSave();
  }

  isExpanded(panel: string) {
    return this.projektpanelService.isExpanded(panel);
  }

  toggleExpanded(panel: string) {
    this.projektpanelService.toggleExpanded(panel);
  }
}

