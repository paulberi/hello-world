import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  TemplateRef,
  ViewChild
} from "@angular/core";
import { SkyddadeOmradenResponse, SkyddatOmrade } from "../../../../services/skyddade-omraden.service";
import {MatDialog} from "@angular/material/dialog";
import { ContextedDialogComponent } from "../../../../../../../lib/dialogs/contexted-dialog/contexted-dialog.component";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {SkogligtFel} from "../../analysis-report.component";

@Component({
  selector: "xp-skyddade-omraden-report",
  templateUrl: "./skyddade-omraden-report.component.html",
  styleUrls: ["./skyddade-omraden-report.component.scss"],
  animations: [
    trigger("detailExpand", [
      state("collapsed", style({height: "0px", minHeight: "0"})),
      state("expanded", style({height: "*"})),
      transition("expanded <=> collapsed", animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)")),
    ]),
  ],
})
export class SkyddadeOmradenReportComponent implements OnChanges {
  @Input() resultat: SkyddadeOmradenResponse;
  @Input() error: SkogligtFel;

  @ViewChild("dialogTemplate") dialogTemplate: TemplateRef<any>;

  chartData = [];
  dataSource: DataSource[] = [];
  totalAreaAlla: number; 
  columnsToDisplay = ["skyddsform", "totalAreal"];
  expandedElement: DataSource | null;

  dialogref = null;

  constructor(public dialog: MatDialog) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes["resultat"]) {
      this.updateChartData();
      this.generateDatasource();
    }
  }

  updateChartData() {
    if (!this.resultat) {
      return;
    }

    const skyddadeOmraden = this.resultat.totalareaSkyddsform;
    const skyddsformTotalAreal = new Map<string, number>();

    for (const omrade of skyddadeOmraden) {
      skyddsformTotalAreal.set(omrade.skyddsform, (skyddsformTotalAreal.get(omrade.skyddsform) || 0) + omrade.areaHa);
    }

    this.chartData = [];
    skyddsformTotalAreal.forEach((value, key) => {
      this.chartData.push({
        name: key,
        value: value
      });
    });
  }

  generateDatasource() {
    if (!this.resultat) {
      return;
    }

    const res: DataSource[] = [];

    const skyddsformer = new Set(this.resultat.skyddadeOmraden.map(omrade => omrade.skyddsform));

    for (const skyddsform of skyddsformer) {
      const filtered = this.resultat.skyddadeOmraden.filter(omrade => omrade.skyddsform === skyddsform);
      const filteredTotAreal = this.resultat.totalareaSkyddsform.filter(omrade => omrade.skyddsform === skyddsform);
      const countIds = this.resultat.skyddadeOmraden.reduce((count, omrade) => count + omrade.id.length, 0);
      if (filtered.length !== 0) {
        res.push({
          skyddsform: skyddsform,
          skyddadeOmraden: filtered,
          totalAreal: filteredTotAreal.reduce((sum, skyddatOmrade) => sum + skyddatOmrade.areaHa, 0)
        });
      }
    }
    this.dataSource = res;

    this.totalAreaAlla = this.resultat.totalareaSkyddadeOmraden;
  }
}

export interface DataSource {
  skyddsform: string;
  skyddadeOmraden: SkyddatOmrade[];
  totalAreal: number;
}
