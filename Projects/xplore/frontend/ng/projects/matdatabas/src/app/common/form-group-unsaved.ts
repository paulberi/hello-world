import {AbstractControl, AbstractControlOptions, AsyncValidatorFn, UntypedFormGroup, ValidatorFn} from "@angular/forms";
import {cloneDeep} from "lodash";
import {deepEqual} from "../../../../lib/util/deepEqual.util";

/**
 * A form that has been tampered with becomes dirty, and will stay so even when the changes in the form have been
 * restored. This decorator class includes a check (isChanged) for whether the form actually is altered or not.
 */
export class FormGroupUnsaved extends UntypedFormGroup {
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

  isChanged(): boolean {
    return !deepEqual(this.savedChanges, this.value);
  }
}
