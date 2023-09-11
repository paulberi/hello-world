import {Component, OnInit} from "@angular/core";
import {DefinitionMatningstyp, DefinitionmatningstypService} from "../../services/definitionmatningstyp.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {Matningstyp, MatobjektService, TYP_VATTENKEMI} from "../../services/matobjekt.service";
import {Location} from "@angular/common";
import {integerValidator} from "../../common/validators";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {UserService} from "../../services/user.service";
import {Observable} from "rxjs";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-matningstyp",
  template: `
    <div class="information" *ngIf="definition">
      <h2>{{definition.namn}}</h2>
      <h3>Beskrivning</h3>
      <div>{{definition.beskrivning}}</div>
      <div>Mätning: {{matningLabel(definition)}}</div>
      <div *ngIf="definition.beraknadStorhet && definition.beraknadEnhet">Beräkning: {{definition.beraknadStorhet}}
        [{{definition.beraknadEnhet}}]
      </div>
    </div>

    <form *ngIf="form && grupper" [formGroup]="form" lang="en">
      <div class="form-section">
        <h3>Mätansvarig och mätintervall</h3>
        <div class="form-controls">
          <mat-form-field>
            <select matNativeControl placeholder="Mätansvarig" formControlName="matansvarigAnvandargruppId" required>
              <option *ngFor="let grupp of grupper" [value]="grupp.id">{{grupp.namn}}</option>
            </select>
            <mat-hint>Användargrupp som ansvarar för mätningar.</mat-hint>
          </mat-form-field>
          <div>
            <h4>Mätintervall</h4>
            <div class="matintervall">
              <mat-form-field>
                <input type="number" autocomplete="off" matInput placeholder="Antal gånger" required
                       formControlName="matintervallAntalGanger">
                <mat-error>{{form.controls.matintervallAntalGanger.errors?.error}}</mat-error>
              </mat-form-field>
              <span>per</span>
              <mat-form-field>
                <select matNativeControl placeholder="Tidsenhet" formControlName="matintervallTidsenhet" required>
                  <option [value]="0">Timme</option>
                  <option [value]="1">Dag</option>
                  <option [value]="2">Vecka</option>
                  <option [value]="3">Månad</option>
                  <option [value]="4">År</option>
                </select>
              </mat-form-field>
            </div>
          </div>
          <mat-form-field>
            <input type="number" autocomplete="off" matInput placeholder="Dagar innan påminnelse" required
                   formControlName="paminnelseDagar">
            <mat-hint>När systemet ska skicka ut en påminnelse till mätansvariga.</mat-hint>
            <mat-error>Ange ett antal mellan 1 och 99</mat-error>
          </mat-form-field>
          <mat-checkbox formControlName="aktiv">Aktiv</mat-checkbox>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.automatiskInrapportering">
        <h3>Automatisk inrapportering</h3>
        <div class="form-controls">
          <mat-form-field>
            <input matInput maxlength="60" placeholder="Instrument" formControlName="instrument">
            <mat-hint>Identitet på instrumentet.</mat-hint>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.automatiskGranskning">
        <h3>Automatisk granskning</h3>
        <div class="form-controls">
          <mat-checkbox formControlName="granskasAutomatiskt">Granskas automatiskt</mat-checkbox>
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Min" formControlName="granskasMin">
            <mat-hint>Mingräns för inrapporterade mätvärden</mat-hint>
            <mat-error>Ange ett giltigt min-värde.</mat-error>
          </mat-form-field>
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Max" formControlName="granskasMax">
            <mat-hint>Maxgräns för inrapporterade mätvärden</mat-hint>
            <mat-error>Ange ett giltigt max-värde</mat-error>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.isNivaVattenOchLufttryck()">
        <h3>Attribut för nivå (vatten- och lufttryck)</h3>
        <div class="form-controls">
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Konstant" required formControlName="berakningKonstant">
            <mat-hint>Konstant (k<sub>ref, ber</sub>) för beräkning av grundvattennivå.</mat-hint>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.isNivaNedmatning()">
        <h3>Attribut för nivå (nedmätning)</h3>
        <div class="form-controls">
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Referensnivå" required
                   formControlName="berakningReferensniva">
            <span matSuffix>m</span>
            <mat-hint>Referensnivå (z<sub>ref</sub>) för beräkning av vattennivå från nedmätning.</mat-hint>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Gradningskonstant" required
                   formControlName="berakningKonstant">
            <mat-hint>Gradningskonstant (k<sub>grad</sub>) som anger eventuell lutning på röret.</mat-hint>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Maximalt pejlbart djup" required
                   formControlName="maxPejlbartDjup">
            <span matSuffix>m</span>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.isNivaPortryck()">
        <h3>Attribut för nivå (portryck)</h3>
        <div class="form-controls">
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Spetsnivå" required
                   formControlName="berakningReferensniva">
            <span matSuffix>m</span>
            <mat-hint>Spetsnivå (z<sub>spets</sub>).</mat-hint>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
          <mat-form-field>
            <input type="decimal-number" autocomplete="off" matInput placeholder="Gradningskonstant" required
                   formControlName="berakningKonstant">
            <mat-hint>Gradningskonstant (k<sub>grad</sub>). Anger eventuell lutning på röret.</mat-hint>
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.isSattning()">
        <h3>Fixpunkt</h3>
        <div class="form-controls">
          <mat-form-field>
            <input matInput placeholder="Identitet" required formControlName="fixpunkt">
            <mat-hint>Identitet för den fixpunkt som ska användas vid mätning av sättningar.</mat-hint>
          </mat-form-field>
        </div>
      </div>

      <div class="form-section" *ngIf="definition.isVattenkemi()">
        <h3>Attribut för vattenkemi</h3>
        <div class="form-controls">
          <mat-form-field>
            <input matInput placeholder="Enhet" required formControlName="enhet">
          </mat-form-field>
          <mat-form-field>
            <input type="number" autocomplete="off" matInput placeholder="Decimaler" required formControlName="decimaler">
            <mat-error>Ange (max) en siffra.</mat-error>
          </mat-form-field>
        </div>
      </div>

      <div class="actions">
        <button type="button" (click)="goBack()" mat-stroked-button color="primary">Tillbaka</button>
        <button type="button" mat-raised-button color="primary" (click)="onSave()" [disabled]="!form.dirty || !form.valid">
          Spara
        </button>
        <button type="button" (click)="onDelete()" mat-raised-button color="warn" [disabled]="!canDelete">Ta bort</button>
      </div>
    </form>

  `,
  styles: [`
    .information {
      margin-bottom: 2rem;
    }

    form {
      display: grid;
      grid-gap: 3rem;
    }

    .form-controls {
      display: grid;
      grid-gap: 1rem;
    }

    .matintervall > span {
      margin-left: 0.5rem;
      margin-right: 0.5rem;
    }
  `]
})
export class EditMatningstypComponent implements OnInit {
  currentMatobjektId: number;
  currentMatningstypId: string;
  definition: DefinitionMatningstyp;
  form: FormGroupUnsaved;
  grupper: Anvandargrupp[];
  canDelete = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private dialogService: DialogService,
              private matobjektService: MatobjektService,
              private userService: UserService,
              private anvandargruppService: AnvandargruppService,
              private definitionMatningstypService: DefinitionmatningstypService) {
  }

  ngOnInit() {
    this.currentMatobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.route.paramMap.subscribe(params => {
      this.currentMatningstypId = params.get("id");
      if (this.currentMatningstypId === "new") {
        const definitionMatningstypId = +this.route.snapshot.paramMap.get("definitionMatningstypId");
        this.definitionMatningstypService.get(definitionMatningstypId).subscribe(definition => {
          this.definition = definition;
          this.form = this.createForm(this.getDefaultValues(definition));
          this.setupValidation();
        });
      } else {
        this.matobjektService.getMatningstyp(this.currentMatobjektId, +this.currentMatningstypId).subscribe(matningstyp => {
          this.definitionMatningstypService.get(matningstyp.definitionMatningstypId).subscribe(definition => {
            this.definition = definition;
            this.form = this.createForm(matningstyp);
            this.setupValidation();
          });

          this.matobjektService.canDeleteMatningstyp(this.currentMatobjektId, +this.currentMatningstypId)
            .subscribe(canDelete => this.canDelete = canDelete);
        });
      }
    });

    this.anvandargruppService.getAll().subscribe(grupper => this.grupper = grupper);
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  matningLabel(definition: DefinitionMatningstyp) {
    if (definition.storhet != null) {
      return definition.storhet + " [" + definition.enhet + "]";
    } else {
      return definition.enhet;
    }
  }

  // Setup non static lists of validators.
  private setupValidation() {
    const granskasAutomatiskt = this.form.get("granskasAutomatiskt");
    if (granskasAutomatiskt) {
      this.setupGransvardeValidation(granskasAutomatiskt.value);
      granskasAutomatiskt.valueChanges.subscribe(value => {
        this.setupGransvardeValidation(value);
      });
    }
  }

  private setupGransvardeValidation(granskasAutomatiskt: boolean) {
    const granskasMin = this.form.get("granskasMin");
    const granskasMax = this.form.get("granskasMax");
    if (granskasAutomatiskt) {
      granskasMin.setValidators([Validators.required]);
      granskasMax.setValidators([Validators.required]);
    } else {
      granskasMin.setValidators(null);
      granskasMax.setValidators(null);
    }
    granskasMin.updateValueAndValidity();
    granskasMax.updateValueAndValidity();
  }

  private createForm(m: Partial<Matningstyp>) {
    const newForm = new FormGroupUnsaved({
      matansvarigAnvandargruppId: new UntypedFormControl(m.matansvarigAnvandargruppId),
      matintervallAntalGanger: new UntypedFormControl(m.matintervallAntalGanger, integerValidator),
      matintervallTidsenhet: new UntypedFormControl(m.matintervallTidsenhet),
      paminnelseDagar: new UntypedFormControl(m.paminnelseDagar, [integerValidator, Validators.min(1), Validators.max(99)]),
      aktiv: new UntypedFormControl(m.aktiv),
      instrument: new UntypedFormControl(m.instrument, Validators.maxLength(60)),
      granskasAutomatiskt: new UntypedFormControl(m.granskasAutomatiskt),
      granskasMin: new UntypedFormControl(m.granskasMin),
      granskasMax: new UntypedFormControl(m.granskasMax),
      berakningKonstant: new UntypedFormControl(m.berakningKonstant),
      berakningReferensniva: new UntypedFormControl(m.berakningReferensniva),
      maxPejlbartDjup: new UntypedFormControl(m.maxPejlbartDjup),
      fixpunkt: new UntypedFormControl(m.fixpunkt),
      enhet: new UntypedFormControl(m.enhet),
      decimaler: new UntypedFormControl(m.decimaler, [integerValidator, Validators.max(9)])
    }, {validators: saneIntervalValidator});

    if (!this.userService.userDetails.isTillstandshandlaggare()) {
      newForm.disable();
    }

    return newForm;
  }

  private getDefaultValues(definition: DefinitionMatningstyp): Partial<Matningstyp> {
    return {
      matintervallAntalGanger: 1,
      matintervallTidsenhet: 3,
      paminnelseDagar: 3,
      aktiv: true,
      granskasAutomatiskt: false,
      enhet: definition.matobjektTyp === TYP_VATTENKEMI ? definition.enhet : null,
      decimaler: definition.matobjektTyp === TYP_VATTENKEMI ? definition.decimaler : null
    };
  }

  goBack() {
    this.location.back();
  }

  goToParent() {
    this.router.navigate(["../"], {relativeTo: this.route});
  }

  onDelete() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort mätningstypen?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      (confirmed) => {
        if (confirmed) {
          this.matobjektService.deleteMatningstyp(this.currentMatobjektId, +this.currentMatningstypId).subscribe(() => {
            this.form.markAsPristine();
            this.goBack();
          });
        }
      });
  }

  onSave() {
    const value: Partial<Matningstyp> = this.form.value;
    value.definitionMatningstypId = this.definition.id;
    if (this.currentMatningstypId === "new") {
      this.matobjektService.postMatningstyp(this.currentMatobjektId, this.definition.id, value).subscribe(matningstyp => {
        this.form.markAsPristine();
        this.router.navigate(["../", matningstyp.id], {relativeTo: this.route, replaceUrl: true});
      });
    } else {
      this.matobjektService.putMatningstyp(this.currentMatobjektId, +this.currentMatningstypId, value).subscribe(() => {
        this.form.markAsPristine();
      });
    }
  }
}

const saneIntervalValidator = (form: UntypedFormGroup) => {
  const antalGanger = form.get("matintervallAntalGanger");
  const tidsenhet = form.get("matintervallTidsenhet");
  const a = +antalGanger.value;
  const t = +tidsenhet.value;
  if (antalGanger.errors && antalGanger.errors.isNaI) {
    antalGanger.setErrors({error: "Ange ett heltal."});
  } else if (a < 1) {
    antalGanger.setErrors({error: "Minst 1 gång per tidsenhet."});
  } else if (t === 0 && a > 60) {
    antalGanger.setErrors({error: "Maximum är 60 gånger per timme."});
  } else if (t === 1 && a > 24) {
    antalGanger.setErrors({error: "Maximum är 24 gånger per dag."});
  } else if (t === 2 && a > 7) {
    antalGanger.setErrors({error: "Maximum är 7 gånger per vecka."});
  } else if (t === 3 && a > 4) {
    antalGanger.setErrors({error: "Maximum är 4 gånger per månad."});
  } else if (t === 4 && a > 12) {
    antalGanger.setErrors({error: "Maximum är 12 gånger per år."});
  } else {
    antalGanger.setErrors(null);
  }
  return null;
};

