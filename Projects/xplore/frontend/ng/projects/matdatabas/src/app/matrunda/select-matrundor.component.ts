import {Component, Inject, OnInit} from "@angular/core";
import {ListMatrunda, Matrunda, MatrundaMatningstyp, MatrundaService} from "../services/matrunda.service";
import {Form, UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {Matningstyp, MatobjektService} from "../services/matobjekt.service";
import {Observable} from "rxjs";

@Component({
  selector: "mdb-select-matrundor",
  template: `
    <form [formGroup]="form">
      <h3>Välj mätningstyp och mätrunda</h3>
      <mat-form-field>
        <select matNativeControl placeholder="Mätningstyp" formControlName="matningstyp" required>
          <option *ngFor="let item of matningstyper" [value]="item.id">{{item.typ}}</option>
        </select>
      </mat-form-field>
      <mat-form-field>
        <select matNativeControl placeholder="Mätrunda" formControlName="matrunda" required>
          <option *ngFor="let item of matrundor" [value]="item.id">{{item.namn}}</option>
        </select>
      </mat-form-field>
      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <mdb-save-button [label]="'Spara'" (clicked)="onSave(form)" [saving]="saving"
                         [disabled]="!allowedToSave(form)"></mdb-save-button>
      </div>
    </form>
  `,
  styles: [`
    form {
      display: grid;
    }

    mat-form-field {
      width: 50%;
    }

  `]
})

export class SelectMatrundorComponent implements OnInit {
  matrundor: ListMatrunda[] = [];
  matningstyper: Matningstyp[];
  error: RestError = null;

  form = new UntypedFormGroup({
    "matningstyp": new UntypedFormControl(""),
    "matrunda": new UntypedFormControl("")
  });
  saving: boolean;

  constructor(
    private route: ActivatedRoute, private location: Location,
    private matrundaService: MatrundaService,
    private matobjektService: MatobjektService) {
  }

  ngOnInit() {
    const matobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.matobjektService.getMatningstyper(matobjektId).subscribe(matningstyper => {
        if (matningstyper.length === 1) {
          this.form.get("matningstyp").patchValue(matningstyper[0].id);
        }
        this.matningstyper = matningstyper;
      },
      () => this.error = RestError.GET_MATOBJEKT
    );

    this.matrundaService.getAll().subscribe(
      matrundor => {
        this.matrundor = matrundor;
      },
      () => this.error = RestError.GET_MATRUNDOR
    );
  }

  onSave(form: UntypedFormGroup) {
    const matrundaId = form.get("matrunda").value;
    const matningstypId = form.get("matningstyp").value;

    this.matrundaService.get(matrundaId).subscribe(
      matrunda => {
        const item: MatrundaMatningstyp = {
          matningstypId: matningstypId,
          ordning: matrunda.matningstyper.length + 1
        };

        matrunda.matningstyper.push(item);

        this.matrundaService.put(matrundaId, matrunda).subscribe(
          () => this.handleSaveSuccess(form, matrunda),
          () => this.handleSaveFailure(RestError.PUT_MATRUNDA)
        );
      },
      () => this.error = RestError.GET_MATRUNDA
    );
  }

  handleSaveSuccess(form: UntypedFormGroup, matrunda?: Matrunda) {
    this.saving = false;
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();
  }

  handleSaveFailure(error: RestError) {
    console.log("Kunde inte spara mätrunda, error: " + RestError);
    this.error = error;
  }

  goBack() {
    this.location.back();
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty;
  }
}

export enum RestError {
  GET_MATOBJEKT = "Misslyckades hämta grunduppgifter för mätobjektet. Ladda om sidan för att försöka på nytt.",
  GET_MATRUNDOR = "Misslyckades hämta information om mätrundorna. Ladda om sidan för att försöka på nytt.",
  GET_MATRUNDA = "Misslyckades hämta information om mätrundan. Ladda om sidan för att försöka på nytt.",
  PUT_MATRUNDA = "Misslyckades uppdatera mätrundan. Försök igen om en liten stund"
}
