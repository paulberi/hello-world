import {Injectable} from "@angular/core";
import {forkJoin, Observable, of, Subject} from "rxjs";
import {map} from "rxjs/operators";
import {SelectionService} from "../../../../lib/map/services/selection.service";
import {StateService} from "../../../../lib/map/services/state.service";
import {Place, SokService} from "../../../../lib/map/services/sok.service";
import {transform} from "ol/proj";
import {get as getProjection} from "ol/proj";
import {ConfigService} from "../../../../lib/config/config.service";
import {ViewService} from "../../../../lib/map-core/view.service";
import {Coordinate} from "ol/coordinate";

// Behövs för att kompilera jQuery/Signalr i Stor
declare var $: any;

export interface EdpCoordinate {
  Northing: string;
  Easting: string;
  SpatialReferenceSystemIdentifier: string;
  Label: string;
}

export interface RealEstateIdentifier {
  Uuid?: string;
  Municipality?: string;
  Name?: string;
}

@Injectable({
  providedIn: "root"
})
export class EdpIntegrationService {
  private edpHubProxy;
  private edpConnection: any; // SignalR.Hub.Connection
  private edpConnectionStatus = new Subject<boolean>();
  private readonly projectionCode: string;

  public markCoordinates = new Subject<boolean>();

  constructor(private selectionService: SelectionService,
              private stateService: StateService,
              private configService: ConfigService,
              private viewService: ViewService,
              private sokService: SokService) {
    this.projectionCode = this.configService.config.projectionCode.replace("EPSG:", "");
  }

  getStatus(): Observable<boolean> {
    return this.edpConnectionStatus.asObservable();
  }

  public startConnection(serverUrl: string, username: string, organisation: string) {
    if (this.edpConnection) {
      this.stopConnection();
    }

    return new Observable(subscriber => {
      try {
        this.edpConnection = $.hubConnection(serverUrl);

        console.error("EDP connection", this.edpConnection);

        this.edpConnection.qs = {
          "user": username,
          "organisation": organisation,
          "client": "WebMap",
          "clientType": "External",

        };

        this.edpHubProxy = this.edpConnection.createHubProxy("GisAndRealEstate");

        this.edpHubProxy.on("HandleRealEstateIdentifiers", msg => {
          this.handleRealEstateIdentifiers(msg);
        });

        this.edpHubProxy.on("HandleCoordinates", msg => {
          this.handleCoordinates(msg);
        });

        this.edpHubProxy.on("HandleAskingForRealEstateIdentifiers", _ => {
          this.handleAskingForRealEstateIdentifiers();
        });

        this.edpHubProxy.on("HandleAskingForCoordinates", _ => {
          this.handleAskingForCoordinates();
        });

        this.edpConnection.logging = true;

        this.edpConnection.disconnected(() => {
          this.edpConnectionStatus.next(false);
        });


        this.edpConnection.start().done( _ => {
          subscriber.next();
          subscriber.complete();
          this.edpConnectionStatus.next(true);
        })
          .fail( _ => {
            subscriber.error("EDP connection failed");
            console.error("EDP connection failed");
          });
      } catch (err) {
        subscriber.error(err);
      }
    });
  }

  public handleRealEstateIdentifiers(realEstateIdentifiers: RealEstateIdentifier[]) {
    const uuidRequest: Observable<string>[] = [];
    for (const realEstateIdentifier of realEstateIdentifiers) {
      if (realEstateIdentifier.Uuid) {
        uuidRequest.push(of(realEstateIdentifier.Uuid));
      } else {
        const beteckning = realEstateIdentifier.Municipality + " " + realEstateIdentifier.Name;

        uuidRequest.push(this.sokService.findFastighet(beteckning).pipe(map(places => {
          return places[0].feature.properties.OBJEKT_ID;
        })));
      }
    }

    forkJoin(uuidRequest).subscribe(uuidArray => {
      this.selectionService.setKomplettFastighet(uuidArray, true);
    });
  }

  public handleCoordinates(msgArray: EdpCoordinate[]) {
    const places: Place[] = [];

    for (const msg of msgArray) {
      console.error("handleCoordinates message", msg);

      const inputProjection = getProjection("EPSG:" + msg.SpatialReferenceSystemIdentifier);

      if (!inputProjection) {
        alert("EDP integration: projection not supported (not found in configuration): " + msg.SpatialReferenceSystemIdentifier);
        return;
      }

      const cord = [Number(msg.Easting), Number(msg.Northing)];

      const transformedLocation = transform(cord,
        inputProjection,
        this.configService.config.projectionCode,
      );

      const place: Place = {
        coordinate: transformedLocation, name: msg.Label, type: "edpCoordinate"
      };

      places.push(place);
    }

    this.selectionService.setPlaces(places, true);
  }

  /**
   * @param testRun Decides if the result will be printed to console instead of sent back to EDP.
   */
  public handleAskingForRealEstateIdentifiers(testRun: boolean = false) {
    const valdaDelomaden = this.stateService.getUiStates().valdaDelomraden;

    const answer = [];

    // Todo; Hantera delområden, kan det vara flera per fastighet?

    const objektHanterat = new Set<string>();

    if (valdaDelomaden.length > 0) {
      for (const delOmr of valdaDelomaden) {
        const objektId = delOmr.properties.objekt_id;

        if (!objektHanterat.has(objektId)) {
          answer.push({
                        Uuid: delOmr.properties.objekt_id,
                        Municipality: delOmr.properties.kommunnamn,
                        Name: delOmr.properties.trakt + " " + delOmr.properties.blockenhet
          });

          objektHanterat.add(objektId);
        }
      }
    }

    if (testRun) {
      this.testRunPrintOut(answer);
    } else {
      this.sendRealEstateIdentifiersToEdp(answer);
    }
  }

  /**
   * @param testRun Decides if the result will be printed to console instead of sent back to EDP.
   */
  public handleAskingForCoordinates(testRun: boolean = false) {
    this.markCoordinates.next(testRun);
  }

  public convertCoordinateToEdpCoordinate(coordinate: Coordinate, label: string): EdpCoordinate {
    return {
      Northing: coordinate[1].toString(),
      Easting: coordinate[0].toString(),
      SpatialReferenceSystemIdentifier: this.projectionCode,
      Label: label
    };
  }

  public testRunPrintOut(list: any[]) {
    console.log("### Test run result ###");
    list.forEach(item => {
      console.log(item);
    });
  }

  public sendCoordinatesToEdp(payload: EdpCoordinate[]) {
    this.invokeEdpMethod("SendCoordinates", payload);
  }

  private sendRealEstateIdentifiersToEdp(payload: any[]) {
    this.invokeEdpMethod("SendRealEstateIdentifiers", payload);
  }

  private invokeEdpMethod(name: string, payload: any) {
    // console.log(`Sending the following payload to ${name}: \n ${payload}`);
    this.edpHubProxy.invoke(name, payload)
      .done(_ => {
        console.log(`${name} done`);
      })
      .fail(e => {
        console.log(`Failed to invoke ${name}: ${e}`);
      });
  }

  public stopConnection() {
    if (this.edpConnection) {
      this.edpConnection.stop();
      this.edpConnection = undefined;
    }
  }
}
