import {Component, Input, OnInit} from "@angular/core";
import {Rapport, RapportGraf} from "../services/rapport.service";
import { BifogadfilService } from "../services/bifogadfil.service";
import moment from "moment";
import { ViewEncapsulation } from "@angular/core";
import {ConfigService, MapConfig} from "../../../../lib/config/config.service";
import {MatdatabasInitService} from "../services/matdatabas-init.service";

@Component({
  selector: "mdb-rapport",
  template: `
    <header>
      <div class="rapport-header">
        <div>
          <img src={{reportLogoUrl}} width="200px"><br>
          {{reportHeaderText}}
        </div>
        <div class="header-right-column-grid">
          <span></span><span></span>
          <span></span><span></span>
          <span style="text-align: right; padding-right: 0.5rem;">Datum:</span><span>{{dateString()}}</span>
        </div>
      </div>
    </header>

    <footer>
      <div class="rapport-footer">
        <div class="footer-column">
          {{reportFooterTextLeft}}
        </div>
        <div class="footer-column">
          {{reportFooterTextCenter}}
        </div>
        <div class="footer-column">
          {{reportFooterTextRight}}
        </div>
        <div class="span-3-columns">
          {{reportFooterTextBottom}}
        </div>
      </div>
    </footer>

    <table>

    <thead><tr><td>
          <div class="header-space"></div>
    </td></tr></thead>

    <tbody><tr><td>
          <div class="main-content">
            <div class="rapport-info">
              <h1 style="text-align: center">{{rapport?.rubrik}}</h1>
              <br>
              <div style="overflow: auto;">
                <div *ngIf="rapport.lagesbildId">
                  <img class="img" [src]="lagesbildUrl() | secureLoadImage | async" alt="Lägesbild på objekt">
                  <p style="white-space: pre-wrap">{{rapport?.information}}</p>
                </div>
                <div *ngIf="!rapport.lagesbildId">
                  <p style="white-space: pre-wrap">{{rapport?.information}}</p>
                </div>
              </div>
            </div>
            <div *ngFor="let graf of rapport?.grafer" style="page-break-inside: avoid; position: relative;">
              <mdb-rapport-graf [grafData]="graf" [divName]="divNames.get(graf)"></mdb-rapport-graf>
            </div>
          </div>
    </td></tr></tbody>

    <tfoot><tr><td>
          <div class="footer-space"></div>
    </td></tr></tfoot>

    </table>
  `,
  styles: [`
    *{
      font-family:Verdana,Geneva,sans-serif;
    }

    .rapport-info {
      margin-right: 0.9cm;
      margin-left: 0.9cm;
      line-height: 15.5px;
      font-size: 12px;
    }

    .img {
      max-width: 10cm;
      max-height: 6cm;
      margin-right: 1rem;
      margin-bottom: 0.5rem;
      float: left;
    }

    .description {
      display: grid;
      grid-template-columns: auto auto;
      grid-gap: 10px;
    }

    .information {
      white-space: pre;
    }

    @media screen {
      footer {
        display: none;
      }

      header {
        display: none;
      }
    }

    @media print {
      thead {
        display: table-header-group;
      }

      tfoot {
        display: table-footer-group;
      }

      header {
        position: fixed;
        top: 0;
        margin-top: 1cm;
        margin-left: 1.5cm;
        margin-right: 1.5cm;
      }

      footer {
        position: fixed;
        bottom: 0;
        padding-top: 1cm;
        margin-bottom: 1cm;
        margin-left: 1.5cm;
        margin-right: 1.5cm;
      }

      .main-content {
        margin-right: 0.8cm;
        margin-left: 0.8cm;
      }

      header, .header-space {
        width: 17.5cm;
        height: 140px;
      }

      footer, .footer-space {
        width: 17.5cm;
        height: 165px;
      }

      .rapport-header {
        display: flex;
        justify-content: space-between;
        margin-left: 0.5cm;
        margin-right: 0.5cm;
        font-size: 12px;
      }

      .header-right-column {
        justify-content: right;
      }

      .header-right-column-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        grid-template-rows: 1fr 1fr 1fr;
      }

      .rapport-footer {
        display: grid;
        grid-template-columns: 7cm 7cm 4cm;
        font-size: 11px;
        line-height: 14.5px;
      }

      .footer-column {
        padding-left: 0.5rem;
        padding-right: 1rem;
        padding-bottom: 1rem;
      }

      .span-3-columns {
        grid-column-start: span 3;
        padding-left: 0.5rem;
        padding-right: 1rem;
        padding-bottom: 1rem;
      }
    }
  `], encapsulation: ViewEncapsulation.None
})

export class RapportComponent implements OnInit {
  @Input() rapport: Rapport;
  divNames: Map<RapportGraf, string> = new Map<RapportGraf, string>();

  reportLogoUrl = "../assets/metria_logo_banner.png";
  reportHeaderText = "Metria Miljökoll";
  reportFooterTextLeft: string;
  reportFooterTextCenter: string;
  reportFooterTextRight: string;
  reportFooterTextBottom: string;

  error = false;
  errorCode = null;

  constructor(private bifogadfilService: BifogadfilService,
              private configService: ConfigService,
              private matdatabasInitService: MatdatabasInitService) {
  }

  ngOnInit() {
    this.matdatabasInitService.init((mapConfig) => this.handleConfigDownloadSuccess(mapConfig),
      (error) => this.handleConfigDownloadFailure(error));

    this.rapport.grafer.forEach(g => this.divNames.set(g, this.randomName()));
  }

  randomName(): string {
    return Math.random().toString(36).substring(2);
  }

  lagesbildUrl(): string {
    return this.bifogadfilService.getDataLink(this.rapport.lagesbildId);
  }

  dateString(): string {
    const now = moment();
    return now.format("YYYY-MM-DD");
  }

  private handleConfigDownloadSuccess(mapConfig: MapConfig) {
    this.configService.setMapConfig(mapConfig);

    if (this.configService.config) {
      this.reportLogoUrl = this.configService.config.app.reportLogoUrl;
      this.reportHeaderText = this.configService.config.app.reportHeaderText;
      this.reportFooterTextLeft = this.configService.config.app.reportFooterTextLeft;
      this.reportFooterTextCenter = this.configService.config.app.reportFooterTextCenter;
      this.reportFooterTextRight = this.configService.config.app.reportFooterTextRight;
      this.reportFooterTextBottom = this.configService.config.app.reportFooterTextBottom;
    }
  }

  private handleConfigDownloadFailure(error) {
    this.error = true;
    this.errorCode = error.status;
  }
}
