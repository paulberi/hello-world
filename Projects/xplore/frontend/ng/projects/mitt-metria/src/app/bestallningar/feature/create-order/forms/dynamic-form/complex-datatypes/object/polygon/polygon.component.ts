import { Component, Input } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Property } from "../../../../../../../../shared/utils/property-utils";

@Component({
  selector: "mm-polygon",
  templateUrl: "./polygon.component.html",
  styleUrls: ["./polygon.component.scss"]
})
export class PolygonComponent {
  @Input() dynamicForm: UntypedFormGroup;
  @Input() acceptedFiles?: string[] = [".zip"];
  @Input() label: string = "Ladda upp en zip-fil";
  @Input() property: Property<string>;
}
