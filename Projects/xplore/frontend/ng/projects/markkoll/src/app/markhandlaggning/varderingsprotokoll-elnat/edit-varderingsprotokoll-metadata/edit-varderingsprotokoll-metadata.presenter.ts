import { EventEmitter } from "@angular/core";
import { FormControl, UntypedFormGroup } from "@angular/forms";
import { EditElnatVp } from "../../../../../../../generated/markkoll-api";
import { FormGroupChangeCheck } from "../../../../../../lib/util/formGroupChangeCheck";
import { uuid } from "../../../model/uuid";

export class MkEditVarderingsprotokollMetadataPresenter {
  _form: FormGroupChangeCheck;
  saveChanges = new EventEmitter<EditElnatVp[]>();
  editElnatVps: EditElnatVp[] = [];

  get form(): UntypedFormGroup {
    return this._form;
  }

  initialize(editElnatVps: EditElnatVp[]) {
    this.editElnatVps = editElnatVps;
    this._form = new FormGroupChangeCheck({
      varderingsmanOchForetag: new FormControl(editElnatVps.every(vp => vp.vpMetadata.varderingsmanOchForetag === editElnatVps[0].vpMetadata.varderingsmanOchForetag)
        ? editElnatVps[0].vpMetadata.varderingsmanOchForetag : "-"),
      ledning: new FormControl(editElnatVps.every(vp => vp.vpMetadata.ledning === editElnatVps[0].vpMetadata.ledning)
        ? editElnatVps[0].vpMetadata.ledning : "-"),
      spanningsniva: new FormControl(editElnatVps.every(vp => vp.vpMetadata.spanningsniva === editElnatVps[0].vpMetadata.spanningsniva)
        ? editElnatVps[0].vpMetadata.spanningsniva : "-"),

      franStation: new FormControl(editElnatVps.every(vp => vp.avtalMetadata.franStation === editElnatVps[0].avtalMetadata.franStation)
        ? editElnatVps[0].avtalMetadata.franStation : "-"),
      tillStation: new FormControl(editElnatVps.every(vp => vp.avtalMetadata.tillStation === editElnatVps[0].avtalMetadata.tillStation)
        ? editElnatVps[0].avtalMetadata.tillStation : "-"),
    })
  }

  submit(currentSelection: uuid[]) {
    const values = this.form.getRawValue()
    const shouldChange = this.editElnatVps.filter(vp => currentSelection.find(id => id === vp.fastighetsId))
    shouldChange.forEach(editElnatVp => {
      if (values.ledning !== "-") editElnatVp.vpMetadata.ledning = values.ledning;
      if (values.varderingsmanOchForetag !== "-") editElnatVp.vpMetadata.varderingsmanOchForetag = values.varderingsmanOchForetag;
      if (values.spanningsniva !== "-") editElnatVp.vpMetadata.spanningsniva = values.spanningsniva;
      if (values.franStation !== "-") editElnatVp.avtalMetadata.franStation = values.franStation;
      if (values.tillStation !== "-") editElnatVp.avtalMetadata.tillStation = values.tillStation;
    });
    this.saveChanges.emit(shouldChange);
  }

  canSave(): boolean {
    return this._form?.valid && this._form?.isChanged;
  }

  getNewSelection(editElnatVps: EditElnatVp[]) {
    return editElnatVps.map(vp => vp.fastighetsId);
  }

}