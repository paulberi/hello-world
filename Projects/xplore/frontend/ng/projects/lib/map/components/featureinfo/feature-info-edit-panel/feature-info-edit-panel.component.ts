import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input, OnDestroy,
  OnInit,
  Output
} from "@angular/core";

import {StateService, StyleType} from "../../../services/state.service";
import {FeatureInfoDisplayObject, TemplateProperty} from "../feature-info-panel.component";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogModel
} from "../../dialogs/confirmation-dialog/confirmation-dialog.component";
import {EditService} from "../../../services/map-tools/edit.service";
import {GeometryType, WfsSource} from "../../../../map-core/wfs.source";
import VectorSource from "ol/source/Vector";

@Component({
  selector: "xp-feature-info-edit-panel",
  changeDetection: ChangeDetectionStrategy.Default,
  templateUrl: "./feature-info-edit-panel.component.html",
  styleUrls: ["./feature-info-edit-panel.component.scss"],
})
export class FeatureInfoEditPanelComponent implements OnInit, OnDestroy {
  @Input() fido: FeatureInfoDisplayObject;
  @Output() updated = new EventEmitter<void>();

  editForm = new UntypedFormGroup({});
  syncing: boolean;
  syncError: boolean;

  constructor(private cdr: ChangeDetectorRef, public stateService: StateService, public dialog: MatDialog,
              private editService: EditService) {
  }

  ngOnInit() {
    this.fido.properties.forEach(prop => {
      const validators: ValidatorFn[] = [];
      if (prop.minLength) {
        validators.push(Validators.required);
        validators.push(Validators.minLength(prop.minLength));
      }
      if (prop.maxLength) {
        validators.push(Validators.maxLength(prop.maxLength));
      }

      if (prop.type === "style") {
        const style = JSON.parse(this.fido.featureInfo.feature.get(prop.name));
        if (style.type === "Text") {
          this.editForm.addControl(prop.name, new UntypedFormControl(style.style.text.text, validators));
        }
      } else {
          this.editForm.addControl(prop.name, new UntypedFormControl(this.fido.featureInfo.feature.get(prop.name), validators));
      }
    });

    this.editForm.valueChanges.subscribe(values => {
      for (const name in values) {
        if (values.hasOwnProperty(name)) {
          const property = this.fido.properties.find(prop => prop.name === name);
          if (property && property.type === "style") {
            const styleType: StyleType = JSON.parse(this.fido.featureInfo.feature.get(name));
            if (styleType.type === GeometryType.Text) {
              styleType.style["text"].text = values[name];
              this.fido.featureInfo.feature.set(name, JSON.stringify(styleType));
            }
          } else {
            this.fido.featureInfo.feature.set(name, values[name]);
          }
        }
      }
    });

    this.startEditing();
  }

  ngOnDestroy(): void {
    const layer = this.fido.featureInfo.layer;
    const foundFeature = (<VectorSource> layer.getSource()).getFeatures()
      .filter(feature => feature.getId() === this.fido.featureInfo.feature.getId());

    // Om ingen feature hittas innebär det att den tagits bort, så redigeringsläget
    // behöver stängas av annars ligger redigeringsbrytpunkter kvar i kartan
    if (foundFeature.length === 0) {
      this.stopEditing(false, false);
    }
  }

  private startEditing() {
    this.editService.startEdit(this.fido.featureInfo);
    this.fido.featureInfo.feature.getGeometry().on("change", () => {
      this.cdr.markForCheck();
    });
  }

  private stopEditing(revert: boolean, update?: boolean) {
    if (revert) {
      (<any> this.fido.featureInfo.feature).revert();
    } else {
      (<any> this.fido.featureInfo.feature).finishEdit();
    }
    this.editService.finishEdit((<any> this.fido.featureInfo.feature).ol_uid);

    if (update) {
      this.updateSelectedFeatures();
    }

    this.updated.emit();
  }

  private startSyncing() {
    this.syncing = true;
    this.syncError = false;
    this.editForm.disable();
  }

  private stopSyncing(error: boolean) {
    this.syncing = false;
    this.syncError = error;
    this.editForm.enable();
  }

  save() {
    this.startSyncing();

    (<WfsSource> this.fido.featureInfo.layer.getSource()).syncWithRemote([this.fido.featureInfo.feature]).subscribe(() => {
        this.stopSyncing(false);
        this.stopEditing(false);
      },
      error => {
        this.stopSyncing(true);
      }
    );
  }

  remove() {
    this.showConfirmationDialog(
      "remove",
      "Radera objekt",
      "Är du säker på att du vill radera objektet?<p>Operationen går inte att ångra.",
      "Avbryt",
      "Radera"
    );
  }

  abort() {
    if ((<any> this.fido.featureInfo.feature).hasChanged()) {
      this.showConfirmationDialog(
        "abort",
        "Ångra redigering",
        "Är du säker på att du vill ångra redigeringen?<p>Alla gjorda förändringar kommer gå förlorade.",
        "Fortsätt redigera",
        "Ångra"
      );
    } else {
      this.stopEditing(true);
    }
  }

  showConfirmationDialog(action: string, title: string, message: string, dismissLabel: string, confirmLabel: string): void {
    const dialogData = new ConfirmationDialogModel(title, message, dismissLabel, confirmLabel);
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "300px",
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult) {
        if (action === "remove") {
          this.startSyncing();

          (<WfsSource> this.fido.featureInfo.layer.getSource()).removeFromRemote([this.fido.featureInfo.feature]).subscribe(() => {
              this.stopSyncing(false);
              this.stopEditing(false, true);
            },
            error => {
              this.stopSyncing(true);
            });
        }

        if (action === "abort") {
          this.stopEditing(true);
        }
      }
    });
  }

  getValidationErrorMessage(prop: TemplateProperty) {
    const control: AbstractControl = this.editForm.get(prop.name);
    if (control.hasError("required")) {
      return "Fältet får inte vara tomt";
    } else if (control.hasError("minlength")) {
      return "Fältet innehåller för få tecken (< " + prop.minLength + " st)";
    } else if (control.hasError("maxlength")) {
      return "Fältet innehåller för många tecken (> " + prop.maxLength + " st)";
    } else if (control.hasError("pattern")) {
      return "Fältet har otillåtet innehåll eller struktur";
    }

    return "Odefinerat fel i fältinmatning";
  }

  getProperties(): TemplateProperty[] {
    return this.fido.properties.filter((prop) => {
      if (prop.type == 'style') {
        let styleType: StyleType = JSON.parse(this.fido.featureInfo.feature.get(prop.name));
        return styleType.type == GeometryType.Text;
      }
      return true;
    });
  }

  getNumberOfRows(property: TemplateProperty) {
    const value = this.fido.featureInfo.feature.get(property.name);
    return value ? (value.match(/\n/g) || "").length + 1 : 1;
  }

  public getTitleProperty(fido: FeatureInfoDisplayObject): TemplateProperty {
    return fido.properties.find(p => p.type === "title");
  }

  private updateSelectedFeatures() {
    const updatedSelection = this.stateService.getUiStates().valdaFeatures
      .filter(item => item.feature.getId() !== this.fido.featureInfo.feature.getId());
    this.stateService.setUiStates({valdaFeatures: updatedSelection});
  }

  isSaveNotPossible() {
    return this.getProperties().length !== 0 ?
      ((!(<any>this.fido.featureInfo.feature).hasChanged()
      && (<any> this.fido.featureInfo.feature).isPersisted()) || !this.editForm.valid || this.syncing)
      : this.syncing;
  }
}

