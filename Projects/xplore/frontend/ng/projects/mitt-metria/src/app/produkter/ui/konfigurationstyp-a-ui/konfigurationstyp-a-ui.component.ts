import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControlStatus } from '@angular/forms';
import { GeoJson } from '../../../../../../lib/map-core/geojson.util';
import { KonfigurationsTypAProductVariant } from '../../feature/konfigurationstyp-a/konfigurationstyp-a.component';
import { AttributeFormResult, KonfigurationstypAPresenter } from '../../utils/konfigurationstyp-a.presenter';

export interface KonfigurationstypAFormControl {
  label: string;
  multiple: boolean;
  options: string[];
}

@Component({
  selector: 'mm-konfigurationstyp-a-ui',
  templateUrl: './konfigurationstyp-a-ui.component.html',
  styleUrls: ['./konfigurationstyp-a-ui.component.scss'],
  providers: [KonfigurationstypAPresenter]
})
export class KonfigurationstypAUiComponent implements OnInit {
  @Input() showDropzone: boolean;
  @Input() parentFormControlOptions: KonfigurationsTypAProductVariant[] = [];
  @Input() childFormControl: KonfigurationstypAFormControl;
  @Input() referenssystem: string[] = [];
  @Input() leveransformat: string[] = [];

  @Output() activeStepChanges = new EventEmitter<number>();
  @Output() statusChanges = new EventEmitter<FormControlStatus>();
  @Output() fileChanges = new EventEmitter<GeoJson[]>();
  @Output() selectChanges = new EventEmitter<KonfigurationstypAFormControl>();
  @Output() attributeFormChanges = new EventEmitter<AttributeFormResult>();

  activeStep: number = 1;

  constructor(private presenter: KonfigurationstypAPresenter) { }
  get konfigurationstypAForm() {
    return this.presenter.konfigurationstypAForm;
  }
  get areaForm() {
    return this.presenter.areaForm;
  }
  get attributeForm() {
    return this.presenter.attributeForm;
  }

  ngOnInit(): void {
    this.presenter.initializeKonfigurationForm();
    this.presenter.statusChanges.subscribe(status => {
      if (status === "VALID") {
        this.activeStep = 4;
      }
      this.statusChanges.emit(status)
    });
    this.presenter.fileChanges.subscribe(value => this.fileChanges.emit(value));
    this.presenter.selectChanges.subscribe(value => this.selectChanges.emit(value));
    this.presenter.attributeFormChanges.subscribe(value => this.attributeFormChanges.emit(value));
  }

  next() {
    this.activeStep++;
    if (this.activeStep === 3) {
      if (this.attributeForm.valid) {
        this.activeStep = 4;
      } else {
        this.activeStep = 3;
      }
    }
    this.activeStepChanges.emit(this.activeStep);
  }

  previous() {
    if (this.activeStep === 4) {
      this.activeStep = 2;
    } else {
      this.activeStep--;
    }
    this.activeStepChanges.emit(this.activeStep);
  }
}