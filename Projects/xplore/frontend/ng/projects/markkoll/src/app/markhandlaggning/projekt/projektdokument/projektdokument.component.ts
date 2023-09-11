import { AfterContentInit, AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { UntypedFormGroup, FormGroupDirective } from "@angular/forms";
import { MatStepper } from "@angular/material/stepper";
import { Dokumentmall, DokumentInfo, DokumentTyp } from "../../../../../../../generated/markkoll-api";
import { OptionItem } from "../../../common/filter-option/filter-option.component";
import { MkDokumentCreateFormPresenter } from "./dokument-create-form.presenter";
import { MkDokumentPrepareFormPresenter } from "./dokument-prepare-form.presenter";
import { MkDokumentRadioFormPresenter } from "./dokument-radio-form.presenter";

export class SaveProjektdokument {
  projektdokumentInfo: DokumentInfo;
  file: File;
}

enum StepperChoice {
  CREATE, PREPARE
}

@Component({
  providers: [
    MkDokumentRadioFormPresenter,
    MkDokumentCreateFormPresenter,
    MkDokumentPrepareFormPresenter
  ],
  selector: "mk-projektdokument-ui",
  templateUrl: "./projektdokument.component.html",
  styleUrls: ["./projektdokument.component.scss"]
})
export class MkProjektdokumentComponent implements OnInit {
  acceptedFileEndings = [".docx"];

  // Vilka dokumenttyper man ska kunna välja på när man skapar dokument
  @Input() dokumenttyper: OptionItem[];

  // Dokument att lista
  @Input() dokument: Dokumentmall[];

  // Information om dokumentet som ska skapas
  @Output() dokumentCreate = this.createFormPresenter.dokumentCreate;

  // Information om dokumentet som ska behandlas
  @Output() dokumentPrepare = this.prepareFormPresenter.dokumentPrepare;

  // En uppdaterad version av ett dokument
  @Output() dokumentChange = new EventEmitter<Dokumentmall>();

  // Dokumentet man vill ta bort
  @Output() dokumentDelete = new EventEmitter<Dokumentmall>();

  DokumentTyp = DokumentTyp;

  @ViewChild(MatStepper) stepper: MatStepper;
  @ViewChild(FormGroupDirective) private formGroupDirective: FormGroupDirective;
  private stepperChoice: StepperChoice = StepperChoice.CREATE;

  private selectedDokumentTyp: DokumentTyp = DokumentTyp.MARKUPPLATELSEAVTAL;

  constructor(private createFormPresenter: MkDokumentCreateFormPresenter,
              private prepareFormPresenter: MkDokumentPrepareFormPresenter,
              private radioFormPresenter: MkDokumentRadioFormPresenter) { }

  ngOnInit(): void {
    this.initializeAllForms();
  }

  get radioForm(): UntypedFormGroup {
    return this.radioFormPresenter.form;
  }

  get createForm(): UntypedFormGroup {
    return this.createFormPresenter.form;
  }

  get prepareForm(): UntypedFormGroup {
    return this.prepareFormPresenter.form;
  }

  resetDokumentStepper() {
    this.initializeAllForms();
    this.stepper.reset();
    this.formGroupDirective.resetForm();
  }

  createDokument() {
    this.createFormPresenter.createDokument();
  }

  prepareDokument() {
    this.prepareFormPresenter.prepareDokument();
  }

  getAcceptedFileEndings(): string[] {
    switch (this.selectedDokumentTyp) {
      case DokumentTyp.MARKUPPLATELSEAVTAL:
      case DokumentTyp.INFOBREV:
        return [".docx"];
      case DokumentTyp.FORTECKNING:
        return [".xlsx"];
      default:
        return [];
    }
  }

  goToCreate() {
    this.stepperChoice = StepperChoice.CREATE;
    this.stepper.next();
  }

  goToPrepare() {
    this.stepperChoice = StepperChoice.PREPARE;
    this.stepper.next();
  }

  onDokumenttypChange(dokumentTyp: DokumentTyp) {
    this.selectedDokumentTyp = dokumentTyp;
  }

  onStepChange() {
    if (this.stepper.selectedIndex == 0) {
      this.radioFormPresenter.initializeForm();
    }
  }

  isCreatingDokument(): boolean {
    return this.stepperChoice == StepperChoice.CREATE;
  }

  isPreparingDokument(): boolean {
    return this.stepperChoice == StepperChoice.PREPARE;
  }

  filterDokumentmallar(dokumenttyp: DokumentTyp): Dokumentmall[] {
    return this.dokument?.filter(d => d.dokumenttyp === dokumenttyp) || [];
  }

  private initializeAllForms() {
    this.radioFormPresenter.initializeForm();
    this.createFormPresenter.initializeForm(this.selectedDokumentTyp);
    this.prepareFormPresenter.initializeForm();
  }
}
