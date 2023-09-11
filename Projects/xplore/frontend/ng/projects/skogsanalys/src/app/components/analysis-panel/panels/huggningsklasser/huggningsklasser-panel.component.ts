import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {HuggningsklasserResponse, HuggningsklasserV2Response} from "../../../../services/huggningsklasser.service";
import {SkogligtFel} from "../../analysis-panel.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-huggningsklasser-panel",
  templateUrl: "./huggningsklasser-panel.component.html",
  styleUrls: ["./huggningsklasser-panel.component.scss"]
})
export class HuggningsklasserPanelComponent {
  @Input() resultat: HuggningsklasserV2Response;
  @Input() error: SkogligtFel;

  colorScheme = {
    domain: ["#6CAC2B"]
  };

  chartTypes = [
    "Volym",
    "Areal"
  ];

  // Initialize chart type to default type
  chartTypeVolym = this.chartTypes[0];
  chartTypeAreal = this.chartTypes[1];

  getChartData(results: HuggningsklasserV2Response) {
    if (this.chartTypeVolym === this.chartTypes[1]) {
      return this.getArealChartData(results);
    }
    return this.getVolymChartData(results);
  }

  getChartLabel() {
    if (this.chartTypeVolym === this.chartTypes[1]) {
      return "Areal (ha)";
    }
      return "Volym (m\u00B3sk)";
  }

  getChartTooltipPostfix() {
    if (this.chartTypeVolym === this.chartTypes[1]) {
      return "ha";
    }
    return "m\u00B3sk";
  }

  getArealChartData(results: HuggningsklasserV2Response) {
    const result = [
      { "name": "0-3", "value": results.areal0to3HaExklAvv},
      { "name": "3-10", "value": results.areal3to10HaExklAvv},
      { "name": "10-15", "value": results.areal10to15HaExklAvv},
      { "name": "15-20", "value": results.areal15to20HaExklAvv},
      { "name": "20-25", "value": results.areal20to25HaExklAvv},
      { "name": "25+", "value": results.arealGe25HaExklAvv}
    ];
    return result;
  }

  getVolymChartData(results: HuggningsklasserV2Response) {
    const result = [
      { "name": "0-3", "value": results.vol0to3ExklAvv},
      { "name": "3-10", "value": results.vol3to10ExklAvv},
      { "name": "10-15", "value": results.vol10to15ExklAvv},
      { "name": "15-20", "value": results.vol15to20ExklAvv},
      { "name": "20-25", "value": results.vol20to25ExklAvv},
      { "name": "25+", "value": results.volGe25ExklAvv}
    ];
    return result;
  }
}
