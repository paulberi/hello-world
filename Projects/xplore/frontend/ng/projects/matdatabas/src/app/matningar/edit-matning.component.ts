import { Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {FELKOD_OK, Matning, Matningstyp, MatobjektService} from "../services/matobjekt.service";
import {UntypedFormControl, FormGroup} from "@angular/forms";
import {switchMap, tap} from "rxjs/operators";
import {DefinitionmatningstypService} from "../services/definitionmatningstyp.service";
import {formatNumber} from "@angular/common";
import {forkJoin, Observable} from "rxjs";
import {GranskaMatningComponent, SelectionMode} from "./granska-matning/granska-matning.component";
import {UserService} from "../services/user.service";
import {FormGroupUnsaved} from "../common/form-group-unsaved";
import {UNSAVED_CHANGES} from "../services/can-deactivate-guard.service";

@Component({
  selector: "mdb-matning",
  template: `
    <div *ngIf="form" class="main-content">
      <h2>{{matningstypNamn}}</h2>
      <form [formGroup]="form">
        <div class="matning-content">
          <h3>Mätvärde</h3>
            <div class="first-row">
              <mat-form-field class="datum">
                <input matInput placeholder="Datum" type="text" formControlName="avlastDatum">
              </mat-form-field>
              <mat-form-field class="avlastVarde">
                <input matInput placeholder="Uppmätt värde" type="decimal-number" formControlName="avlastVarde">
              </mat-form-field>
              <mat-form-field class="beraknatVarde">
                <input matInput placeholder="Beräknat värde" type="decimal-number" formControlName="beraknatVarde">
              </mat-form-field>
              <mat-form-field class="status">
                <input matInput placeholder="Status" type="text" formControlName="status">
              </mat-form-field>
            </div>
            <div class="second-row">
              <mat-form-field class="rapportor">
                <input matInput placeholder="Mätorganisation" type="text" formControlName="rapportor">
              </mat-form-field>
              <mat-form-field class="felkod">
                <input matInput placeholder="Felkod" type="text" formControlName="felkod">
              </mat-form-field>
              <mat-form-field *ngIf="fixpunkt" class="fixpunkt">
                <input matInput placeholder="Fixpunkt" type="text" formControlName="fixpunkt">
              </mat-form-field>
            </div>
            <div class="third-row">
              <mat-form-field class="kommentar">
                <textarea matInput mat-autosize="true" autocomplete="off" placeholder="Kommentar"
                          formControlName="kommentar" maxlength="250" matAutosizeMinRows=3>
                </textarea>
              </mat-form-field>
            </div>
        </div>
      </form>
      <div *ngIf="!readonly" class="granska-content">
        <h3>Granska mätvärde</h3>
        <mdb-granska-matning (updated)="onMatningUpdated($event)" (isChanged)="setChanged($event)" [matningar]="matningar" [matningstypId]="matningstypId"
                             [matobjektId]="matobjektId" [selectionMode]="selectionMode" #granskamatning></mdb-granska-matning>
      </div>
      <div class="matvardeslogg-content">
        <h3>Mätvärdeslogg</h3>
        <mdb-matningslogg [matning]="matning"></mdb-matningslogg>
      </div>
    </div>
  `,
  styles: [
    `
    .main-content {
      display: grid;
      grid-gap: 2rem;
    }

    .first-row {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: 1fr 1fr;
      width: 80%;
      margin-top: 1rem;
    }

    .second-row {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: 1fr 1fr;
      width: 80%;
      margin-top: 1rem;
    }

    .third-row {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: 1fr 1fr;
      width: 80%;
    }

    .granska-content {
      margin-bottom: 2rem;
    }

    .mat-input-element:disabled {
      color: rgb(0, 0, 0);
      cursor: not-allowed;
    }

    @media only screen and (min-width: 930px) {
      .first-row {
        display: grid;
        grid-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;
        margin-top: 1rem;
      }

      .second-row {
        display: grid;
        grid-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;
      }

      .third-row {
        display: grid;
        grid-gap: 2rem;
        grid-template-columns: 1fr 1fr;
        width: 80%;
      }
    }

  `
  ]
})
export class EditMatningComponent implements OnInit {

  matningstypNamn: string;
  matningstypId: number;
  matobjektId: number;
  matningId: number;
  matningstyp: Matningstyp;
  form: FormGroupUnsaved;
  decimaler: number;
  enhet: string;
  beraknadEnhet: string;
  matning: Matning;
  matningar: number[] = [];
  fixpunkt: string;
  matningStatus: {[key: number]: string} = {0: "Ej granskat", 1: "Godkänt", 2: "Fel"};
  detektion: {[key: number]: string} = {0: ">    ", 1: "", 2: "<    "};
  selectionMode: SelectionMode = SelectionMode.SINGLE;
  readonly = true;
  @ViewChild("granskamatning") granskaMatning: GranskaMatningComponent;
  granskaMatningChanged: boolean;

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService,
              private userService: UserService,
              private definitionmatningstypService: DefinitionmatningstypService) { }

  ngOnInit() {
    this.matningId = +this.route.snapshot.paramMap.get("matningid");
    this.matningstypId = +this.route.snapshot.paramMap.get("id");
    this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");

    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    forkJoin([this.matobjektService.getMatningstyp(this.matobjektId, this.matningstypId)
      .pipe(
        tap(matningstyp => {
          this.matningstyp = matningstyp;
          this.matningstypNamn = matningstyp.typ;
          this.fixpunkt = matningstyp.fixpunkt;
          this.enhet = matningstyp.enhet;
          this.decimaler = matningstyp.decimaler;
        }),
        switchMap(matningstyp => this.definitionmatningstypService.
          get(matningstyp.definitionMatningstypId))),
          this.matobjektService.getMatning(this.matobjektId, this.matningstypId, this.matningId)
            .pipe(
              tap(matning => this.matning = matning)
            )]).subscribe(([definition]) => {
            this.beraknadEnhet = definition.beraknadEnhet;
            this.matningar.push(this.matning.id);
            this.initForm();
        });
  }

  setChanged(isChanged: boolean): void {
    this.granskaMatningChanged = isChanged;
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged() || this.granskaMatningChanged) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  initForm() {
    this.form = new FormGroupUnsaved( {
      avlastDatum: new UntypedFormControl(this.formatDatum(this.matning.avlastDatum)),
      avlastVarde: new UntypedFormControl(this.getAvlastVarde(this.matning)),
      beraknatVarde: new UntypedFormControl(this.getBeraknatVarde(this.matning)),
      status: new UntypedFormControl(this.matningStatus[this.matning.status]),
      kommentar: new UntypedFormControl(this.matning.kommentar),
      felkod: new UntypedFormControl(this.matning.felkod),
      rapportor: new UntypedFormControl(this.matning.rapportor),
      fixpunkt: new UntypedFormControl(this.fixpunkt)
    });
    this.form.disable();
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

  setDigits(): string {
    return "1.0-" + (this.decimaler ? this.decimaler : 4);
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE", this.setDigits());
  }

  getBeraknatVarde(matning: Matning): string {
    // 0 is falsy, so we need to check the type as well.
    // null isn't type of number, so it shouldn't be shown.
    if (matning.beraknatVarde || (typeof matning.beraknatVarde === "number")) {
      return this.formatValue(matning.beraknatVarde) + " " + this.beraknadEnhet;
    } else {
      return null;
    }
  }

  getAvlastVarde(matning: Matning): string {
    if (matning.felkod !== FELKOD_OK) {
      return matning.felkod;
    } else if (matning.avlastVarde) {
      return this.detektion[matning.inomDetektionsomrade]  + " " + this.formatValue(matning.avlastVarde) + " " + this.enhet;
    } else {
      return null;
    }
  }

  onMatningUpdated(matning: Matning) {
    this.matningar = [];
    this.matning = matning;
    this.matningId = matning.id;
    this.initForm();
    this.matningar.push(this.matningId);
  }
}
