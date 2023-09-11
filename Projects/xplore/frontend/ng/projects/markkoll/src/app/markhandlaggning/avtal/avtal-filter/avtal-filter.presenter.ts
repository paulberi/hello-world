import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";

export class MkAvtalFilterPresenter {
  form: UntypedFormGroup;

  filterChange = new EventEmitter<MkAvtalsfilter>();

  initializeForm(filter: MkAvtalsfilter) {
    const f = {...filter};
    this.form = new UntypedFormGroup({
      searchControl: new UntypedFormControl(f.search),
      statusControl: new UntypedFormControl(f.status)
    });

    this.form.valueChanges.subscribe(form => {
      const filterEmit: MkAvtalsfilter = {
        search: form.searchControl,
        status: form.statusControl
      };

      this.filterChange.emit(filterEmit);
    });
  }
}
