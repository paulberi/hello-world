import { Component, Input } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Property } from "../../../../../../../shared/utils/property-utils";

@Component({
  selector: "mm-object",
  templateUrl: "./object.component.html",
  styleUrls: ["./object.component.scss"]
})
export class ObjectComponent {
  @Input() dynamicForm: UntypedFormGroup;
  @Input() property: Property<string>;
}
