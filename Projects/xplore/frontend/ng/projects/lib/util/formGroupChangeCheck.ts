import {AbstractControl, AbstractControlOptions, AsyncValidatorFn, UntypedFormGroup, ValidatorFn} from "@angular/forms";
import {cloneDeep} from "lodash";
import { deepEqual } from "./deepEqual.util";

export class FormGroupChangeCheck extends UntypedFormGroup {
  savedChanges: Object;

  constructor(controls: { [key: string]: AbstractControl; },
              validatorOrOpts?: ValidatorFn | AbstractControlOptions | ValidatorFn[],
              asyncValidator?: AsyncValidatorFn | AsyncValidatorFn[]) {
    super(controls, validatorOrOpts, asyncValidator);
    this.savedChanges = cloneDeep(this.value);
  }

  markAsPristine(opts?: { onlySelf?: boolean }): void {
    super.markAsPristine(opts);
    this.savedChanges = cloneDeep(this.value);
  }

  get isChanged(): boolean {
    return !deepEqual(this.savedChanges, this.value);
  }
}
