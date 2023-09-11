import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from "@angular/core";
import {MkVirkesinlosenPresenter, } from "./virkesinlosen.presenter";
import { TillvaratagandeTyp } from "../../../../../../generated/markkoll-api";

@Component({
  selector: "mk-virkesinlosen-ui",
  templateUrl: "./virkesinlosen.component.html",
  styleUrls: ["./virkesinlosen.component.scss"],
  providers: [MkVirkesinlosenPresenter]
})
export class MkVirkesinlosenComponent implements OnChanges {

  @Input() skogligVardering = false;
  @Input() tillvaratagandeTyp: TillvaratagandeTyp = TillvaratagandeTyp.EJBESLUTAT;
  @Input() rotnetto: number = 0;
  @Input() egetTillvaratagande: number = 0;

  @Output() skogligVarderingChange = this.presenter.skogligVarderingChange;
  @Output() tillvaratagandeTypChange = this.presenter.tillvaratagandeTypChange;
  @Output() exportHms = new EventEmitter();

  constructor(private presenter: MkVirkesinlosenPresenter) {}

  ngOnChanges() {
    this.presenter.initializeForm(this.skogligVardering, this.tillvaratagandeTyp, this.rotnetto,
      this.egetTillvaratagande);
  }

  get form() {
    return this.presenter.form;
  }

  toggle() {
    this.skogligVardering = !this.skogligVardering;
  }

  get typEgetTillvaratagande(): string {
    return TillvaratagandeTyp.EGETTILLVARATAGANDE;
  }

  get typRotnetto(): string {
    return TillvaratagandeTyp.ROTNETTO;
  }
}
