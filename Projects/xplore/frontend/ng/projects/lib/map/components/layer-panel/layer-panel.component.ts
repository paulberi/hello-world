import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter, HostBinding, Input, OnDestroy,
  OnInit,
  Output, TemplateRef,
} from "@angular/core";
import {ConfigService, GroupDef, LayerDef} from "../../../config/config.service";
import {LayerService} from "../../../map-core/layer.service";
import {tap} from "rxjs/operators";

/**
 * Representerar legendpanelen som helhet.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-layer-panel",
  template: `
    <xp-collapsable-panel [icon]="'layers'" [collapsed]="collapsed" (stateUpdate)="onCollapsedStateUpdate($event)">
      <div class="layer-panel">
        <a *ngIf="logo" [href]="logo.url" target="_blank" class="logo">
          <img [src]="logo.icon" class="logo-img">
        </a>
        <mat-tab-group dynamicHeight [disablePagination]="true">
          <mat-tab label="Lager">
            <div class="layer-panel-groups" *ngFor="let group of groups; last as last">
              <ng-container *ngIf="hasLayers(group)">
                <xp-layer-panel-group [group]="group" [layers]="layers"
                                      (layerChange)="layerChange.emit($event)"></xp-layer-panel-group>
                <mat-divider *ngIf="!last"></mat-divider>
              </ng-container>
            </div>
          </mat-tab>

          <mat-tab label="Teckenförklaring">
            <ng-template matTabContent> <!-- Lazy load-->
              <ng-container *ngTemplateOutlet="specialMapKeyTemplateRef"></ng-container>
              <xp-layer-panel-map-key [layers]="layers"></xp-layer-panel-map-key>
            </ng-template>
          </mat-tab>
        </mat-tab-group>
      </div>
    </xp-collapsable-panel>
  `,
  styles: [`
    .logo {
      display: table;
      margin-left: auto;
      margin-right: auto;
      padding: 0;
      height: 64px;
      width: fit-content;
    }

    .logo a {
      display: inline-block;
    }

    .logo-img {
      max-width: 300px;
    }

    .layer-panel-groups {
      margin-right: 9px;
    }
  `]
})
export class LayerPanelComponent implements OnInit, OnDestroy {
  @Input() specialMapKeyTemplateRef: TemplateRef<any>;
  @Input() initiallyCollapsed = true;
  @Output() layerChange = new EventEmitter<any>();
  @Output() collapsedStateUpdate = new EventEmitter<boolean>();

  collapsed: boolean;
  logo: any;
  groups: GroupDef[];
  layers: LayerDef[];

  layerSubscription;

  @HostBinding("class.mat-elevation-z5") showBoxShadow = false;

  constructor(configService: ConfigService, private layerService: LayerService, private cdr: ChangeDetectorRef) {
    this.logo = configService.config.app.logo;
    this.groups = configService.config.groups;
    this.layers = configService.config.layers;
  }

  ngOnInit() {
    this.collapsed = this.initiallyCollapsed;
    this.collapsedStateUpdate.emit(this.collapsed);
    this.showBoxShadow = !this.collapsed;
    this.layerSubscription = this.layerService.getLayerChange().pipe(
      tap(() => {
        this.cdr.markForCheck();
      })
    ).subscribe();
  }

  ngOnDestroy() {
    this.layerSubscription.unsubscribe();
  }

  hasLayers(group): boolean {
    if (group.groups) {
      for (const subGroup of group.groups) {
        if (this.hasLayers(subGroup)) {
          return true;
        }
      }
    }

    const visibleLayersInGroup = this.layers.some(
      layer => layer.group === group.name && layer.__checkingAccess !== true && layer.__layerExists !== false
    );

    if (visibleLayersInGroup) {
      return true;
    }

    return false;
  }

  onCollapsedStateUpdate(collapsed: boolean): void {
    this.showBoxShadow = !collapsed;
    this.collapsed = collapsed;
    this.collapsedStateUpdate.emit(this.collapsed);
  }
}
