import { ChangeDetectionStrategy, Component, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { OptionItem } from "../../../common/filter-option/filter-option.component";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { MkAvtalFilterPresenter } from "./avtal-filter.presenter";

/**
 * Filtrera på avtal efter önskade filterinställningar
 */
@Component({
  providers: [MkAvtalFilterPresenter],
  selector: "mk-avtal-filter",
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: "./avtal-filter.component.html",
  styleUrls: ["./avtal-filter.component.css"]
})
export class MkAvtalFilterComponent implements OnChanges {

  /** Filterinställningar */
  @Input() filter: MkAvtalsfilter;

  /** Emittas när ändring har skett i filterinställningarna */
  @Output() filterChange = this.presenter.filterChange;

  /** Menyval för avtalsstatusar */
  @Input() statusOptions: OptionItem[];

  constructor(private presenter: MkAvtalFilterPresenter) { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.filter) {
      this.presenter.initializeForm(this.filter);
    }
  }

  get form() {
    return this.presenter.form;
  }
}
