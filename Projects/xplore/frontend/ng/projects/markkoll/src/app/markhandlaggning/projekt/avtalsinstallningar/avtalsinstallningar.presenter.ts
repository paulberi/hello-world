import { EventEmitter } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { Avtalsinstallningar, BerakningAbel07, BerakningRev } from "../../../../../../../generated/markkoll-api";

export class AvtalsinstallningarPresenter {
  form: FormGroup;
  avtalsinstallningarChange = new EventEmitter<Avtalsinstallningar>();

  initializeForm(avtalsinstallningar: Avtalsinstallningar) {
    this.form = new FormGroup({
      berakningRev: new FormControl(avtalsinstallningar?.berakningRev),
      berakningAbel07: new FormControl(avtalsinstallningar?.berakningAbel07),
      mappstrategi: new FormControl(avtalsinstallningar?.mappstrategi),
    });

    this.form.valueChanges.subscribe(f => {
      this.avtalsinstallningarChange.emit({...f});
    });
  }

  updateForm(avtalsinstallningar: Avtalsinstallningar) {
    this.form.setValue({
      berakningRev: avtalsinstallningar?.berakningRev,
      berakningAbel07: avtalsinstallningar?.berakningAbel07,
      mappstrategi: avtalsinstallningar?.mappstrategi
    });
  }
}
