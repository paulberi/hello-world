import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {Observable, of} from "rxjs";
import {catchError, debounceTime, flatMap, tap} from "rxjs/operators";
import {MatobjektService} from "../../services/matobjekt.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";

@Component({
  selector: "mdb-search-matobjekt-namn",
  template: `
    <form [formGroup]="searchForm">
      <mat-form-field>
        <input matInput type="text" [placeholder]="placeholder" formControlName="namn"
               [matAutocomplete]="autoNamn" />
        <mat-autocomplete (optionSelected)="onValjMatobjekt($event.option.value)" #autoNamn="matAutocomplete">
          <mat-option *ngFor="let namn of filteredMatobjektnamn | async" [value]="namn">{{namn}}</mat-option>
        </mat-autocomplete>
      </mat-form-field>
    </form>
  `,
  styles: [`
    form {
      display: grid;
      grid-gap: 1rem;
    }

    mat-form-field {
      width: 100%;
    }

    @media only screen and (min-width: 576px) {
      form {
        grid-template-columns: 1fr auto;
      }
    }
  `]
})
export class SearchMatobjektNamnComponent implements OnInit {
  @Input() matobjektTyp: number;
  @Input() placeholder: string;
  @Output() selected = new EventEmitter<number>();

  searchForm: UntypedFormGroup;

  validMatobjektnamn: Observable<boolean>;

  matobjektnamn: string[] = [];
  filteredMatobjektnamn: Observable<string[]>;

  constructor(private matobjektService: MatobjektService) {
  }

  ngOnInit() {
    this.searchForm = new UntypedFormGroup({
      namn: new UntypedFormControl(),
    });

    this.initNamn();
  }

  initNamn() {
    this.filteredMatobjektnamn = this.searchForm.get("namn").valueChanges.pipe(
      tap(() => this.validMatobjektnamn = of(false)),
      debounceTime(500),
      flatMap((value: string) => value.length > 0 ?
        this.matobjektService.getMatobjektNamn(value, this.matobjektTyp).pipe(
          catchError(() => of([])))
        : of([])
      ),
      tap(filteredMatobjektnamn => this.validMatobjektnamn =
        of(filteredMatobjektnamn.indexOf(this.searchForm.get("namn").value) !== -1))
    );
  }

  onValjMatobjekt(namn: string) {
    this.matobjektService.getMatobjektIdByNamn(namn).subscribe(
      id => this.selected.emit(id));
  }
}
