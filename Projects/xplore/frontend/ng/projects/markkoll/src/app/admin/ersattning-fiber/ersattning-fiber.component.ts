import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Observable } from "rxjs";
import { FiberVarderingConfig, FiberVarderingConfigNamnAgare } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { MkErsattningFiberPresenter } from "./ersattning-fiber.presenter";

@Component({
  selector: "mk-ersattning-fiber-ui",
  templateUrl: "./ersattning-fiber.component.html",
  styleUrls: ["./ersattning-fiber.component.scss"],
  providers: [MkErsattningFiberPresenter]
})
export class MkErsattningFiberComponent implements OnInit {
  @Input() kundAvtalspartConfig: FiberVarderingConfigNamnAgare[];
  @Input() kundDefaultConfig: FiberVarderingConfigNamnAgare;
  @Output() defaultFiberVarderingConfigChange = new EventEmitter<FiberVarderingConfig>();
  @Output() fiberVarderingConfigCreate = this.presenter.fiberVarderingConfigCreate;
  @Output() fiberVarderingConfigChange = new EventEmitter<FiberVarderingConfigNamnAgare>();
  @Output() fiberVarderingConfigDelete = new EventEmitter<FiberVarderingConfigNamnAgare>();

  infoClosed = localStorage.getItem("ersattningFiberInfoClosed");

  constructor(private presenter: MkErsattningFiberPresenter) { }

  ngOnInit(): void {
    this.presenter.initializeForm();
  }

  submit() {
    this.presenter.submit();
  }

  get form() {
    return this.presenter.form;
  }

  closeInfoMessage() {
    localStorage.setItem("ersattningFiberInfoClosed", "true");
    this.infoClosed = localStorage.getItem("ersattningFiberInfoClosed");
  }

}
