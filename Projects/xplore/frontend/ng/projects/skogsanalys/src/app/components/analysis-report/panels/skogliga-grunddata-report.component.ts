import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {SkogligaGrunddataResponse} from "../../../services/skogliga-grunddata.service";
import {SkogligtFel} from "../analysis-report.component";
import {Observable} from "rxjs";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-skogliga-grunddata-report",
  template: `
    <p>
      Beräkning av skattade skogliga nyckeltal utifrån Skogsstyrelsens skogliga grunddata. Värden under överskriften
      ”Original” redovisar ursprungsvärden från skattningarna. För siffror under ”Efter avverkning” har slutavverkad
      skog redovisad av Skogsstyrelsen tagits bort från beräkningarna. Observera att tillväxtberäkningar på skogen ej
      har applicerats på siffrorna utan siffrorna är aktuella för det givna referensåret.
    </p>
    <br/>
    <div *ngIf="!resultat && !error">
      Inga resultat har hämtats för den här fastigheten
    </div>
    <div *ngIf="error"><span style="color: red">{{error.felbeskrivning}}</span></div>
    <div *ngIf="resultat | async as resultat">
      <table>
        <thead>
          <tr>
            <th></th>
            <th>Original</th>
            <th>Efter avverkning</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Total areal</td>
            <td>{{resultat.areaHa < 0.1 ? '< 0.1' : resultat.areaHa | number:'1.0-1':'sv'}} ha</td>
            <td></td>
          </tr>
          <tr>
            <td>Produktiv skogsareal</td>
            <td>{{resultat.areaProduktivHa < 0.1 ? '< 0.1' : resultat.areaProduktivHa | number:'1.0-1':'sv'}} ha</td>
          </tr>
          <tr>
            <td>Volym</td>
            <td>{{resultat.volSum | number:'1.0-0':'sv'}} m<sup>3</sup>sk</td>
            <td><ng-container *ngIf="resultat.volSumExklAvv != null">{{resultat.volSumExklAvv | number:'1.0-0':'sv'}} m<sup>3</sup>sk</ng-container></td>
          </tr>
          <tr>
            <td>Volym per ha</td>
            <td>{{resultat.volMedel | number:'1.0-0':'sv'}} m<sup>3</sup>sk/ha</td>
            <td><ng-container *ngIf="resultat.volMedelExklAvv != null">{{resultat.volMedelExklAvv | number:'1.0-0':'sv'}} m<sup>3</sup>sk/ha</ng-container></td>
          </tr>
          <tr>
            <td>Grundyta</td>
            <td>{{resultat.gyMedel | number:'1.0-0':'sv'}} m<sup>2</sup>/ha</td>
            <td><ng-container *ngIf="resultat.gyMedelExklAvv != null">{{resultat.gyMedelExklAvv | number:'1.0-0':'sv'}} m<sup>2</sup>/ha</ng-container></td>
          </tr>
          <tr>
            <td>Medelhöjd</td>
            <td>{{resultat.hgvMedel | number:'1.0-1':'sv'}} m</td>
            <td><ng-container *ngIf="resultat.hgvMedelExklAvv != null">{{resultat.hgvMedelExklAvv | number:'1.0-1':'sv'}} m</ng-container></td>
          </tr>
          <tr>
            <td>Medeldiameter</td>
            <td>{{resultat.dgvMedel | number:'1.0-0':'sv'}} cm</td>
            <td><ng-container *ngIf="resultat.dgvMedelExklAvv != null">{{resultat.dgvMedelExklAvv | number:'1.0-0':'sv'}} cm</ng-container></td>
          </tr>
          <tr>
            <td>Lövad säsong</td>
            <td>{{resultat.lov ? 'Ja' : 'Nej'}}</td>
            <td></td>
          </tr>
          <tr>
            <td>Referensdatum</td>
            <td>{{resultat.referensArIntervall}}</td>
            <td></td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [`
    h3 {
      margin-bottom: 0;
    }

    table {
      text-align: left;
    }
  `]
})
export class SkogligaGrunddataReportComponent {
  @Input() resultat: Observable<SkogligaGrunddataResponse>;
  @Input() error: SkogligtFel;
}
