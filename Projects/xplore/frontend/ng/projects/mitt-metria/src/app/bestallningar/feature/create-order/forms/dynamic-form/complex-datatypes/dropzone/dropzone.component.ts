import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Property } from "../../../../../../../shared/utils/property-utils";

@Component({
  selector: "mm-dropzone",
  templateUrl: "./dropzone.component.html",
  styleUrls: ["./dropzone.component.scss"]
})
export class DropzoneComponent implements OnInit {
  @Input() property: Property<string>;
  @Input() dynamicForm: UntypedFormGroup;
  @Input() acceptedFiles?: string[] = [".zip"];
  @Input() label?: string = "Ladda upp en zip-fil";

  @Output() onChanges = new EventEmitter();

  constructor() { }

  ngOnInit() {
    this.dynamicForm.get(this.property.key).valueChanges.subscribe(change => {
      this.onChanges.emit(change);
    })
  }
}
