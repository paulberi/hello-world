import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormGroup, UntypedFormGroup } from '@angular/forms';
import { EditElnatVp } from '../../../../../../../generated/markkoll-api';
import { uuid } from '../../../model/uuid';
import { MkEditVarderingsprotokollMetadataPresenter } from './edit-varderingsprotokoll-metadata.presenter';

@Component({
  selector: 'mk-edit-varderingsprotokoll-metadata',
  templateUrl: './edit-varderingsprotokoll-metadata.component.html',
  styleUrls: ['./edit-varderingsprotokoll-metadata.component.scss'],
  providers: [MkEditVarderingsprotokollMetadataPresenter]
})
export class EditVarderingsprotokollMetadataComponent implements OnInit, OnChanges {
  @Output() saveChanges = this.presenter.saveChanges;
  @Output() cancel = new EventEmitter<void>();

  @Input() editElnatVps: EditElnatVp[];
  @Input() registerenheter;
  currentSelection: uuid[] = [];

  constructor(private presenter: MkEditVarderingsprotokollMetadataPresenter) { }

  ngOnInit(): void {
    this.presenter.initialize(this.editElnatVps);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.editElnatVps && (changes.editElnatVps.currentValue !== changes.editElnatVps.previousValue)) {
      this.currentSelection = this.presenter.getNewSelection(changes.editElnatVps.currentValue);
      this.presenter.initialize(changes.editElnatVps.currentValue);
    }
  }

  submit() {
    this.presenter.submit(this.currentSelection);
  }

  canSave() {
    return this.presenter.canSave();
  }

  allFastigheterChecked() {
    return this.editElnatVps.every(vp => this.currentSelection.find(s => s === vp.fastighetsId));

  }

  noFastighetChecked(): boolean {
    return this.editElnatVps.every(vp => !this.currentSelection.find(s => s === vp.fastighetsId));
  }

  someFastigheterChecked() {
    return !this.noFastighetChecked() && !this.allFastigheterChecked()
  }

  fastigheterCheckAllChange(checked) {
    if (checked) {
      this.editElnatVps.forEach(vp => {
        if (!this.currentSelection.find(s => s === vp.fastighetsId)) {
          this.currentSelection.push(vp.fastighetsId)
        }
      })
    } else {
      this.editElnatVps.forEach(vp => {
        this.currentSelection.splice(this.currentSelection.indexOf(vp.fastighetsId), 1)
      })
    }
  }

  change(checked, fastighetId) {
    checked ? this.currentSelection.push(fastighetId) : this.currentSelection.splice(this.currentSelection.indexOf(fastighetId), 1);
  }

  fastighetSelected(fastighetId) {
    return this.currentSelection.find(f => f === fastighetId) ?? false;
  }

  get form(): FormGroup {
    return this.presenter.form;
  }
}
