import { Component, EventEmitter, Input, Output, QueryList, ViewChildren } from "@angular/core";
import { FastighetsokAuth, MetriaMapsAuth, System } from "../../../../../generated/kundconfig-api";
import { AdmMetriaMapsAuthFormComponent } from "../metria-maps-auth-form/metria-maps-auth-form.component";
import { AdmFastighetsokAuthFormComponent } from "../fastighetsok-auth-form/fastighetsok-auth-form.component";

export type AdmCredentials = MetriaMapsAuth | FastighetsokAuth;

export interface AdmCredentialsEvent {
  system: System;
  credentials: AdmCredentials;
  index: number;
}

export interface AdmResetCredentialsEvent {
  system: System;
  id: string;
  index: number;
}

/**
 * En tabell med redigerbara systeminloggningar.
 */
@Component({
  selector: "adm-systeminloggningar-ui",
  templateUrl: "./systeminloggningar.component.html",
  styleUrls: ["./systeminloggningar.component.scss"]
})
export class AdmSysteminloggningarComponent {
  @Input() auth: AdmCredentials[];

  /** Index för den för nuvarande valda systeminloggningen */
  @Input() indexSelected: number = null;

  /** Event som emittas när autentiseringsuppgifter för MetriaMaps uppdateras */
  @Output() metriaMapsAuthChange = new EventEmitter<AdmCredentialsEvent>();

  /** Event som emittas när autentiseringsuppgifter för FastighetSok uppdateras */
  @Output() fastighetsokAuthChange = new EventEmitter<AdmCredentialsEvent>();

  @Output() authReset = new EventEmitter<AdmResetCredentialsEvent>();

  /** Event som emittas när valt index ändras */
  @Output() indexSelectedChange = new EventEmitter<number>();

  @ViewChildren(AdmMetriaMapsAuthFormComponent)
  private formsMMaps: QueryList<AdmMetriaMapsAuthFormComponent>;

  @ViewChildren(AdmFastighetsokAuthFormComponent)
  private formsFsok: QueryList<AdmFastighetsokAuthFormComponent>;
  
  readonly columns = ["edit", "system"];

  isSelected(index: number): boolean {
    return index === this.indexSelected;
  }

  toggleSelected(index: number) {
    this.indexSelectedChange.emit(this.indexSelected === index ? null : index);
  }

  hasCredentials(credentials: AdmCredentials) {
    return credentials.username && credentials.password;
  }

  onFormCancel() {
    this.indexSelectedChange.emit(null);
  }

  resetForms() {
    this.formsMMaps.forEach(f => f.initializeForm());
    this.formsFsok.forEach(f => f.initializeForm());
  }

  /* Obs: Vi antar att MMaps eller Fsök är de system som är tillgängliga. Annars kommer inte den här
  logiken att fungera. */
  isMetriaMapsAuth(credentials: AdmCredentials): boolean {
    return !this.isFastighetsokAuth(credentials);
  }

  isFastighetsokAuth(credentials: AdmCredentials): boolean {
    return "kundmarke" in credentials;
  }

  systemName(credentials: AdmCredentials): string {
    if (this.isMetriaMapsAuth(credentials)) {
      return System.METRIAMAPS;
    } else {
      return System.FASTIGHETSOK;
    }
  }

  onMetriaMapsAuthChange(auth: MetriaMapsAuth, index: number) {
    this.metriaMapsAuthChange.emit({
      system: System.METRIAMAPS,
      credentials: auth,
      index: index
    });
  }

  onFastighetsokAuthChange(auth: FastighetsokAuth, index: number) {
    this.fastighetsokAuthChange.emit({
      system: System.FASTIGHETSOK,
      credentials: auth,
      index: index
    });
  }

  onMetriaMapsAuthReset(authId: string, index: number) {
    this.authReset.emit({
      id: authId,
      index: index,
      system: System.METRIAMAPS
    });
  }

  onFastighetsokAuthReset(authId: string, index: number) {
    this.authReset.emit({
      id: authId,
      index: index,
      system: System.FASTIGHETSOK
    });
  }

  on(auth: MetriaMapsAuth, index: number) {
    this.metriaMapsAuthChange.emit({
      system: System.METRIAMAPS,
      credentials: auth,
      index: index
    });
  }
}
