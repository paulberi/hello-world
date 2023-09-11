import {Component, EventEmitter, HostBinding, OnDestroy, OnInit} from "@angular/core";
import {EdpCoordinate, EdpIntegrationService} from "../services/edp-integration.service";
import {MarkedCoordinate} from "./edp-mark-coordinates.component";
import {Place} from "../../../../lib/map/services/sok.service";
import {SelectionService} from "../../../../lib/map/services/selection.service";
import {Subscription} from "rxjs";
import {first, timeout} from "rxjs/operators";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

class EdpConnectionInfo {
  public username;
  public serverUrl;
  public organisation;
}

@Component({
  selector: "xp-edp-info-panel",
  template: `
    <xp-collapsable-panel [icon]="'extension'" [collapsed]="collapsed"
                          (stateUpdate)="showBoxShadow = !$event">
      <div class="xp-sub-panel-content">
        <h3 style="margin-bottom: 0">EDP</h3>
        <div style="margin: 10px">
          <mat-slide-toggle matTooltip="Anslut eller koppla ifrån EDP" aria-label="Anslut eller koppla ifrån EDP"
            [checked]="checked" (change)="connectionToggle($event)">
            {{toggleText}}
          </mat-slide-toggle>

<!--          <div>-->
<!--            <button mat-stroked-button class="okButton" (click)="edpFakeCoordinates()"-->
<!--                    style="margin-left: 10px">Fejka coordinater-->
<!--            </button>-->
<!--            <button mat-stroked-button class="okButton" (click)="edpFakeFastigheter()"-->
<!--                    style="margin-left: 10px">Fejka fastigheter-->
<!--            </button>-->
<!--            <button mat-stroked-button class="okButton" (click)="edpFakeCoordinateRequest()">Fejka koordinat begäran</button>-->
<!--            <button mat-stroked-button class="okButton" (click)="edpFakeFastigheterRequest()"-->
<!--                    style="margin-left: 10px">Fejka fastighets begäran-->
<!--            </button>-->
<!--          </div>-->

        <div *ngIf="!connected && connecting">
          <div>
            <mat-form-field class="full-width">
              <input type="text" placeholder="Användarnamn" matInput [(ngModel)]="edpConnectionInfo.username"
                     name="username">
            </mat-form-field>
          </div>

          <div>
            <mat-form-field class="full-width">
              <input type="text" placeholder="Server URL" matInput [(ngModel)]="edpConnectionInfo.serverUrl"
                     name="serverUrl">
            </mat-form-field>
          </div>

          <div>
            <mat-form-field class="full-width">
              <input type="text" placeholder="Organisation" matInput [(ngModel)]="edpConnectionInfo.organisation"
                     name="organisation">
            </mat-form-field>
          </div>

            <button style="float: right;" mat-stroked-button (click)="edpLogIn()">Anslut</button>
            <button style="float: right;" mat-button (click)="abortConnection()">Avbryt</button>
            <div style="clear: both"></div>
          </div>

          <div *ngIf="showMarkCoordinate" style="margin-top: 5px">
              <xp-edp-mark-coordinates [resultEvent]="markedCoordinateEvent"></xp-edp-mark-coordinates>
          </div>
        </div>
      </div>
    </xp-collapsable-panel>
  `,
  styles: [`
  `]
})
export class EdpInfoPanelComponent implements OnInit, OnDestroy {
  public connected = false;
  public connecting = false;
  public visible = false;
  public showMarkCoordinate = false;

  public edpConnectionInfo: EdpConnectionInfo;
  private markCoordinates$: Subscription;

  public markedCoordinateEvent = new EventEmitter<MarkedCoordinate[]>();
  private subscription: Subscription;

  checked = false;

  private testRun = false;
  toggleText = "Anslut till EDP";

  collapsed = false;
  @HostBinding("class.mat-elevation-z5") showBoxShadow = false;

  constructor(private selectionService: SelectionService,
              private edpIntegrationService: EdpIntegrationService) {

    this.markCoordinates$ = this.edpIntegrationService.markCoordinates.subscribe(
      testRun => {
        this.testRun = testRun;
        this.showMarkCoordinate = true;

        this.subscription = this.markedCoordinateEvent
          .pipe(first<MarkedCoordinate[]>())
          .pipe(timeout(120000))
          .subscribe( coordinates => {
            const edpCoordinates = coordinates.map(val => this.convertToEdpCoordinate(val));
            this.coordinateResponse(testRun, edpCoordinates);

            this.showMarkCoordinate = false;
          },
          error => {
            // Timeout
            // console.log(error);

            this.coordinateResponse(testRun, []);
            this.showMarkCoordinate = false;
          }
        );
      }
    );
  }

  connectionToggle($event: MatSlideToggleChange) {
    if ($event.checked) {
      this.connecting = true;
      this.checked = true;
    } else {
      this.checked = false;
      this.edpLogOut();
    }
  }

  private coordinateResponse(testRun: boolean, payload: EdpCoordinate[]) {
    if (testRun) {
      this.edpIntegrationService.testRunPrintOut(payload);
    } else {
      this.edpIntegrationService.sendCoordinatesToEdp(payload);
    }
  }

  private convertToEdpCoordinate(markedCoordinate: MarkedCoordinate) {
    return this.edpIntegrationService.convertCoordinateToEdpCoordinate(markedCoordinate.coordinate, markedCoordinate.label);
  }

  ngOnInit(): void {
    // Collapse the panel by default on small screens
    this.collapsed = window.innerWidth <= 660;
    this.showBoxShadow = !this.collapsed;

    const oldConnectionInfo = localStorage.getItem("EdpConnectionInfo");

    if (oldConnectionInfo) {
      this.edpConnectionInfo = JSON.parse(oldConnectionInfo);
    } else {
      this.edpConnectionInfo = new EdpConnectionInfo();
    }

    this.edpIntegrationService.getStatus().subscribe(value => {
      this.connected = value;
    });
  }

  ngOnDestroy() {
    if (!this.markCoordinates$.closed) {
      this.markCoordinates$.unsubscribe();
    }
  }

  edpLogIn() {
    this.edpIntegrationService.startConnection(this.edpConnectionInfo.serverUrl,
      this.edpConnectionInfo.username,
      this.edpConnectionInfo.organisation)
      .subscribe(res => {
        this.connecting = false;
        localStorage.setItem("EdpConnectionInfo", JSON.stringify(this.edpConnectionInfo));
        this.toggleText = `Ansluten som ${this.edpConnectionInfo.username}`;
      }, error => {
        alert("Kan inte ansluta till EDP");
      });
  }

  edpLogOut() {
    this.edpIntegrationService.stopConnection();
    this.connecting = false;
    this.toggleText = "Anslut till EDP";
  }

  toggleShowMarkCoordinates() {
    this.showMarkCoordinate = !this.showMarkCoordinate;
  }

  cancel() {
    this.subscription.unsubscribe();
    this.coordinateResponse(this.testRun, []);
  }

  markCoordinatesInMap(coordinates: MarkedCoordinate[]) {
    this.toggleShowMarkCoordinates();
    const places: Place[] = coordinates.map<Place>(coord => ({
      coordinate: coord.coordinate, name: coord.label, type: "edpCoordinate"
    }));

    this.selectionService.setPlaces(places, true);
  }

  edpFakeCoordinates() {
    const fakeData = {
      Northing: "6167373",
      Easting: "387272",
      SpatialReferenceSystemIdentifier: "3006",
      Label: "En punkt"
    };

    const fakeData2 = {
      Northing: "6166979",
      Easting: "387513",
      SpatialReferenceSystemIdentifier: "3006",
      Label: "En punkt till"
    };

    // const fakeData = {
    //   Northing: "6168986.320173",
    //   Easting: "132066.370978",
    //   SpatialReferenceSystemIdentifier: "3008",
    //   Label: "En punkt"
    // };
    //
    // const fakeData2 = {
    //   Northing: "6167986.320173",
    //   Easting: "132066.370978",
    //   SpatialReferenceSystemIdentifier: "3008",
    //   Label: "En punkt till"
    // };

    this.edpIntegrationService.handleCoordinates([fakeData, fakeData2]);
  }

  edpFakeFastigheter() {
    const fakeData1 = {
      Uuid: "909a6a61-1c5b-90ec-e040-ed8f66444c3f",
      Municipality: "Staffanstorp",
      Name: "STANSTORP 1:177"
    };

    const fakeData2 = {
      // Uuid: "909a6a61-1c5b-90ec-e040-ed8f66444c3f",
      Municipality: "Staffanstorp",
      Name: "STANSTORP 1:176"
    };


    this.edpIntegrationService.handleRealEstateIdentifiers([fakeData1, fakeData2]);
  }

  edpFakeCoordinateRequest() {
    this.edpIntegrationService.handleAskingForCoordinates(true);
  }

  edpFakeFastigheterRequest() {
    this.edpIntegrationService.handleAskingForRealEstateIdentifiers(true);
  }

  abortConnection() {
    this.connecting = false;
    this.checked = false;
  }
}
