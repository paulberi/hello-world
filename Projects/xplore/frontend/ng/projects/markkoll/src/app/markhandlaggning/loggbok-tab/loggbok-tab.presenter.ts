import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { MkLoggItem } from "../../model/loggItem";

export class MkLoggbokTabPresenter {
  form: UntypedFormGroup;
  visibleLoggItems = 4;
  anteckningarChange = new EventEmitter<string>();

  initializeForm(anteckningar: string) {
    this.form = new UntypedFormGroup({
      anteckning: new UntypedFormControl(anteckningar)
    });
  }

  submit() {
    this.anteckningarChange.emit(this.form.controls.anteckning.value);
  }

  filteredLoggbok(loggbok: MkLoggItem[]): MkLoggItem[] {
    return loggbok?.slice(0, this.visibleLoggItems) || [];
  }

  canShowMoreLoggItems(loggbok: MkLoggItem[]): boolean {
    return this.visibleLoggItems < this.filteredLoggbok(loggbok).length;
  }

  showMoreLoggItems(amount: number) {
    this.visibleLoggItems += amount;
  }
}
