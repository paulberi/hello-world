import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {ConfigService, LayerDef, WfsSourceDef} from "../../../config/config.service";
import {MatDialog} from "@angular/material/dialog";
import {MatSliderChange} from "@angular/material/slider";
import {ActionMode, StateService} from "../../services/state.service";
import {LayerService} from "../../../map-core/layer.service";
import {
  FeatureTypeDescription,
  GeometryType,
  WfsSource
} from "../../../map-core/wfs.source";
import {AddFeatureDialogComponent, AddFeatureOptions} from "../dialogs/add-feature-dialog/add-feature-dialog.component";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogModel
} from "../dialogs/confirmation-dialog/confirmation-dialog.component";
import {StyleService} from "../../services/style.service";

/**
 * Representerar enskilda lager i lagerträdet.
 */
@Component({
  selector: "xp-layer-panel-layer",
  template: `
    <div class="main-row" cdkOverlayOrigin #trigger="cdkOverlayOrigin">
      <mat-radio-button *ngIf="type == 'radio'" color="warn" class="radio-button" [checked]="layer.visible"
                        [value]="layer" (change)="onCheckChange()" name="radio-button-layer">
      </mat-radio-button>

      <mat-checkbox *ngIf="type != 'radio'" class="checkbox" color="primary" [checked]="layer.visible"
                    (change)="onCheckChange()">
      </mat-checkbox>

      <xp-layer-panel-legend [layer]="layer"></xp-layer-panel-legend>
      <div [class]="{title: true, layerNotShown: this.layer.__zoomVisible === false}" (click)="onCheckChange()"
           (mouseenter)="description.open=openPopup()" (mouseleave)="description.open=false">
        {{layer.title}}
      </div>

      <div class="tools">
        <ng-container *ngIf="this.layerEditing && this.editableLayer">
          <mat-progress-bar mode="indeterminate" color="accent"
                            *ngIf="this.loadingFeatureDescription"></mat-progress-bar>

          <ng-container
            *ngIf="!this.loadingFeatureDescription && this.featureTypeDescription && this.featureTypeDescription != null">
            <mat-icon *ngIf="this.canClearAll" matTooltip="Ta bort alla objekt" (click)="clearAll()">clear
            </mat-icon>
            <mat-icon matTooltip="Lägg till objekt" (click)="onAddFeatureClicked()">add</mat-icon>
          </ng-container>

        </ng-container>

        <mat-icon (click)="this.showOpacity = !this.showOpacity;"
                  matTooltip="Opacitet"
                  [class.active]="showOpacity">opacity
        </mat-icon>

        <mat-icon class="error-tool" *ngIf="layer.__layerProblemLoading===true && layer.visible" color="warn"
                  matTooltip="Kan inte ladda lagret">warning
        </mat-icon>
      </div>

    </div>

    <div *ngIf="showOpacity" class="opacity-tool">
      Opacitet:
      <mat-slider [value]="layer.opacity" [min]="0" [max]="1" [step]="0.01"
                  color="primary"
                  (input)="onOpacityChange($event)"></mat-slider>
      <mat-form-field *ngIf="show_zindex">
        <input matInput [value]="layer.zIndex" type="number" maxLength="5" placeholder="Z-Index"
               (change)="onZIndexChange($event)">
      </mat-form-field>
    </div>

    <xp-popover #description [title]="layer.title"
                [description]="hasProperty(layer, 'abstract') ? layer.abstract.text : ''"
                [attribution]="hasProperty(layer, 'abstract') ? layer.abstract.attribution : ''"
                [horizontalAlign]="'before'"
                [trigger]="trigger"
    ></xp-popover>
  `,
  styles: [`
    .main-row {
      display: flex;
      justify-content: space-between;
    }

    .checkbox {
      padding-right: 5px;
    }

    .title {
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      flex: 1 1 auto;
      cursor: pointer;
    }

    .layerNotShown {
      opacity: 0.38;
    }

    /* z-index Används för att felikonen ska hamna under dom andra, det behövs så att
    det inte ska bli svårt att trycka på dom andra ikonerna för att fel-ikonen ligger
    för nära */
    .tools {
      white-space: nowrap;
      z-index: 10;
    }

    .tools .mat-icon {
      font-size: 19px;
      cursor: pointer;
    }

    .tools .error-tool {
      position: absolute;
      right: 0px;
      cursor: default;
      z-index: -1;
    }
  `]
})
export class LayerPanelLayerComponent implements OnInit {
  @Input() layer: LayerDef;
  @Input() type: string;
  @Output() layerChange = new EventEmitter<any>();

  showOpacity = false;
  loadingFeatureDescription = false;

  layerEditing = false;
  show_zindex = false;
  editableLayer = false;
  canClearAll = false;
  featureTypeDescription: FeatureTypeDescription = null;

  constructor(private stateService: StateService, private layerService: LayerService, private cdRef: ChangeDetectorRef,
              public dialog: MatDialog, public configService: ConfigService) {
    this.layerEditing = ConfigService.appConfig.layerEditing;
    this.show_zindex = <boolean>configService.getProperty("app.components.layer_panel.show_zindex", false);
  }

  ngOnInit() {
    this.editableLayer = this.layer.source.type && this.layer.source.type.toLowerCase() === "wfs" && (this.layer.hasOwnProperty("editable") ? this.layer.editable : true);
    this.canClearAll = (<WfsSourceDef>this.layer.source).canClear;
    this.loadGeometryType();
  }

  onCheckChange() {
    this.layer.visible = !this.layer.visible;
    this.layerChange.emit(this.layer);
  }

  onOpacityChange(event: MatSliderChange) {
    this.layer.opacity = event.value;
    this.layerChange.emit(this.layer);
  }

  onZIndexChange(event) {
    this.layer.zIndex = event.target.valueAsNumber;
    this.layerChange.emit(this.layer);
  }

  onAddFeatureClicked() {
    this.layer.visible = true;
    this.layerChange.emit(this.layer);

    if (this.featureTypeDescription.geometryType === GeometryType.Geometry) {
      let geometryTypes = (<WfsSourceDef>this.layer.source).customGeometryTypes;
      geometryTypes = geometryTypes == null ? [GeometryType.MapNeedle, GeometryType.Square, GeometryType.ArrowRightUp, GeometryType.ArrowLeftDown, GeometryType.Point, GeometryType.Line, GeometryType.Polygon] : geometryTypes.concat([GeometryType.MapNeedle, GeometryType.Square, GeometryType.ArrowRightUp, GeometryType.ArrowLeftDown, GeometryType.Point, GeometryType.Line, GeometryType.Polygon]);
      const dialogRef = this.dialog.open(AddFeatureDialogComponent, {
        width: "15%",
        minWidth: "450px",
        data: {
          geometryTypes: geometryTypes,
          styles: this.layer.style.styles.map((styleKey) => StyleService.createStyle(this.configService.config.styles[styleKey][0][0]))
        }
      });
      dialogRef.afterClosed().subscribe((result: AddFeatureOptions) => {
        if (result != null && result.geometryType != null) {
          this.stateService.setUiStates({
            actionMode: ActionMode.Add,
            addMode: {
              selectedLayer: this.layer,
              selectedGeometryType: result.geometryType,
              selectedGeometryName: this.featureTypeDescription.geometryName,
              selectedStyleType: result.styleType
            }
          });
        }
      });
    } else {
      this.stateService.setUiStates({
        actionMode: ActionMode.Add,
        addMode: {
          selectedLayer: this.layer,
          selectedGeometryType: this.featureTypeDescription.geometryType,
          selectedGeometryName: this.featureTypeDescription.geometryName
        }
      });
    }
  }

  clearAll() {
    const dialogData = new ConfirmationDialogModel("Ta bort alla objekt i lagret", "Vill du verkligen ta bort alla objekt i " + this.layer.title + "?", "Avbryt", "Ta bort");
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "350px",
      data: dialogData
    });
    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        let layer = this.layerService.getLayer(this.layer.id);
        (<WfsSource>layer.getSource()).removeAllFeaturesFromRemote().subscribe(result => {
          let featureInfos = this.stateService.getUiStates().valdaFeatures.filter(featureInfo => result.loadedFeatures.indexOf(featureInfo.feature) === -1);
          this.stateService.setUiStates({...{valdaFeatures: featureInfos}});
        });
      }
    });
  }

  private loadGeometryType() {
    if (!this.featureTypeDescription && this.layerEditing && this.editableLayer) {
      this.loadingFeatureDescription = true;
      let layer = this.layerService.getLayer(this.layer.id);
      if (layer.getSource() instanceof WfsSource) {
        (<WfsSource>layer.getSource()).getFeatureTypeDescription().subscribe((result) => {
          this.featureTypeDescription = result;
          this.loadingFeatureDescription = false;
          this.cdRef.detectChanges();
        });
      }
    }
  }

  hasProperty(object, property) {
    return object ? object.hasOwnProperty(property) : false;
  }

  openPopup() {
    return this.hasProperty(this.layer, "abstract");
  }
}
