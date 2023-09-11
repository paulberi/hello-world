import { AbstractControl, ControlValueAccessor, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validator, Validators } from "@angular/forms";
import { BehaviorSubject } from "rxjs";
import { Property } from "../../../../../shared/utils/property-utils";

export class DynamicFormPresenter implements ControlValueAccessor, Validator {
  private _dynamicForm: UntypedFormGroup;
  onChange: any = () => { };
  onTouched: any = () => { };

  get dynamicForm(): UntypedFormGroup {
    return this._dynamicForm;
  }

  get value(): any {
    return this._dynamicForm.value;
  }
  set value(value: any) {
    this._dynamicForm.setValue(value);
    this.onChange(value);
    this.onTouched();
  }

  //Väntar på att formuläret har skapats upp och sedan körs writeValue för att parentForm ska kunna se 
  //värdena i formControls om det sätts några när formuläret skapas
  async initializeForm(properties: Property<string>[], required: BehaviorSubject<string[]>) {
    this._dynamicForm = await this.createForm(properties, required);
    this.writeValue(this._dynamicForm.value)

    this._dynamicForm.valueChanges.subscribe(value => {
      this.onChange(value);
      this.onTouched();
    })
  }

  createForm(properties: Property<string>[], required: BehaviorSubject<string[]>): Promise<UntypedFormGroup> {
    return new Promise<UntypedFormGroup>((resolve, reject) => {
      try {
        let group: any = {};
        for (const property of properties) {
          let formControl;
          property.type === "boolean" ? formControl = new UntypedFormControl(property.value || false) : formControl = new UntypedFormControl(property.const || property.value || null);
          this.addValidators(required, formControl, property);
          group[property.key] = formControl;
        }
        group = new UntypedFormGroup(group);
        resolve(group);
      } catch {
        reject("Error: Could not create dynamic form");
      }
    })
  }

  addValidators(required: BehaviorSubject<string[]>, formControl: UntypedFormControl, property: Property<string>,) {
    required.subscribe((required: string[]) => {
      if (required?.length) {
        const ifRequired = required?.includes(property.key);
        ifRequired ? formControl.addValidators([Validators.required]) : null;
      }

      property.minItems ? formControl.addValidators([Validators.minLength(property.minItems)]) : null;
      property.maxItems ? formControl.addValidators([Validators.maxLength(property.maxItems)]) : null;
    })
  }

  writeValue(value: any): void {
    value ? this.value = value : null;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  validate(control: AbstractControl): ValidationErrors {
    return control.value && this._dynamicForm.valid ? null : { dynamicForm: true }
  }
}
