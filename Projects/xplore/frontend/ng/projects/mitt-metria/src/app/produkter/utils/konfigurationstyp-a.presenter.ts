import { EventEmitter } from "@angular/core";
import { FormControl, FormControlStatus, FormGroup, Validators } from "@angular/forms";
import { GeoJson } from "../../../../../lib/map-core/geojson.util";
import { KonfigurationstypAFormControl } from "../ui/konfigurationstyp-a-ui/konfigurationstyp-a-ui.component";

export interface AttributeFormResult {
    omrade: any;
    refsys: string;
    format: string;
}

export class KonfigurationstypAPresenter {
    fileChanges = new EventEmitter<GeoJson[]>();
    selectChanges = new EventEmitter<KonfigurationstypAFormControl>();
    attributeFormChanges = new EventEmitter<AttributeFormResult>();
    statusChanges = new EventEmitter<FormControlStatus>();

    private _konfigurationstypAForm: FormGroup;
    get konfigurationstypAForm(): FormGroup {
        return this._konfigurationstypAForm;
    }
    get areaForm(): FormGroup {
        return this._konfigurationstypAForm.get("areaForm") as FormGroup;
    }
    get attributeForm(): FormGroup {
        return this._konfigurationstypAForm.get("attributeForm") as FormGroup;
    }

    public initializeKonfigurationForm() {
        this._konfigurationstypAForm = new FormGroup({
            areaForm: new FormGroup({
                file: new FormControl(null),
                select: new FormControl(null)
            }),
            attributeForm: new FormGroup({
                omrade: new FormControl(null, [Validators.required]),
                refsys: new FormControl(null, [Validators.required]),
                format: new FormControl(null, [Validators.required])
            })
        });

        this.selectFormControlChanges();
        this.fileFormControlChanges();
        this.attributeForm.statusChanges.subscribe(status => this.statusChanges.emit(status));
        this.attributeForm.valueChanges.subscribe(value => this.attributeFormChanges.emit(value));
    }

    private selectFormControlChanges() {
        this.areaForm.get("select").valueChanges.subscribe(value => {
            this.areaForm.get("file").reset(null, { emitEvent: false });

            if (this.checkIfFormHasValue(this.attributeForm)) {
                this.attributeForm.reset();
                this.attributeForm.get("omrade").setValue(null, {
                    emitModelToViewChange: true
                });
                this.attributeForm.get("omrade").setValue(null);
            }
            this.selectChanges.emit(value);
        })
    }

    private fileFormControlChanges() {
        this.areaForm.get("file").valueChanges.subscribe(value => {
            this.areaForm.get("select").reset(null, { emitEvent: false });

            if (this.checkIfFormHasValue(this.attributeForm)) {
                this.attributeForm.reset();
            }

            this.attributeForm.get("omrade").setValue([value]);
            this.fileChanges.emit([value]);
        })
    }

    private checkIfFormHasValue(form: FormGroup): boolean {
        let formValues = Array.from(Object.values(form.value));
        const hasValues = formValues.some(value => value);
        return hasValues;
    }
}