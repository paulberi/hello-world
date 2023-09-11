import { Component, forwardRef, Input, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { BehaviorSubject, Subscription } from 'rxjs';
import { JSONType, Property } from '../../../../../shared/utils/property-utils';
import { DynamicFormPresenter } from './dynamic-form.presenter';

@Component({
  selector: 'mm-dynamic-form',
  templateUrl: './dynamic-form.component.html',
  styleUrls: ['./dynamic-form.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DynamicFormPresenter),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => DynamicFormPresenter),
      multi: true
    },
    DynamicFormPresenter
  ]
})
export class DynamicFormComponent implements OnInit, OnDestroy {
  @Input() properties: BehaviorSubject<Property<string>[]>;
  @Input() required: BehaviorSubject<string[]>;
  options: (string | number)[] = [];
  subscriptions: Subscription[] = [];

  get dynamicForm(): UntypedFormGroup {
    return this.dynamicFormPresenter.dynamicForm;
  }

  constructor(private dynamicFormPresenter: DynamicFormPresenter) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.properties.subscribe((properties: Property<string>[]) => {
        if (properties?.length) {
          this.dynamicFormPresenter.initializeForm(properties, this.required).then().catch(err => {
            console.error(err)
          });
        }
      })
    )
  }

  convertToInputType(property: Property<string>) {
    if (property?.type === "string" || property?.type === "number") {
      this.options = property?.enum || [];
    } else if (property?.type === "array") {
      this.options = property?.items["enum"] || [];
    } else if (property?.enum?.length) {
      this.options = property?.enum || [];
    }
    let inputType: string;
    switch (property.type as JSONType) {
      case "string":
        if (property?.enum?.length) {
          property.enum.length > 5 ? inputType = "select" : inputType = "radio";
        } else {
          inputType = "text";
        }
        return inputType;
      case "array":
        if (property?.items?.["enum"]?.length) {
          property?.items["enum"]?.length > 5 ? inputType = "select" : inputType = "selection-list";
        } else {
          // To-Do Finns det anledningar till att det ska bli text här eller ska vi göra om till inputType=object, resultat ska ju bli en array?
          //inputType = "text";
          inputType = "object"
        }
        return inputType;
      case "object":
        return "object";
      case "number":
        if (property?.enum?.length) {
          property.enum.length > 5 ? inputType = "select" : inputType = "radio";
        } else {
          inputType = "number";
        }
        return inputType;
      case "boolean":
        return "checkbox";
      case "null":
        return null;
      case undefined:
        return null;
      default:
        if (property?.enum?.length) {
          property.enum.length > 5 ? inputType = "select" : inputType = "radio";
          return inputType;
        }
        //Returnerar object för att visa felmeddelande för användaren
        return "object";
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}

