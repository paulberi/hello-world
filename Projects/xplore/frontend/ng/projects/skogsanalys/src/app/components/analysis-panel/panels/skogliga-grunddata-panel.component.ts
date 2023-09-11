import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {SkogligaGrunddataResponse} from "../../../services/skogliga-grunddata.service";
import {SkogligtFel} from "../analysis-panel.component";
import {Observable} from "rxjs";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-skogliga-grunddata-panel",
  template: `
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
          </tr>
          <tr>
            <td>Produktiv skogsareal</td>
            <td>{{resultat.areaProduktivHa < 0.1 ? '< 0.1' : resultat.areaProduktivHa | number:'1.0-1':'sv'}} ha</td>
          </tr>
          <tr>
            <td>Total skogsareal</td>
            <td>{{resultat.areaSkogHa < 0.1 ? '< 0.1' : resultat.areaSkogHa | number:'1.0-1':'sv'}} ha</td>
          </tr>
          <tr>
            <td>Volym</td>
            <td>{{resultat.volSum | number:'1.0-0':'sv'}} m<sup>3</sup>sk</td>
            <td *ngIf="resultat.volSumExklAvv != null">{{resultat.volSumExklAvv | number:'1.0-0':'sv'}} m<sup>3</sup>sk</td>
          </tr>
          <tr>
            <td>Volym per ha</td>
            <td>{{resultat.volMedel | number:'1.0-0':'sv'}} m<sup>3</sup>sk/ha</td>
            <td *ngIf="resultat.volMedelExklAvv != null">{{resultat.volMedelExklAvv | number:'1.0-0':'sv'}} m<sup>3</sup>sk/ha</td>
          </tr>
          <tr>
            <td>Grundyta</td>
            <td>{{resultat.gyMedel | number:'1.0-0':'sv'}} m<sup>2</sup>/ha</td>
            <td *ngIf="resultat.gyMedelExklAvv != null">{{resultat.gyMedelExklAvv | number:'1.0-0':'sv'}} m<sup>2</sup>/ha</td>
          </tr>
          <tr>
            <td>Medelhöjd</td>
            <td>{{resultat.hgvMedel | number:'1.0-1':'sv'}} m</td>
            <td *ngIf="resultat.hgvMedelExklAvv != null">{{resultat.hgvMedelExklAvv | number:'1.0-1':'sv'}} m</td>
          </tr>
          <tr>
            <td>Medeldiameter</td>
            <td>{{resultat.dgvMedel | number:'1.0-0':'sv'}} cm</td>
            <td *ngIf="resultat.dgvMedelExklAvv != null">{{resultat.dgvMedelExklAvv | number:'1.0-0':'sv'}} cm</td>
          </tr>
          <tr>
            <td>Lövad säsong</td>
            <td>{{resultat.lov ? 'Ja' : 'Nej'}}</td>
          </tr>
          <tr>
            <td>Referensdatum</td>
            <td>{{resultat.referensArIntervall}}</td>
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
export class SkogligaGrunddataPanelComponent {
  @Input() resultat: Observable<SkogligaGrunddataResponse>;
  @Input() error: SkogligtFel;
}
