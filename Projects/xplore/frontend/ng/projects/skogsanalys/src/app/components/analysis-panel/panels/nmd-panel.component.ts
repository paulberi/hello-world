import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import { treeColors } from "../../../../../../lib/colors/treeColors";
import {NmdResponse} from "../../../services/nmd.service";
import {SkogligtFel} from "../analysis-panel.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-nmd-panel",
  template: `
    <div *ngIf="error">
      <ng-container *ngIf="error.felkod !== 'NO_CONTENT'; else noraster">
        <span style="color: red">{{error.felbeskrivning}}</span>
      </ng-container>
      <ng-template #noraster>
        <p>Nationellt marktäckedata saknas för närvarande för detta område.</p>
      </ng-template>
    </div>
    <div *ngIf="resultat">
      <table class="table table-hover">
        <tr>
          <td>Areal</td>
          <td>{{resultat.Areal_raster_ha < 0.1 ? '< 0.1' : resultat.Areal_raster_ha | number:'1.0-1':'sv'}} ha</td>
        </tr>
        <tr>
          <td>Referensår</td>
          <td>{{resultat.referensAr}}</td>
        </tr>
      </table>
      <div *ngIf="resultat.raster_status !== 'OK'" style="color: red; padding-top: 0.5em">
        Området ligger delvis utanför begränsningsytan för nationellt marktäckedata.
      </div>
      <h3>Arealfördelning</h3>
      <div class="diagram">
        <ngx-charts-bar-horizontal
          [scheme]="colorSchemeAreaTypes"
          [results]="getSkogsmarkResults(resultat)"
          [xAxis]="true"
          [yAxis]="true"
          [legend]="false"
          [animations]="false"
          [showXAxisLabel]="true"
          [showYAxisLabel]="false"
          [showDataLabel]="true"
          [xAxisLabel]="'Areal (ha)'"
          [yAxisLabel]="'Marktyp'">
          <ng-template #tooltipTemplate let-model="model">
            <span class="tooltip-label">{{model.name}}</span>
            <span class="tooltip-val">{{model.value | number:'1.0-1':'sv' }} ha</span>
          </ng-template>
        </ngx-charts-bar-horizontal>
      </div>
      <h3>Trädslagsfördelning</h3>
      <mat-radio-group [(ngModel)]="chartTypeTrad">
        <mat-radio-button *ngFor="let chartType of chartTypes" [value]="chartType" class="chart-radio-button">
          {{chartType}}
        </mat-radio-button>
      </mat-radio-group>
      <div class="diagram" *ngIf="chartTypeTrad === chartTypes[0]">
        <ngx-charts-bar-horizontal
          [scheme]="colorSchemeTreeTypes"
          [results]="getTradslagResults(resultat)"
          [xAxis]="true"
          [yAxis]="true"
          [legend]="false"
          [animations]="false"
          [showXAxisLabel]="true"
          [showYAxisLabel]="false"
          [showDataLabel]="true"
          [xAxisLabel]="'Areal (ha)'"
          [yAxisLabel]="'Marktyp'">
          <ng-template #tooltipTemplate let-model="model">
            <span class="tooltip-label">{{model.name}}</span>
            <span class="tooltip-val">{{model.value | number:'1.0-1':'sv'
              }} ha ({{(model.value / totalHa) | percent:'1.0-0'}})</span>
          </ng-template>
        </ngx-charts-bar-horizontal>
      </div>
      <div class="diagram-pie" *ngIf="chartTypeTrad === chartTypes[1]">
        <ngx-charts-pie-chart
          [scheme]="colorSchemeTreeTypes"
          [results]="getTradslagResults(resultat)"
          [legend]="false"
          [animations]="false"
          [labels]="true"
          [maxLabelLength]="15">
          <ng-template #tooltipTemplate let-model="model">
            <span class="tooltip-label">{{model.name}}</span>
            <span class="tooltip-val">{{model.value | number:'1.0-1':'sv'
              }} ha ({{(model.value / totalHa) | percent:'1.0-0'}})</span>
          </ng-template>
        </ngx-charts-pie-chart>
      </div>
    </div>
  `,
  styles: [`
    .chart-radio-button {
      margin-bottom: 5px;
      margin-right: 5px;
    }

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

    .diagram-pie {
      height: 200px;
      background-color: transparent;
      border-radius: 2px;
      color: white;
    }
  `]
})
export class NmdPanelComponent {
  @Input() resultat: NmdResponse;
  @Input() error: SkogligtFel;

  chartTypes = [
    "Stapel",
    "Cirkel"
  ];
  // Initialize chart type to default type
  chartTypeTrad = this.chartTypes[1];
  chartTypeMark = this.chartTypes[0];

  colorSchemeAreaTypes = { // Sätt färger för arealfördelningen här
    domain: [
      treeColors.utanfor_vatmark.lovblandadbarrskog,
      treeColors.pa_vatmark.lovblandadbarrskog,
      treeColors.vatmark,
      treeColors.akermark,
      treeColors.ovrig_oppen_mark,
      treeColors.exploaterad_mark,
      treeColors.vatten,
      treeColors.oklassat,
    ]
  };

  colorSchemeTreeTypes = { // Sätt färger för trädslagsfördelning här
    domain: [
      treeColors.utanfor_vatmark.tallskog, 
      treeColors.utanfor_vatmark.granskog, 
      treeColors.utanfor_vatmark.barrblandskog, 
      treeColors.utanfor_vatmark.lovblandadbarrskog, 
      treeColors.utanfor_vatmark.triviallovskog, 
      treeColors.utanfor_vatmark.adellovskog, 
      treeColors.utanfor_vatmark.triviallovskog_med_adellovinslag, 
      treeColors.utanfor_vatmark.temporart_ej_skog
    ]
  };

  totalHa;

  getTradslagResults(results: NmdResponse) {
    this.totalHa =
      results.tallskogHa +
      results.granskogHa +
      results.barrblandskogHa +
      results.lovblandadBarrskogHa +
      results.triviallovHa +
      results.adellovHa +
      results.triviallovAdellovHa +
      results.temporartEjSkogHa;

    return [
      {
        "name": "Tallskog",
        "value": results.tallskogHa
      },
      {
        "name": "Granskog",
        "value": results.granskogHa
      },
      {
        "name": "Barrblandskog",
        "value": results.barrblandskogHa
      },
      {
        "name": "Lövblandad barrskog",
        "value": results.lovblandadBarrskogHa
      },
      {
        "name": "Triviallövskog",
        "value": results.triviallovHa
      },
      {
        "name": "Ädellövskog",
        "value": results.adellovHa
      },
      {
        "name": "Triviallövskog med ädellövinslag",
        "value": results.triviallovAdellovHa
      },
      {
        "name": "Temporärt ej skog",
        "value": results.temporartEjSkogHa
      },
    ];
  }

  getSkogsmarkResults(results: NmdResponse) {
    return [
      {
        "name": "Skog utanför våtmark",
        "value": results.skogHa
      },
      {
        "name": "Skog på våtmark",
        "value": results.skogVatmarkHa
      },
      {
        "name": "Öppen våtmark",
        "value": results.oppenVatmarkHa
      },
      {
        "name": "Åkermark",
        "value": results.akermarkHa
      },
      {
        "name": "Övrig öppen mark",
        "value": results.ovrigOppenMarkHa
      },
      {
        "name": "Exploaterad mark",
        "value": results.exploateradMarkHa
      },
      {
        "name": "Vatten",
        "value": results.vattenHa
      },
      {
        "name": "Moln/Oklassat",
        "value": results.molnOklassatHa
      },
    ];
  }
}
