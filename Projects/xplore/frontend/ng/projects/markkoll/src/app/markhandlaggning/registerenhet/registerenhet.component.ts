import { Directive, EventEmitter, Input, Output } from "@angular/core";
import { MkAgare } from "../../model/agare";
import { MkAvtal } from "../../model/avtal";
import { Avtalsstatus, FiberVarderingsprotokoll, ProjektTyp, TillvaratagandeTyp, ElnatVarderingsprotokoll, FiberProjekt, ElnatProjekt, ElnatBilaga } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { VersionMessage } from "../fastighet/fastighet.component";
import { Projekt } from "../../model/projekt";

@Directive({ selector: "registerenhet-component" })
export abstract class RegisterenhetComponent {

  /** Projekt-ID */
  @Input() projektId: uuid;

  /** Fastighet-ID */
  @Input() fastighetId: uuid;

  /** Avtalsinformation. */
  @Input() avtal: MkAvtal;

  /** Värderingsprotokoll. */
  @Input() vp: ElnatVarderingsprotokoll;

  /** Värderingsprotokoll. */
  @Input() fiberVp: FiberVarderingsprotokoll;

  /** Meddelande för att bekräfta versionsändring. */
  @Input() versionMessage: VersionMessage;

  /** Index på nuvarande tab. Giltiga val antingen avtal (0) eller loggbok (1). */
  @Input() tabIndex = null;

  /** Om avtal genereras. */
  @Input() isGeneratingAvtal = false;

  /** Om anteckningar sparas. */
  @Input() isSavingAnteckningar = false;

  /** Om intrång sparas. */
  @Input() isSavingIntrang = false;

  /** Projekt. */
  @Input() projekt: Projekt;

  /** Event när anteckningar sparas. */
  @Output() anteckningarChange = new EventEmitter<string>();

  /** Event när användaren ändrar status för hela avtalet. */
  @Output() avtalStatusChange = new EventEmitter<Avtalsstatus>();

  /** Event när versionändring bekräftas. */
  @Output() confirmVersionChange = new EventEmitter<void>();

  /** Event när användaren lägger till ett nytt ombud. */
  @Output() ombudCreate = new EventEmitter<MkAgare>();

  /** Event när användaren på Skapa avtal. */
  @Output() skapaAvtalClick = new EventEmitter<void>();

  /** Event när användaren klickar på "Exportera"-knapp */
  @Output() exportXlsxClick = new EventEmitter();

  /** Event när användaren sparar ombud. */
  @Output() ombudChange = new EventEmitter<MkAgare>();

  /** Event när användaren tar bort ett ombud. */
  @Output() ombudDelete = new EventEmitter<MkAgare>();

  /** Event när användaren sätter "Signera avtal" för alla markägare. */
  @Output() signAvtalCheckAllChange = new EventEmitter<boolean>();

  ombudFormVisible = false;

  abstract onResetForm();

  abstract isAvtalsstatusEnabled(): boolean;

  abstract isSkapaAvtalEnabled(): boolean;

  hasOmbud(): boolean {
    return this.ombud.length > 0;
  }

  hasMultipleAgare(): boolean {
    return this.allAgare().length > 1;
  }

  hasMultipleLagfarnaAgare(): boolean {
    return this.lagfarnaAgare?.length > 1;
  }

  allAgareChecked(): boolean {
    return this.allAgare().every(ag => ag.inkluderaIAvtal);
  }

  someAgareChecked(): boolean {
    return !this.noAgareChecked() && !this.allAgareChecked();
  }

  noAgareChecked(): boolean {
    return this.allAgare().every(ag => !ag.inkluderaIAvtal);
  }

  onSkapaAvtal() {
    if (!this.isGeneratingAvtal) {
      this.skapaAvtalClick.emit();
    }
  }

  createOmbud(ombud: MkAgare) {
    this.ombudCreate.emit(ombud);
    this.ombudFormVisible = false;
  }

  cancelCreateOmbud() {
    this.ombudFormVisible = false;
    this.onResetForm();
  }

  resetOmbudForm() {
    this.onResetForm();
  }

  andelIsFelaktig() {
    if (this.allAgare().length === 0
      || this.allAgare().length === 1 && this.lagfarnaAgare[0]?.namn === "Ägare saknas") { // Finns det inga ägare så skapar vi upp en fake med namnet 'Ägare saknas'
      return false;
    }
    let andel = 0;
    this.lagfarnaAgare.filter(agare => agare.inkluderaIAvtal).forEach(agare => andel += (!agare.andel ? 0 : this.calculateFraction(agare.andel)));
    this.tomtrattsinnehavare.filter(agare => agare.inkluderaIAvtal).forEach(agare => andel += (!agare.andel ? 0 : this.calculateFraction(agare.andel)));
    this.ombud.filter(agare => agare.inkluderaIAvtal).forEach(agare => andel += (!agare.andel ? 0 : this.calculateFraction(agare.andel)));
    return Math.fround(andel) !== 1;
  }

  calculateFraction(fraction): number {
    var split = fraction.split('/');
    if (split[1] <= 0) {
      return 0;
    }
    return (split[0] / split[1]);
  }

  get avtalsstatusOptions(): Avtalsstatus[] {
    return Object.values(Avtalsstatus);
  }

  get ombud() {
    return this.avtal?.ombud || [];
  }

  get lagfarnaAgare(): MkAgare[] {
    return this.avtal?.lagfarnaAgare || [];
  }

  get tomtrattsinnehavare(): MkAgare[] {
    return this.avtal?.tomtrattsinnehavare || [];
  }

  get projektTyp() {
    return this.projekt?.projektInfo.projektTyp;
  }

  get showVarderingsprotokoll(): boolean {
    if (this.isFiberProjekt) {
      const fiberProjekt = this.projekt as FiberProjekt;
      return fiberProjekt.fiberInfo.varderingsprotokoll;
    } else {
      return true;
    }
  }

  get isFiberProjekt(): boolean {
    return this.projektTyp === ProjektTyp.FIBER;
  }

  get uppdragsnummer(): string {
    return this.projekt.projektInfo.uppdragsnummer;
  }

  private allAgare(): MkAgare[] {
    return [...this.lagfarnaAgare, ...this.tomtrattsinnehavare, ...this.ombud];
  }
}
