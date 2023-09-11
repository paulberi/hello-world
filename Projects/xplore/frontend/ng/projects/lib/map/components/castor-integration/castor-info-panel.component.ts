import {Component, HostBinding, Injectable, Input, OnDestroy, OnInit} from "@angular/core";
import {CastorIntegrationService, MyCastorSelectionListItem} from "../../services/castor-integration.service";
import {SelectionService} from "../../services/selection.service";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {Delomrade} from "../featureinfo/fastighet/fsokpanel/fsok-panel.component";
import {ConfigService} from "../../../config/config.service";
import {MapService} from "../../../map-core/map.service";

@Component({
  selector: "xp-castor-info-panel",
  template: `
    <xp-collapsable-panel [icon]="'swap_horiz'" [collapsed]="collapsed"
                          (stateUpdate)="showBoxShadow = !$event">
      <div class="xp-sub-panel-content">
        <h3 style="margin-bottom: 0">CASTOR</h3>
        <div style="margin: 10px">

          <mat-slide-toggle matTooltip="Hämta objelt från CASTOR" aria-label="Hämta objelt från CASTOR"
            [checked]="checked" (change)="showToggle($event)">
            {{toggleText}}
          </mat-slide-toggle>

            <div *ngIf="checked">
            <span *ngFor="let listItem of this.cSelectionList">
              <span *ngFor="let x of listItem.selectionList">
                <span class="zoom-button" (click)="onZoom($event, x.uuid)">
                  <mat-icon class="zoom-button zoom-button-icon toolbar-button">zoom_in</mat-icon>
                </span>
                <span>{{x.beteckning}} {{" (" + listItem.name + ")" }}<br></span>
              </span>
            </span>
            </div>
            <mat-divider></mat-divider>
            <div class="castor" id="castor">
              <div *ngIf="this.castorIntegrationService.tempRealEstateList.length > 0">
                  <span> {{"Antal poster = " }}{{castorIntegrationService.tempRealEstateList.length}} <br>
                  </span>
                  <div *ngFor="let x of this.castorIntegrationService.tempRealEstateList">
                    <span class="zoom-button" (click)="onZoom($event, x.uuid)">
                      <mat-icon class="zoom-button zoom-button-icon toolbar-button">zoom_in</mat-icon>
                    </span>
                    <span>
                        {{x.beteckning}} <br>
                    </span>
                  </div>
              </div>
              <div *ngIf="this.castorIntegrationService.tempRealEstateList.length === 0">
                <span> {{"Inga poster finns i det temporära urvalet!"}} <br></span>
              </div>
            </div>

          <div>
            <mat-form-field>
              <input type="text" placeholder="Namnge försendelsen" matInput [(ngModel)]="tagName"
                     name="tagName">
            </mat-form-field>
            <button style="float: left;" mat-stroked-button (click)="rensaUrvalet()">Rensa urvalet</button>
            <button style="float: right;" mat-stroked-button (click)="skickaTillCastor()">Skicka till Castor</button>
            <div style="clear: both"></div>
          </div>
        </div>
      </div>
    </xp-collapsable-panel>
  `,
  styles: [`
    .castor {
      font-size: 12px;
      color: #202020;
    }
    .zoom-button {
      cursor: pointer;
      color: #858582;
    }

    .zoom-button-icon {
      position: relative;
      top: 8px;
    }
  `]
})

export class CastorInfoPanelComponent implements OnInit, OnDestroy {

  @Input() delomraden: Delomrade[];

  public connected = false;
  checked = false;
  tagName = "";
  realEstateList: string = "Inga poster finns i det temporära urvalet!";

  public cSelectionList: MyCastorSelectionListItem[];

  @HostBinding("class.mat-elevation-z5") showBoxShadow = false;

  toggleText = "Visa objektlistan från CASTOR";

  collapsed = false;

  constructor(private mapService: MapService, private configService: ConfigService,
              private selectionService: SelectionService, public castorIntegrationService: CastorIntegrationService) {
  }

  ngOnInit(): void {
    // Collapse the panel by default on small screens
    this.collapsed = window.innerWidth <= 660;
    this.showBoxShadow = !this.collapsed;
  }

  ngOnDestroy() {
  }

  showToggle($event: MatSlideToggleChange) {
    if ($event.checked) {
      this.checked = true;
      this.toggleText = "Dölj objektlistan från CASTOR";
      this.cSelectionList = this.castorIntegrationService.getCastorSelectionList();
    } else {
      this.cSelectionList = [];
      this.checked = false;
      this.hideToggle();
    }
  }

  hideToggle() {
    this.toggleText = "Visa objektlistan från CASTOR";
  }

  rensaUrvalet() {
    this.castorIntegrationService.cleanTempRealEstateList();
    this.tagName = "";
    this.realEstateList = "Inga poster finns i det temporära urvalet!";
  }

  skickaTillCastor() {
    this.castorIntegrationService.sendToCastor(this.tagName);
    this.rensaUrvalet()
  }

  onZoom(event: Event, uuid: string) {
    event.stopPropagation();
    this.selectionService.setKomplettFastighet([uuid], true);
  }
}
