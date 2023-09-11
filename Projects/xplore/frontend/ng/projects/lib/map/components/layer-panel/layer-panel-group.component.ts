import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy, OnInit,
  Output,
  SimpleChanges, ViewChild
} from "@angular/core";
import {ConfigService, GroupDef, LayerDef} from "../../../config/config.service";
import {UntypedFormControl} from "@angular/forms";
import {LayerService} from "../../../map-core/layer.service";
import {Subscription} from "rxjs";
import {LoginService} from "../../../oidc/login.service";

enum GroupLayersVisible {
  All,
  None,
  Some,
  NoLayersExist
}

/**
 * Representerar en grupp av lager i lagerträdet.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-layer-panel-group",
  template: `
    <ng-template [ngIf]="showGroup">
      <div class="main-row" cdkOverlayOrigin #trigger="cdkOverlayOrigin">
        <div class="group-title" (click)="group.expanded = !group.expanded"
             (mouseenter)="openPopup() ? groupDescription.open=true : ''"
             (mouseleave)="groupDescription.open=false">
          <mat-icon>{{group.expanded ? 'expand_more' : 'chevron_right'}}</mat-icon>
          <mat-checkbox *ngIf="group.type != 'radio'" color="primary" class="group-checkbox"
                        [ngModel]="groupLayersVisibleAll()" [indeterminate]="groupLayersVisibleIndeterminate()"
                        (change)="onCheckChange()" (click)="$event.stopPropagation()">
          </mat-checkbox>
          <span [class]="{groupNotVisibleInMap: groupNotVisibleInMap}">{{group.title}}</span>
        </div>

        <div class="error-icon" *ngIf="groupHasErrors && !group.expanded">
          <mat-icon color="warn" matTooltip="Kan inte ladda något av lagren i gruppen">warning</mat-icon>
        </div>
      </div>

      <div [ngStyle]="{'display': group.expanded ? 'block': 'none'}" class="groups">

        <div *ngIf="group.filter" class="filter">
          <mat-form-field>
            <input type="text" placeholder="Lagerfilter" aria-label="Number" matInput [formControl]="filterControl">
            <button mat-button matSuffix mat-icon-button aria-label="Clear" (click)="clear()">
              <mat-icon>close</mat-icon>
            </button>
          </mat-form-field>
          <mat-icon class="hide-icon" matTooltip="Dölj avslagna lager"
                    [class.active]="this.hideDisabled" (click)="this.hideDisabled = !this.hideDisabled;">visibility_off
          </mat-icon>
        </div>

        <!-- Hantera grupper rekursivt. -->
        <xp-layer-panel-group *ngFor="let g of group.groups" [group]="g" [layers]="layers"
                              (layerChange)="onSubGroupLayerChange($event)"></xp-layer-panel-group>

        <div class="layers" *ngFor="let layer of layersInGroup">
          <xp-layer-panel-layer *ngIf="shouldLayerShow(layer)"
                                [layer]="layer" [type]="group.type"
                                (layerChange)="onLayerChange($event)">
          </xp-layer-panel-layer>
        </div>
      </div>

      <xp-popover #groupDescription [title]="group.title"
                  [description]="hasProperty(group, 'abstract') ? group.abstract.text : ''"
                  [attribution]="hasProperty(group, 'abstract') ? group.abstract.attribution : ''"
                  [horizontalAlign]="'before'"
                  [trigger]="trigger"
      ></xp-popover>

    </ng-template>
  `,
  styles: [`
    :host {
      display: block;
      margin-bottom: 5px;
    }

    .main-row {
      display: flex;
      justify-content: space-between;
    }

    .group-title {
      font-weight: bold;
      cursor: pointer;
      flex: 1 1 auto;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }

    .group-title mat-icon {
      position: relative;
      top: 5px;
    }

    .group-checkbox {
      margin-right: 5px
    }

    .groups {
      margin-left: 12px;
    }

    .layers {
      margin-left: 24px;
    }

    .filter {
      display: flex;
      justify-content: space-between;
    }

    .error-icon {
      position: absolute;
      right: 0px;
      cursor: default;
      padding-top: 9px;
    }

    .error-icon .mat-icon {
      font-size: 19px;
    }

    ::ng-deep .mat-form-field-infix {
      width: 250px;
    }

    .groupNotVisibleInMap {
      opacity: 0.38;
    }
  `
  ]
})
export class LayerPanelGroupComponent implements OnChanges, OnInit, OnDestroy {
  @Input() group: GroupDef;
  @Input() layers: LayerDef[];
  @Output() layerChange = new EventEmitter<any>();

  @ViewChild("groupDescription", {static: true}) groupDescription;

  // De lager som tillhör den aktuella gruppen.
  // Uppdateras då den totala listan ändras.
  layersInGroup: LayerDef[];

  // De lager som tillhör den aktuella gruppen och dess undergrupper.
  // Uppdateras då den totala listan ändras.
  layersInGroupAndSubGroups: LayerDef[];

  showGroup: boolean;
  groupHasErrors: boolean;

  layersVisible = GroupLayersVisible.Some;
  groupNotVisibleInMap = true;

  filterControl = new UntypedFormControl("");
  hideDisabled = false;
  private layerSubscription: Subscription;
  private filterControlSubscription: Subscription;

  constructor(private cdr: ChangeDetectorRef,
              private layerService: LayerService,
              private loginService: LoginService,
              private configService: ConfigService) {
    this.filterControlSubscription = this.filterControl.valueChanges.subscribe(() => {
      this.cdr.markForCheck();
    });
  }

  ngOnInit() {
    this.showGroup = this.showGroupFunction(this.group);
    this.groupHasErrors = this.groupHasErrorsFunction(this.group);

    this.layerSubscription = this.layerService.getLayerChange().subscribe(
      () => {
        this.showGroup = this.showGroupFunction(this.group);
        this.groupHasErrors = this.groupHasErrorsFunction(this.group);
        this.layersVisible = this.calculateLayersVisible(this.group);
        this.groupNotVisibleInMap = !this.calculateVisibleInMap(this.group);
        this.cdr.markForCheck();
      });
  }

  ngOnDestroy() {
    this.layerSubscription.unsubscribe();
    this.filterControlSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["layers"]) {
      this.layersInGroup = this.layers.filter(layer => layer.group === this.group.name).reverse();
      this.layersInGroupAndSubGroups = this.gatherLayersInGroupAndSubGroups(this.group);
    }
  }

  onLayerChange(layerDef: LayerDef) {
    if (this.group.type === "radio") {
      this.layersInGroupAndSubGroups.forEach(subLayerDef => {
        subLayerDef.visible = false;
      });
      layerDef.visible = true;

      this.layersInGroupAndSubGroups.forEach(subLayerDef => this.layerChange.emit(subLayerDef));
    } else {
      this.layerChange.emit(layerDef);
    }
  }

  setVisibilityForLayersInGroup(visible: boolean) {
    this.layersInGroup.forEach(subLayerDef => {
      subLayerDef.visible = visible;
    });

    this.layersInGroup.forEach(subLayerDef => this.layerChange.emit(subLayerDef));
  }

  onSubGroupLayerChange(event) {
    if(this.group.type === "radio") {
      this.setVisibilityForLayersInGroup(false);
    }

    this.layerChange.emit(event);
  }

  shouldLayerShow(layer) {
    if (layer.__checkingAccess === true || layer.__layerExists === false) {
      return false;
    }

    if (this.hideDisabled && !layer.visible) {
      return false;
    }

    return this.filterControl.value.length === 0 || layer.title.toLowerCase().includes(this.filterControl.value.toLowerCase());
  }

  clear() {
    this.filterControl.setValue("");
  }

  showGroupFunction(group: GroupDef) {
    if (group.groups) {
      for (const subGroup of group.groups) {
        if (this.showGroupFunction(subGroup)) {
          return true;
        }
      }
    }

    const layersInGroup = this.layers.some(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    if (layersInGroup) {
      return true;
    }

    return false;
  }

  groupHasErrorsFunction(group: GroupDef) {
    if (group.groups) {
      for (const subGroup of group.groups) {
        if (this.groupHasErrorsFunction(subGroup)) {
          return true;
        }
      }
    }

    const errorsInGroup = this.layers.some(
      layer => layer.group === group.name
        && layer.__layerProblemLoading === true
        && layer.visible === true
        && layer.__checkingAccess !== true
        && layer.__layerExists !== false
    );

    if (errorsInGroup) {
      return true;
    }

    return false;
  }


  onCheckChange() {
    switch (this.layersVisible) {
      case GroupLayersVisible.None:
      case GroupLayersVisible.Some:
        this.setLayersVisible(this.group, true);
        break;

      case GroupLayersVisible.All:
        this.setLayersVisible(this.group, false);
        break;
    }
  }

  groupLayersVisibleAll() {
    return this.layersVisible === GroupLayersVisible.All;
  }

  groupLayersVisibleIndeterminate() {
    return this.layersVisible === GroupLayersVisible.Some;
  }

  hasProperty(object, property) {
    return object ? object.hasOwnProperty(property) : false;
  }

  openPopup(): boolean {
    return this.hasProperty(this.group, "abstract");
  }

  private setLayersVisible(group: GroupDef, visible: boolean) {
    const layersInGroup = this.layers.filter(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    for (const layer of layersInGroup) {
      if (layer.visible !== visible) {
        layer.visible = visible;
        this.layerChange.emit(layer);
      }
    }

    if (group.groups) {
      for (const subGroup of group.groups) {
        this.setLayersVisible(subGroup, visible);
      }
    }
  }

  private gatherLayersInGroupAndSubGroups(group: GroupDef) {
    let subGroupLayers = group.groups?.flatMap(g => this.gatherLayersInGroupAndSubGroups(g));

    let layers = this.layers.filter(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    if (subGroupLayers) layers = layers.concat(subGroupLayers);

    return layers;
  }

  private calculateLayersVisible(group: GroupDef) {
    const layersInGroup = this.layers.filter(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    const visibleLayersInGroup = layersInGroup.filter(layer => layer.visible === true);

    let calculatedVisible: GroupLayersVisible;

    if (layersInGroup.length > 0) {
      if (layersInGroup.length === visibleLayersInGroup.length) {
        calculatedVisible = GroupLayersVisible.All;
      } else if (visibleLayersInGroup.length > 0) {
        calculatedVisible = GroupLayersVisible.Some;
      } else {
        calculatedVisible = GroupLayersVisible.None;
      }
    } else {
      calculatedVisible = GroupLayersVisible.NoLayersExist;
    }

    if (group.groups) {
      for (const subGroup of group.groups) {
        switch (this.calculateLayersVisible(subGroup)) {
          case GroupLayersVisible.All:
            if (calculatedVisible === GroupLayersVisible.NoLayersExist || calculatedVisible === GroupLayersVisible.All ) {
              calculatedVisible = GroupLayersVisible.All;
            } else if (calculatedVisible === GroupLayersVisible.None) {
              calculatedVisible = GroupLayersVisible.Some;
            }
            break;

          case GroupLayersVisible.Some:
            calculatedVisible = GroupLayersVisible.Some;
            break;

          case GroupLayersVisible.None:
            if (calculatedVisible === GroupLayersVisible.NoLayersExist || calculatedVisible === GroupLayersVisible.None) {
              calculatedVisible = GroupLayersVisible.None;
            } else if (calculatedVisible === GroupLayersVisible.All) {
              calculatedVisible = GroupLayersVisible.Some;
            }
            break;
          case GroupLayersVisible.NoLayersExist:
            // Ignore group
            break;
        }
      }
    }

    return calculatedVisible;
  }
  private calculateVisibleInMap(group: GroupDef) {
    const layersInGroup = this.layers.filter(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    const visibleLayersInGroup = layersInGroup.some(layer => layer.__zoomVisible == null || layer.__zoomVisible === true);

    if (visibleLayersInGroup) {
      return true;
    }

    if (group.groups) {
      if (group.groups.some(g => this.calculateVisibleInMap(g))) {
        return true;
      }
    }

    return false;
  }
}
