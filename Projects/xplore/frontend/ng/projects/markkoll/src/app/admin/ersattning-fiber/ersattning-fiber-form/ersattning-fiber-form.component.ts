import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FiberVarderingConfigNamnAgare } from "../../../../../../../generated/markkoll-api";
import { MkErsattningFiberFormPresenter } from "./ersattning-fiber-form.presenter";

@Component({
  selector: "mk-ersattning-fiber-form",
  templateUrl: "./ersattning-fiber-form.component.html",
  styleUrls: ["./ersattning-fiber-form.component.scss"],
  providers: [MkErsattningFiberFormPresenter]
})
export class MkErsattningFiberFormComponent implements OnInit {
  @Input() fiberConfig: FiberVarderingConfigNamnAgare;
  @Input() kundDefault = false;

  @Output() fiberVarderingConfigChange = this.presenter.fiberVarderingConfigChange;
  @Output() fiberVarderingConfigDelete = new EventEmitter<FiberVarderingConfigNamnAgare>();
  @Output() cancel = new EventEmitter<void>();

  constructor(private presenter: MkErsattningFiberFormPresenter) { }

  ngOnInit(): void {
    this.presenter.initializeForm(this.fiberConfig);
  }

  get form() {
    return this.presenter.form;
  }

  submit() {
    this.presenter.submit();
  }

  isFormValid() {
    return this.form.valid && this.form.dirty;
  }

  cancelChanges() {
    this.presenter.reset();
    this.cancel.emit();
  }
}
