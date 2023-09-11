import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormControl, FormGroupDirective, NgForm } from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";
import { Ledningsagare } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { MkLedningsagarePresenter } from "./ledningsagare.presenter";
import { TranslocoService } from "@ngneat/transloco";

export class NoErrorMatcher implements ErrorStateMatcher {
  isErrorState(control: UntypedFormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return false;
  }
}

@Component({
  selector: "mk-ledningsagare-ui",
  templateUrl: "./ledningsagare.component.html",
  styleUrls: ["./ledningsagare.component.scss"],
  providers: [MkLedningsagarePresenter]
})
export class MkLedningsagareComponent implements OnInit, OnChanges {
  @Input() ledningsagare: Ledningsagare[] = [];

  @Output() ledningsagareAdd = this.presenter.ledningsagareAdd;
  @Output() ledningsagareDelete = new EventEmitter<uuid>();

  readonly columns = ["delete", "namn"];
  readonly matcher = new NoErrorMatcher();

  private _ledningsagareSorted: Ledningsagare[];

  constructor(private presenter: MkLedningsagarePresenter, translate: TranslocoService) {}

  add() {
    this.presenter.submit();
  }

  delete(ledningsagare: Ledningsagare) {
    this.ledningsagareDelete.emit(ledningsagare.id);
  }

  get form() {
    return this.presenter.form;
  }

  get ledningsagareSorted() {
    return this._ledningsagareSorted;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.ledningsagare) {
      if (this.ledningsagare) {
        this._ledningsagareSorted = this.ledningsagare.slice().sort((a, b) => a.namn.localeCompare(b.namn));
      } else {
        this._ledningsagareSorted = this.ledningsagare;
      }
    }
  }

  ngOnInit() {
    this.initializeForm();
  }

  reset() {
    this.initializeForm();
  }

  private initializeForm() {
    this.presenter.initializeForm();
  }
}
