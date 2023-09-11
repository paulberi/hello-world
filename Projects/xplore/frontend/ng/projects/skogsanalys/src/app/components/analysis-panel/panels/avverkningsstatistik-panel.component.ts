import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {AvverkningsstatistikResponse} from "../../../services/avverkningsstatistik.service";
import {SkogligtFel} from "../analysis-panel.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-avverkningsstatistik-panel",
  template: `
    <ng-container [ngSwitch]="getResultStatus()">
      <div *ngSwitchCase="'error'"><span style="color: red">{{error.felbeskrivning}}</span></div>
      <div *ngSwitchCase="'no_results'">
        Inga resultat har hämtats för den här fastigheten.
      </div>
      <div *ngSwitchCase="'no_harvest'">
        Ingen avverkning har skett på den här fastigheten de senaste åren.
      </div>
      <div *ngSwitchDefault>
        <table class="table table-hover">
          <tr>
            <th>Avverkningsår</th>
            <th>Areal</th>
          </tr>
          <tr *ngFor="let avverkning of resultat.Avverkningar">
            <td>{{avverkning.avverkningsAr ? avverkning.avverkningsAr : "Okänt år"}}</td>
            <td>{{avverkning.areallHa < 0.1 ? '< 0.1' : avverkning.areallHa| number:'1.0-1':'sv'}} ha</td>
          </tr>
        </table>
        <div class="diagram">
          <ngx-charts-bar-vertical
            [scheme]="colorScheme"
            [results]="chartData"
            [xAxis]="true"
            [yAxis]="true"
            [legend]="false"
            [animations]="false"
            [showYAxisLabel]="true"
            [yAxisLabel]="'Areal (ha)'">
            <ng-template #tooltipTemplate let-model="model">
              <span class="tooltip-label">{{model.name}}</span>
              <span class="tooltip-val">{{model.value | number:'1.0-1':'sv' }} ha</span>
            </ng-template>
          </ngx-charts-bar-vertical>
        </div>
      </div>
    </ng-container>
  `,
  styles: [`
    h3 {
      margin-top: 10px;
      margin-bottom: 0;
    }

    .diagram {
      height: 250px;
      background-color: transparent;
      border-radius: 2px;
      color: white;
      margin-bottom: 10px;
    }
  `]
})
export class AvverkningsstatistikPanelComponent implements OnChanges {
  @Input() resultat: AvverkningsstatistikResponse;
  @Input() error: SkogligtFel;

  chartData = [];

  colorScheme = {
    domain: ["#5AA454", "#A10A28", "#18888a", "#AAAAAA"] // Sätt färger för diagramkolumnerna här
  };

  ngOnChanges(changes: SimpleChanges) {
    if (changes['resultat']) {
      this.updateChartData();
    }
  }

  getResultStatus(): string {
    if (this.error) {
      if (this.error.felkod === 'NO_CONTENT') {
        return "no_harvest";
      }
      else {
        return "error";
      }
    } else if (!this.resultat) {
      return "no_results";
    } else {
      return "default";
    }
  }

  updateChartData() {
    if (!this.resultat) {
      return;
    }

    const currentYear = new Date().getFullYear();
    const minNumberOfIntervals = 3; // The minimum number of intervals that are always shown in the chart
    const intervalLength = 5; // The length of each interval in years
    const avverkningar = this.resultat.Avverkningar;

    // Every index represents a x-year interval.
    // If the interval length is 5, then Index 0 is the interval 0-4 years back, index 1 is 5-9 years back, etc.
    const intervalTotalAreal = new Array(minNumberOfIntervals).fill(0);

    let unknownIntervalAreal = 0;

    // Calculate the total area for each year interval
    for (let i = 0; i < avverkningar.length; i++) {
      // Calculate interval index
      if (!avverkningar[i].avverkningsAr) {
        // Some avverkningar seem to have no date, group these in a separate 'interval'
        unknownIntervalAreal += avverkningar[i].areallHa;
        continue;
      }
      const intervalIndex = Math.floor((currentYear - avverkningar[i].avverkningsAr) / intervalLength);

      // Intervals outside the minimum intervals set ones will be undefined, so set them to 0 if there are values for them
      if (!intervalTotalAreal[intervalIndex]) {intervalTotalAreal[intervalIndex] = 0; }

      intervalTotalAreal[intervalIndex] += avverkningar[i].areallHa;
    }

    this.chartData = [];
    for (let i = 0; i < intervalTotalAreal.length; i++) {
      const intervalStart = currentYear - (intervalLength - 1) - ( intervalLength * i );
      const intervalEnd = currentYear - ( intervalLength * i );
      this.chartData[i] = {
        "name": intervalStart + "-" + intervalEnd,
        "value": isNaN(intervalTotalAreal[i]) ? 0 : intervalTotalAreal[i],
      };
    }

    if (unknownIntervalAreal > 0) {
      this.chartData[this.chartData.length] = {
        "name": "Okänt år",
        "value": unknownIntervalAreal
      };
    }

    // Reverse the array to get the oldest intervals first
    this.chartData.reverse();
  }
}
