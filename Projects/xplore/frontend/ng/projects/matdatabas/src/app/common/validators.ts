import {AbstractControl} from "@angular/forms";

export const integerValidator = (control: AbstractControl) => {
  const value = control.value;

  if (value == null) {
    return null;
  }

  if (typeof value === "number") {
    if (Number.isInteger(value)) {
      return null;
    } else {
      return {isNaI: true};
    }
  }

  return {isNaI: true};
};
