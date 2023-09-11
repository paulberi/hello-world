import { EventEmitter } from "@angular/core";
import { UntypedFormControl, Validators, UntypedFormGroup, FormControl } from "@angular/forms";
import { FiberVarderingConfig, FiberVarderingConfigNamnAgare } from "../../../../../../../generated/markkoll-api";

export class MkErsattningFiberFormPresenter {
  form: UntypedFormGroup;
  defaultFormValue;
  _fiberVarderingConfigNamnAgare: FiberVarderingConfigNamnAgare;

  fiberVarderingConfigChange = new EventEmitter<FiberVarderingConfigNamnAgare>();

  initializeForm(fiberVarderingConfigNamnAgare: FiberVarderingConfigNamnAgare) {
    this._fiberVarderingConfigNamnAgare = fiberVarderingConfigNamnAgare;
    const fiberVarderingConfig = fiberVarderingConfigNamnAgare.fiberVarderingConfig;

    this.form = new UntypedFormGroup({
      namn: new FormControl(fiberVarderingConfigNamnAgare.namn ?? null),
      personnummer: new FormControl(fiberVarderingConfigNamnAgare.personnummer ?? null),
      markskapSkog: new FormControl(fiberVarderingConfig.markskap.skog, Validators.required),
      markskapJordbruksimpediment: new FormControl(fiberVarderingConfig.markskap.jordbruksimpediment, Validators.required),
      markskapOvrigMark: new FormControl(fiberVarderingConfig.markskap.ovrigMark, Validators.required),
      optobrunnSkog: new FormControl(fiberVarderingConfig.optobrunn.skog, Validators.required),
      optobrunnJordbruksimpediment: new FormControl(fiberVarderingConfig.optobrunn.jordbruksimpediment, Validators.required),
      optobrunnOvrigMark: new FormControl(fiberVarderingConfig.optobrunn.ovrigMark, Validators.required),
      teknikbodSkog6x6: new FormControl(fiberVarderingConfig.teknikbod.skog6x6m, Validators.required),
      teknikbodSkog8x8: new FormControl(fiberVarderingConfig.teknikbod.skog8x8m, Validators.required),
      teknikbodSkog10x10: new FormControl(fiberVarderingConfig.teknikbod.skog10x10m, Validators.required),
      teknikbodJordbruksimpediment6x6: new FormControl(fiberVarderingConfig.teknikbod.jordbruksimpediment6x6m, Validators.required),
      teknikbodJordbruksimpediment8x8: new FormControl(fiberVarderingConfig.teknikbod.jordbruksimpediment8x8m, Validators.required),
      teknikbodJordbruksimpediment10x10: new FormControl(fiberVarderingConfig.teknikbod.jordbruksimpediment10x10m, Validators.required),
      teknikbodOvrigMark6x6: new FormControl(fiberVarderingConfig.teknikbod.ovrigMark6x6m, Validators.required),
      teknikbodOvrigMark8x8: new FormControl(fiberVarderingConfig.teknikbod.ovrigMark8x8m, Validators.required),
      teknikbodOvrigMark10x10: new FormControl(fiberVarderingConfig.teknikbod.ovrigMark10x10m, Validators.required),
      schablon1M: new FormControl(fiberVarderingConfig.schablonersattning.optoror1m, Validators.required),
      schablon2M: new FormControl(fiberVarderingConfig.schablonersattning.optoror2m, Validators.required),
      grundersattning: new FormControl(fiberVarderingConfig.grundersattning, Validators.required),
      minimiersattningstillagg: new FormControl(fiberVarderingConfig.minimiersattning, Validators.required),
      additionAsPerExpropriationslagen: new FormControl(fiberVarderingConfig.tillaggExpropriationslagen, Validators.required),
      specialCompensationAtAgreement: new FormControl(fiberVarderingConfig.sarskildErsattning, Validators.required),
      sarskildErsattningMaxbelopp: new FormControl(fiberVarderingConfig.sarskildErsattningMaxbelopp),
      minimiersattningEnbartMarkledning: new FormControl(fiberVarderingConfig.minimiersattningEnbartMarkledning),
    });
    this.defaultFormValue = this.form.value;
  }

  submit() {
    const fiberVarderingConfig: FiberVarderingConfig = {
      id: this._fiberVarderingConfigNamnAgare.fiberVarderingConfig.id,
      markskap: {
        skog: this.form.controls.markskapSkog.value,
        jordbruksimpediment: this.form.controls.markskapJordbruksimpediment.value,
        ovrigMark: this.form.controls.markskapOvrigMark.value,
      },
      optobrunn: {
        skog: this.form.controls.optobrunnSkog.value,
        jordbruksimpediment: this.form.controls.optobrunnJordbruksimpediment.value,
        ovrigMark: this.form.controls.optobrunnOvrigMark.value,
      },
      teknikbod: {
        skog6x6m: this.form.controls.teknikbodSkog6x6.value,
        skog8x8m: this.form.controls.teknikbodSkog8x8.value,
        skog10x10m: this.form.controls.teknikbodSkog10x10.value,
        jordbruksimpediment6x6m: this.form.controls.teknikbodJordbruksimpediment6x6.value,
        jordbruksimpediment8x8m: this.form.controls.teknikbodJordbruksimpediment8x8.value,
        jordbruksimpediment10x10m: this.form.controls.teknikbodJordbruksimpediment10x10.value,
        ovrigMark6x6m: this.form.controls.teknikbodOvrigMark6x6.value,
        ovrigMark8x8m: this.form.controls.teknikbodOvrigMark8x8.value,
        ovrigMark10x10m: this.form.controls.teknikbodOvrigMark10x10.value
      },
      schablonersattning: {
        optoror1m: this.form.controls.schablon1M.value,
        optoror2m: this.form.controls.schablon2M.value
      },
      grundersattning: this.form.controls.grundersattning.value,
      minimiersattning: this.form.controls.minimiersattningstillagg.value,
      tillaggExpropriationslagen: this.form.controls.additionAsPerExpropriationslagen.value,
      sarskildErsattning: this.form.controls.specialCompensationAtAgreement.value,
      sarskildErsattningMaxbelopp: this.form.controls.sarskildErsattningMaxbelopp.value,
      minimiersattningEnbartMarkledning: this.form.controls.minimiersattningEnbartMarkledning.value
    };
    const fiberVarderingConfigNamnAgare: FiberVarderingConfigNamnAgare = {
      fiberVarderingConfig: fiberVarderingConfig,
      namn: this.form.controls.namn.value,
      personnummer: this.form.controls.personnummer.value
    };
    this.fiberVarderingConfigChange.emit(fiberVarderingConfigNamnAgare);
  }

  reset() {
    this.form.reset();
    // För att det ska hamna rätt i angulars uppdateringsloop. (Tror jag?)
    setTimeout(() => { this.form.patchValue(this.defaultFormValue) });
  }
}
