import { Component, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { Observable } from "rxjs";
import { NisKalla } from "../../../../../../generated/markkoll-api";
import { MkNisKallaPresenter } from "./nis-kalla.presenter";

@Component({
  selector: "mk-nis-kalla-ui",
  templateUrl: "./nis-kalla.component.html",
  styleUrls: ["./nis-kalla.component.scss"],
  providers: [MkNisKallaPresenter]
})
export class MkNisKallaComponent implements OnChanges {
  @Input() nisKalla: NisKalla;

  @Output() nisKallaChange = this.presenter.change;

  constructor(private presenter: MkNisKallaPresenter) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes.nisKalla) {
      this.presenter.initializeForm(changes.nisKalla.currentValue);
    }
  }

  get form() {
    return this.presenter.form;
  }
}
