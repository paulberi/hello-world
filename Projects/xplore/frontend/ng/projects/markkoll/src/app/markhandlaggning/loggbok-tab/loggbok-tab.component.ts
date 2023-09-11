import { Component, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { MkLoggItem } from "../../model/loggItem";
import { MkLoggbokTabPresenter } from "./loggbok-tab.presenter";

@Component({
  providers: [MkLoggbokTabPresenter],
  selector: "mk-loggbok-tab",
  templateUrl: "./loggbok-tab.component.html",
  styleUrls: ["./loggbok-tab.component.scss"]
})
export class MkLoggbokTabComponent implements OnChanges {
  @Input() loggbok: MkLoggItem[];
  @Input() savingAnteckningar = false;

  @Input() anteckningar: string;
  @Output() anteckningarChange = this.presenter.anteckningarChange;

  constructor(private presenter: MkLoggbokTabPresenter) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes.anteckningar) {
      this.presenter.initializeForm(this.anteckningar);
    }
  }

  showMoreLoggItems() {
    this.presenter.showMoreLoggItems(4);
  }

  canShowMoreLoggItems(): boolean {
    return this.presenter.canShowMoreLoggItems(this.loggbok);
  }

  onEmitAnteckning() {
    this.presenter.submit();
  }

  canSave(): boolean {
    return this.form.pristine;
  }

  get form() {
    return this.presenter.form;
  }

  get filteredLoggbok(): MkLoggItem[] {
    return this.loggbok;
  }
}
