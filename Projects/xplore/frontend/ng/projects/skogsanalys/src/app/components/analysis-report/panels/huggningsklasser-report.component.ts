import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {HuggningsklasserResponse, HuggningsklasserV2Response} from "../../../services/huggningsklasser.service";
import {SkogligtFel} from "../analysis-report.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-huggningsklasser-report",
  template: `
    <p>
      En analys över skogens indelning i olika huggningsklasser. Trädhöjden är en styrande parameter för analysen.
      Förändringar i skogsbeståndet i området efter referensåret har ej fångats i redovisade värden. Huggningsklasserna
      skall ses som en grov indikation på valt områdes skogliga sammansättning.</p>
    <br/>
    <div *ngIf="!resultat && !error">
      Inga resultat har hämtats för den här fastigheten
    </div>
    <div *ngIf="error"><span style="color: red">{{error.felbeskrivning}}</span></div>
    <div *ngIf="resultat">
      <table>
        <tr>
          <td>Medelhöjd</td>
          <td>{{resultat.medel < 0.1 ? '< 0.1' : resultat.medel | number:'1.0-1':'sv'}} m</td>
        </tr>
        <tr>
          <td>Referensdatum</td>
          <td>{{resultat.referensArIntervall}}</td>
        </tr>
        <tr>
          <td>Lövad säsong</td>
          <td>{{resultat.lov ? 'Ja' : 'Nej'}}</td>
        </tr>
      </table>

      <h3>Areal</h3>
      <div class="histogram">
        <ngx-charts-bar-vertical
          [scheme]="colorScheme"
          [results]="getArealChartData(resultat)"
          [xAxis]="true"
          [yAxis]="true"
          [legend]="false"
          [animations]="false"
          [showYAxisLabel]="true"
          [showXAxisLabel]="true"
          [yAxisLabel]="'Areal (ha)'"
          [xAxisLabel]="'Trädhöjd (m)'">
          <ng-template #tooltipTemplate let-model="model">
            <span class="tooltip-label">{{model.name}}</span>
            <span class="tooltip-val">{{model.value | number:'1.0-1':'sv' }} ha</span>
          </ng-template>
        </ngx-charts-bar-vertical>
      </div>
      <h3>Volym</h3>
      <div class="histogram">
        <ngx-charts-bar-vertical
          [scheme]="colorScheme"
          [results]="getVolymChartData(resultat)"
          [xAxis]="true"
          [yAxis]="true"
          [legend]="false"
          [animations]="false"
          [showYAxisLabel]="true"
          [showXAxisLabel]="true"
          [yAxisLabel]="'Volym (m\u00B3sk)'"
          [xAxisLabel]="'Trädhöjd (m)'">
          <ng-template #tooltipTemplate let-model="model">
            <span class="tooltip-label">{{model.name}}</span>
            <span class="tooltip-val">{{model.value | number:'1.0-1':'sv' }} {{'m\u00B3sk'}}</span>
          </ng-template>
        </ngx-charts-bar-vertical>
      </div>
    </div>
  `,
  styles: [`
    h3 {
      margin-top: 10px;
      margin-bottom: 0;
    }

    .histogram {
      height: 250px;
      background-color: transparent;
      border-radius: 2px;
      color: white;
      margin-bottom: 10px;
    }
  `]
})


export class HuggningsklasserReportComponent {
  @Input() resultat: HuggningsklasserV2Response;
  @Input() error: SkogligtFel;

  colorScheme = {
    domain: ["#6CAC2B"]
  };


  getArealChartData(results: HuggningsklasserV2Response) {
    const result = [
      {"name": "0-3", "value": results.areal0to3HaExklAvv},
      {"name": "3-10", "value": results.areal3to10HaExklAvv},
      {"name": "10-15", "value": results.areal10to15HaExklAvv},
      {"name": "15-20", "value": results.areal15to20HaExklAvv},
      {"name": "20-25", "value": results.areal20to25HaExklAvv},
      {"name": "25+", "value": results.arealGe25HaExklAvv}
    ];
    return result;
  }

  getVolymChartData(results: HuggningsklasserV2Response) {
    const result = [
      {"name": "0-3", "value": results.vol0to3ExklAvv},
      {"name": "3-10", "value": results.vol3to10ExklAvv},
      {"name": "10-15", "value": results.vol10to15ExklAvv},
      {"name": "15-20", "value": results.vol15to20ExklAvv},
      {"name": "20-25", "value": results.vol20to25ExklAvv},
      {"name": "25+", "value": results.volGe25ExklAvv}
    ];
    return result;
  }
}
