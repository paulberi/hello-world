import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { ProjektLoggFilter } from "../../../../../../../generated/markkoll-api";

export class MkProjektloggTabPresenter {
  form: UntypedFormGroup;
  filterChange = new EventEmitter<ProjektLoggFilter[]>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      filter: new UntypedFormControl()
    });

    this.form.valueChanges
        .pipe(
          debounceTime(500),
          distinctUntilChanged()
        )
        .subscribe(form => this.filterChange.emit(form.filter));
  }
}
