import {Component, HostBinding, Input, OnInit, ViewChild} from "@angular/core";

import {NmdResponse, NmdService} from "../../services/nmd.service";
import {ActionMode, FeatureSelectionMode, StateService} from "../../../../../lib/map/services/state.service";
import {HuggningsklasserResponse, HuggningsklasserService} from "../../services/huggningsklasser.service";
import {SkogligaGrunddataResponse, SkogligaGrunddataService} from "../../services/skogliga-grunddata.service";
import WKT from "ol/format/WKT";
import MultiPolygon from "ol/geom/MultiPolygon";
import {Observable, of} from "rxjs";
import {catchError, tap} from "rxjs/operators";
import {AvverkningsstatistikResponse, AvverkningsstatistikService} from "../../services/avverkningsstatistik.service";
import {SkogligRequest} from "../../model/skoglig-request.model";
import {SkogligService} from "../../services/skoglig.service";
import {SkyddadeOmradenResponse, SkyddadeOmradenService} from "../../services/skyddade-omraden.service";
import {extend, Extent, getSize} from "ol/extent";

export interface SkogligtFel {
  status: number;
  felkod: string;
  felbeskrivning: string;
}

@Component({
  selector: "xp-analysis-report",
  templateUrl: "./analysis-report.component.html",
  styleUrls: ["./analysis-report.component.css"],
})
export class AnalysisReportComponent implements OnInit {
  dataSource = groups;

  @Input() skogligRequest: SkogligRequest;

  private WKT: WKT = new WKT();

  @ViewChild("collapsablePanel", { static: true }) collapsablePanel;

  constructor(public stateService: StateService, private skogligaGrunddataService: SkogligaGrunddataService,
              private skogligService: SkogligService,
              private nmdService: NmdService, private avverkningsstatistikService: AvverkningsstatistikService,
              private huggningsklasserService: HuggningsklasserService,
              private skyddadeOmradenService: SkyddadeOmradenService) {
  }

  ngOnInit() {
    // make sure that the state is set before analysis
    this.stateService.partialUiStates.subscribe(uiStates => {
      if (uiStates.valdaDelomraden && uiStates.valdaDelomraden.length) {
        for (const group of groups) {
          this.analyze(group);
        }
      }
    });
  }

  // Request an analysis from a specific service, depending on the group/component
  public analyze(group: Group) {
    if (!this.validateComplexity()) {
      this.analysisDone(group, {status: 400, error: "COMPLEX_GEOMETRY"});
      return;
    }

    if (!this.validateArea()) {
      this.analysisDone(group, {status: 400, error: "HUGE_GEOMETRY"});
      return;
    }

    if (!this.validBoundingBox()) {
      this.analysisDone(group, {status: 400, error: "FAR_APART"});
      return;
    }

    // If information on this property has already been fetched, just expand/close the tab
    if (group.containsData) {
      group.expanded = !group.expanded;
      return;
    }

    switch (group.component) {
      case "skogliga-grunddata":
        this.getSkogligaGrunddata();
        break;
      case "nmd":
        this.getNmd();
        break;
      case "huggningsklasser":
        this.getHuggningsklasser();
        break;
      case "avverkningsstatistik":
        this.getAvverkningsstatistik();
        break;
      case "skyddade-omraden":
        this.getSkyddadeOmraden();
        break;
      default: {
        group.expanded = !group.expanded;
        break;
      }
    }
  }

  public reanalyze(group: Group) {
    for (let i = 0; i < this.dataSource.length; i++) {
      const ds = this.dataSource[i];
      if (group.component === ds.component) {
        ds.expanded = false;
        ds.isLoading = false;
        ds.containsData = false;
        ds.error = null;
        ds.result = of(null);

        break;
      }
    }

    this.analyze(group);
  }

  private getSelectedFeatures(): any[] {
    return this.stateService.getUiStates().valdaDelomraden;
  }



  private getSkogligaGrunddata() {
    const group = groups[0];
    this.analysisStarted(group);

    group.result = this.skogligaGrunddataService.get(this.skogligRequest).pipe(
      tap((resp) => {
        this.analysisDone(group);
      }),
      catchError(response => {
        this.analysisDone(group, response);
        return of(undefined);
      })
    );
  }

  private getNmd() {
    const group = groups[1];
    this.analysisStarted(group);

    group.result = this.nmdService.get(this.skogligRequest).pipe(
      tap((resp) => {
        this.analysisDone(group);
      }),
      catchError(response => {
        this.analysisDone(group, response);
        return of(undefined);
      })
    );
  }

  private getHuggningsklasser() {
    const group = groups[3];
    this.analysisStarted(group);

    group.result = this.huggningsklasserService.get(this.skogligRequest).pipe(
      tap((resp) => {
        this.analysisDone(group);
      }),
      catchError(response => {
        this.analysisDone(group, response);
        return of(undefined);
      })
    );
  }


  private getAvverkningsstatistik() {
    const group = groups[2];
    this.analysisStarted(group);

    group.result = this.avverkningsstatistikService.get(this.skogligRequest).pipe(
      tap((resp) => {
        this.analysisDone(group);
      }),
      catchError(response => {
        this.analysisDone(group, response);
        return of(undefined);
      })
    );
  }


  private getSkyddadeOmraden() {
    const group = groups[4];
    this.analysisStarted(group);

    group.result = this.skyddadeOmradenService.get(this.skogligRequest).pipe(
      tap((resp: SkyddadeOmradenResponse) => {
        this.analysisDone(group);
      }),
      catchError(response => {
        this.analysisDone(group, response);
        return of(undefined);
      })
    );
  }


  public analysisStarted(group: Group) {
    group.isLoading = true;
    group.error = null;
  }

  public analysisDone(group: Group, errorResponse?: any) {
    group.isLoading = false;
    group.containsData = true;
    group.expanded = true;

    if (errorResponse) {
      if (errorResponse.status === 401) {
        group.error = {
          status: errorResponse.status,
          felkod: "UNAUTHORIZED",
          felbeskrivning: errorMessages["UNAUTHORIZED"]
        };
      } else if (errorResponse.status === 404 || errorResponse.status === 0) {
        group.error = {
          status: errorResponse.status,
          felkod: "NOT_FOUND",
          felbeskrivning: errorMessages["NOT_FOUND"]
        };
      } else if (errorMessages.hasOwnProperty(errorResponse.error)) {
        group.error = {
          status: errorResponse.status,
          felkod: errorResponse.error,
          felbeskrivning: errorMessages[errorResponse.error]
        };
      } else {
        group.error = {
          status: errorResponse.status,
          felkod: "UNKNOWN_ERROR",
          felbeskrivning: errorMessages["UNKNOWN_ERROR"]
        };
      }
    }
  }

  private resetPanel() {
    for (let i = 0; i < this.dataSource.length; i++) {
      this.dataSource[i].expanded = false;
      this.dataSource[i].isLoading = false;
      this.dataSource[i].containsData = false;
      this.dataSource[i].error = null;
      this.dataSource[i].result = of(null);
    }
  }

  public evaluateError(group: Group) {
    if (group.component === "nmd" || group.component === "avverkningsstatistik") {
      return group.error != null && group.error.felkod !== "NO_CONTENT";
    } else {
      return group.error != null;
    }
  }

  private validateComplexity(): boolean {
    let coordinates = 0;
    this.getSelectedFeatures().forEach(feature => {
      coordinates += (this.calculateElements(feature.geometry.coordinates) / 2);
    }, this);
    return coordinates <= 4000;
  }

  private validateArea(): boolean {
    let area = 0;
    this.getSelectedFeatures().forEach(feature => {
      area += (new MultiPolygon(feature.geometry.coordinates)).getArea();
    });
    return (area / 10000) <= 12000;
  }

  private calculateElements(arr) {
    let result = 0;
    arr.forEach(element => {
      (element instanceof Array) ? (result += this.calculateElements(element)) : result++;
    });
    return result;
  }

  private validBoundingBox(): boolean {
    let extent: Extent = null;
    const features = this.getSelectedFeatures();

    if (features.length === 0) {
      return true;
    }

    features.forEach(feature => {
      console.log("feature: ", feature);
      let bbox = feature.bbox ? feature.bbox : feature.properties.bbox;

      // ritade områden har ingen bbox satt i sin multipolygon
      if (!bbox) {
        const mpoly = new MultiPolygon(feature.geometry.coordinates);
        bbox = mpoly.getExtent();
      }

      if (extent === null) {
        extent = bbox;
      } else {
        extend(extent, bbox);
      }
    });

    const sizeInM = getSize(extent);

    return this.validLenght(sizeInM[0]) && this.validLenght(sizeInM[1]);
  }

  private validLenght(lenghtInM: number): boolean {
    if (lenghtInM > 1000) {
      return (lenghtInM / 1000) <= 250;
    }
    return true;
  }
}

export interface Group {
  component: string;
  title: string;
  description: string;
  attribution: string;
  expanded: boolean;
  isLoading: boolean;
  containsData: boolean;
  error: SkogligtFel;
  result: Observable<any>;
}

const groups: Group[] = [
  {
    component: "skogliga-grunddata",
    title: "Skogliga grunddata",
    description: "Beräkning av skattade skogliga nyckeltal utifrån Skogsstyrelsens skogliga grunddata. Värden under överskriften ”Original” redovisar ursprungsvärden från skattningarna. För siffror under ”Efter avverkning” har slutavverkad skog redovisad av Skogsstyrelsen tagits bort från beräkningarna. Observera att tillväxtberäkningar på skogen ej har applicerats på siffrorna utan siffrorna är aktuella för det givna referensåret.\n",
    attribution: "Metria",
    expanded: true,
    isLoading: false,
    containsData: false,
    error: null,
    result: new Observable<SkogligaGrunddataResponse>()
  }, {
    component: "nmd",
    title: "Trädslagsfördelning",
    description: "Anger trädslagsfördelning vilken kategoriserats till åtta olika skogstyper. Samt marktyper i valt område. Användaren ska vara medveten om att redovisade siffror för skogstyperna är beräknade medelvärden vilket kan ge lokala skillnader mot inventeringar med en högre detaljgrad. Informationen bygger på NMD (nationella marktäckedata).",
    attribution: "Metria",
    expanded: true,
    isLoading: false,
    containsData: false,
    error: null,
    result: new Observable<NmdResponse>()
  }, {
    component: "avverkningsstatistik",
    title: "Avverkningstatistik",
    description: "Redovisning av det valda områdets påverkan av slutavverkningar. Informationen kommer från Skogsstyrelsens data ”Utförda avverkningar” som skapas med hjälp av satellitbildsanalyser. Statistiken gäller slutavverkningar och inte gallringar. Områden mindre än 0,5 hektar finns inte med i informationen.",
    attribution: "Metria",
    expanded: true,
    isLoading: false,
    containsData: false,
    error: null,
    result: new Observable<AvverkningsstatistikResponse>()
  }, {
    component: "huggningsklasser",
    title: "Huggningsklasser",
    description: "En analys över skogens indelning i olika huggningsklasser. Trädhöjden är en styrande parameter för analysen. Förändringar i skogsbeståndet i området efter referensåret har ej fångats i redovisade värden. Huggningsklasserna skall ses som en grov indikation på valt områdes skogliga sammansättning.",
    attribution: "Metria",
    expanded: true,
    isLoading: false,
    containsData: false,
    error: null,
    result: new Observable<HuggningsklasserResponse>()
  },
  {
    component: "skyddade-omraden",
    title: "Skyddade områden",
    description: "Redovisning av det valda området berörs av skyddade områden.",
    attribution: "Metria",
    expanded: true,
    isLoading: false,
    containsData: false,
    error: null,
    result: new Observable<SkyddadeOmradenResponse>()
  }
];

const errorMessages = {
  NO_CONTENT: "Den valda fastigheten saknade innehåll eller kunde ej beräknas.",
  INVALID_GEOMETRY: "Den valda fastigheten är felaktig och kunde ej användas vid beräkning.",
  HUGE_GEOMETRY: "Den valda fastigheten är för stor för att kunna användas vid beräkning.",
  UNKNOWN_ERROR: "Ett okänt fel inträffade under beräkningen.",
  TIMEOUT: "Beräkningen avbröts eftersom den tog för länge att genomföra.",
  COMPLEX_GEOMETRY: "Den valda fastigheten innehåller för många brytpunkter för att kunna användas vid beräkning.",
  INVALID_REQUEST: "Beräkningen kunde ej genomföras eftersom anropsformatet var felaktigt.",
  UNAUTHORIZED: "Beräkningen kunde inte genomföras eftersom auktoriseringen misslyckades.",
  NOT_FOUND: "Beräkningen kunde inte genomföras eftersom beräkningstjänsten inte kunde nås.",
  FATAL_ERROR: "Beräkningen misslyckades pga ett fatalt fel i beräkningstjänsten.",
  FAR_APART: "Avståndet är för stort. Fastigheternas yttre gränser måste ligga inom en sträcka på 25 mil."
};
