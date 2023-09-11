import {Component} from "@angular/core";

import {MatCheckboxChange} from "@angular/material/checkbox";
import {MatSliderChange} from "@angular/material/slider";
import {StateService} from "../../../../services/state.service";
import {ConfigProperty, ConfigService} from "../../../../../config/config.service";
import {createFeatureName, FeatureType, isRitadFeature, sortFeatures} from "../../../../util/fastighet.util";
import {GeoJson} from "../../../../../map-core/geojson.util";
import GeoJSON from "ol/format/GeoJSON";
import {ExportRequest, ExportService} from "../../../../export/export.service";
import {HttpHeaders} from "@angular/common/http";
import {ErrorHandlingHttpClient} from "../../../../../http/http-client.service";
import * as fileSaver from "file-saver";
import {MatDialog} from "@angular/material/dialog";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogModel
} from "../../../dialogs/confirmation-dialog/confirmation-dialog.component";
import { AdressEtiketter } from "../../../../../docx/specializedBuilders/adressetiketter";
import {SelectionService} from "../../../../services/selection.service";

/**
 * Visar upp fastighetsinfo vid klick på fastighet.
 */
@Component({
  selector: "xp-fsok-panel",
  templateUrl: "./fsok-panel.component.html",
  styleUrls: ["./fsok-panel.component.scss"]
})
export class FsokPanelComponent {
  showSettings = false;
  showExport = false;
  showOwnerList = false;

  visaFastighetsgranser: boolean;
  grupperaFastigheter: boolean;
  inverteraFastigheter: boolean;
  unionDelomraden: boolean;
  currOpacity: number;

  resultat: Resultat;

  expanded: boolean;
  saveDialogRef;

  extendedStateFastigheter = [];

  constructor(private stateService: StateService, private configService: ConfigService,
              private exportService: ExportService, private http: ErrorHandlingHttpClient,
              public dialog: MatDialog, private selectionService: SelectionService) {
    this.visaFastighetsgranser = stateService.getUiStates().visaFastighetsgranser;
    this.grupperaFastigheter = stateService.getUiStates().grupperaFastigheter;
    this.inverteraFastigheter = stateService.getUiStates().inverteraFastigheter;
    this.unionDelomraden = stateService.getUiStates().unionDelomraden;
    this.currOpacity = stateService.getUiStates().fastighetsgranserOpacity;

    this.stateService.uiStates.subscribe(uiStates => {
      this.visaFastighetsgranser = uiStates.visaFastighetsgranser;
      this.grupperaFastigheter = uiStates.grupperaFastigheter;
      this.inverteraFastigheter = uiStates.inverteraFastigheter;
      this.unionDelomraden = uiStates.unionDelomraden;
      this.currOpacity = uiStates.fastighetsgranserOpacity;

      sortFeatures(uiStates.valdaDelomraden);
      this.buildResult(uiStates.valdaDelomraden);

      if (this.resultat.fastigheter.length === 0) {
        this.expanded = false;
      }
    });

    const prop = configService.getConfigProperty(ConfigProperty.FSOK_PANEL);
    this.showExport = !prop || !prop.export ? false : prop.export.visible;
    this.showOwnerList = !prop || !prop.ownerList ? false : prop.ownerList.visible;
  }

  onVisaFastighetsgranserCheckboxChange(event: MatCheckboxChange) {
    this.stateService.setUiStates({visaFastighetsgranser: event.checked});
  }

  onGrupperaFastigheterCheckboxChange(event: MatCheckboxChange) {
    this.stateService.setUiStates({grupperaFastigheter: event.checked});
  }

  onInverteraFastigheterCheckboxChange(event: MatCheckboxChange) {
    this.stateService.setUiStates({inverteraFastigheter: event.checked});
  }

  onUnionDelomradenCheckboxChange(event: MatCheckboxChange) {
    this.stateService.setUiStates({unionDelomraden: event.checked});
  }


  onOpacityChange(event: MatSliderChange) {
    this.stateService.setUiStates({fastighetsgranserOpacity: event.value});
  }

  onSettingsButtonClick(event: Event) {
    event.stopPropagation();
    this.showSettings = !this.showSettings;
  }

  onSaveButtonClick(event: Event) {
    event.stopPropagation();
  }

  onExportButtonClick(event: Event) {
    event.stopPropagation();
    const request: ExportRequest = {
      filename: "fastigheter",
      format: "XLSX",
      valuesExportFunc: (feature) => {
        return {
          // FNR: feature.getProperties().FNR_FDS,
          Objekt_ID: feature.getProperties().objekt_id,
          Detaljtyp: feature.getProperties().detaljtyp,
          Kommunkod: feature.getProperties().kommunkod,
          Kommunnamn: feature.getProperties().kommunnamn,
          Trakt: feature.getProperties().trakt,
          Blockenhet: feature.getProperties().blockenhet,
          Omrnr: feature.getProperties().omrnr,
          Fastighet: feature.getProperties().fastighet
        };
      },
      features: this.resultat.delomraden.map(delomrade =>  new GeoJSON().readFeature(delomrade.geojson))
    };
    this.exportService.export(request);
  }

  onListButtonClick(event: Event, choice: String) {
    event.stopPropagation();
    const fastigheter = this.getSelectedFastigheter();

    const body = {
      fastigheter: fastigheter,
      kundmarke: ""
    };


    if(choice === null  || choice ===''){
      choice = "METAGADR";
    }

    const antalFastigheterStr = this.createAntalFastigheterStr(fastigheter);

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "500px",
      data: new ConfirmationDialogModel(
        "Bekräfta",
        `<p>Aktuell ägarförteckning omfattar ${antalFastigheterStr}.</p>
                 <p>Ni blir debiterade enligt avtal för det antal fastigheter som ger träff i fastighetsregistret samt en startavgift.</p>
                 <p>OBS! Förteckningar med fler än 500 fastigheter är inte möjligt att producera. Kontakta vår <a href="mailto:kundservice@metria.se">Kundservice</a> för dialog.`,
        "Avbryt",
        "Beställ")
    });
    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult) {
        // Direct request owner list and save file
        this.http.post(ConfigService.urlConfig.ownerListUrl + "?typ=" + choice + "&mode=direct", body,
          {observe: "response", responseType: "blob", headers: new HttpHeaders({
              "Accept": "application/vnd.ms-excel"
            })}).subscribe(
          res => {
            const data = res.body;
            const blob: any = new Blob([data], { type: "application/octet-stream" });
            fileSaver.saveAs(blob, "Ägarförteckning.xlsx");
          }
        );
      }
    });
  }

  private getSelectedFastigheter() {
    const features = this.resultat.delomraden.map(delomrade =>  new GeoJSON().readFeature(delomrade.geojson));
    const fastigheter = [];
    features.forEach((feature) => {
      fastigheter.push(feature.getProperties().objekt_id);
    });

    return fastigheter;
  }

  private createAntalFastigheterStr(fastigheter): string {
    const unikaFastigheter = fastigheter.filter((elem, index, self) => {
      return index === self.indexOf(elem);
    });

    if (unikaFastigheter.length === 1) {
      return "1 fastighet";
    } else if ( unikaFastigheter.length > 1 ) {
      return unikaFastigheter.length.toString() + " fastigheter";
    }
  }

  onAddressButtonClick(event: Event) {
    event.stopPropagation();

    const fastigheter = this.getSelectedFastigheter();

    const body = {
      fastigheter: fastigheter,
      kundmarke: ""
    };

    const antalFastigheterStr = this.createAntalFastigheterStr(fastigheter);

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "500px",
      data: new ConfirmationDialogModel(
        "Bekräfta",
        `<p>Aktuell ägarförteckning omfattar ${antalFastigheterStr}.</p>
                 <p>Ni blir debiterade enligt avtal för det antal fastigheter som ger träff i fastighetsregistret samt en startavgift.</p>
                 <p>OBS! Förteckningar med fler än 500 fastigheter är inte möjligt att producera. Kontakta vår <a href="mailto:kundservice@metria.se">Kundservice</a> för dialog.`,
        "Avbryt",
        "Beställ")
    });
    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult) {
        // Direct request owner list convert to adressetiketter and save file
        this.http.post(ConfigService.urlConfig.ownerListUrl + "?mode=direct", body,
          {observe: "response", responseType: "blob", headers: new HttpHeaders({
              "Accept": "application/json"
            })}).subscribe(
          res => {
            const data = res.body;
            const blob: any = new Blob([data], { type: "application/json" });

            const fr = new FileReader();

            fr.onload = (e) => {
              this.buildAdressetiketter(JSON.parse(e.target.result.toString()));
            };

            fr.readAsText(blob);
          }
        );
      }
    });
  }

  onRemoveFastighet(objektId: string) {
    this.selectionService.deselectOmrade(objektId);
    const index = this.extendedStateFastigheter.indexOf(objektId);
    this.extendedStateFastigheter.splice(index, 1);
  }

  onRemoveDelomrade(externId: string) {
    this.selectionService.deselectDelomrade(externId);
  }

  onRemoveEgetRitatOmrade() {
    this.selectionService.deselectEgenRitatOmrade();
  }

  private buildAdressetiketter(fastigheter) {
    const adressEtiketter = new AdressEtiketter();

    const sortedFastigheter = fastigheter.fastighet.sort((f1, f2) => (f1.beteckning > f2.beteckning ? 1 : -1));

    sortedFastigheter.forEach(fastighet => {
      fastighet.agare.forEach(agare => {
        adressEtiketter.addAdress(fastighet.beteckning, agare.namn, agare.adress, agare.postnummer, agare.postort);
      });
    });

    adressEtiketter.save();
  }

  private buildResult(delomraden: GeoJson[]): Resultat {
    this.resultat = {fastigheter: [], delomraden: []};

    if (!delomraden.length) {
      return;
    }

    delomraden.forEach(feature => {
        if (!isRitadFeature(feature)) {
          const objektId = feature.properties.objekt_id;
          const externId = feature.properties.externid;
          const detaljTyp = feature.properties.detaljtyp;
          let existing = this.resultat.fastigheter.find((fastighet: Fastighet) =>
            (objektId && objektId.length && objektId === fastighet.objektId));
          if (!existing) {
            existing = {
              objektId: objektId,
              beteckning: createFeatureName(feature, FeatureType.FASTIGHET),
              delomraden: [],
              externId,
              detaljTyp
            };

            if (!this.extendedStateFastigheter[objektId]) {
              this.extendedStateFastigheter[objektId] = false;
            }
            this.resultat.fastigheter.push(existing);
          }

          existing.delomraden.push({
            objektId: objektId,
            beteckning: createFeatureName(feature, FeatureType.FASTIGHETSDELOMRADE),
            geojson: feature
          });
        } else {
          const ritadYta = {
            objektId: null,
            beteckning: createFeatureName(feature, FeatureType.RITAD_YTA),
            delomraden: [
              {
                objektId: null,
                beteckning: createFeatureName(feature, FeatureType.RITAD_YTA),
                geojson: feature
              }
            ]
          };

          this.resultat.fastigheter.push(ritadYta);
        }
      }
    );

    this.resultat.fastigheter.forEach(fastighet => {
      this.resultat.delomraden = this.resultat.delomraden.concat(fastighet.delomraden);
    });
  }
}

export interface Resultat {
  fastigheter: Fastighet[];
  delomraden: Delomrade[];
  ritadYta?: RitadYta;
}

export interface Fastighet {
  objektId: string;
  beteckning: string;
  delomraden: Delomrade[];
  externId?: string;
  detaljTyp?: string;
}

export interface Delomrade {
  objektId: string;
  beteckning: string;
  geojson: GeoJson;
}

export interface RitadYta {
  beteckning: string;
}
