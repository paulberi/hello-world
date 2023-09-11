import {Component, Input, AfterViewInit} from "@angular/core";
import {MatningarGraf} from "../granskning/matningar-graf";
import {RapportGraf} from "../services/rapport.service";

@Component({
  selector: "mdb-rapport-graf",
  template: `
    <div class="main-content">
      <h3 style="width: 100%">{{grafData.rubrik}}</h3>
      <h4 style="width: 100%">{{grafData.information}}</h4>
      <div class="graphs" id="{{divName}}" [style.height]="graph_height" style="width: 100%"></div>
    </div>
  `,
  styles: [`
    .main-content {
      display: block;
      text-align: center;
      font-size: 12px;
      width: 18cm;
    }

    .center {
      margin-left: auto;
      margin-right: auto;
    }

    .graphs {
      font-size: 10px;
    }
  `]
})

export class RapportGrafComponent implements AfterViewInit {
  @Input() grafData: RapportGraf;
  @Input() divName: string;
  graf: MatningarGraf;

  ngAfterViewInit(): void {
    this.graf = new MatningarGraf(this.divName);
    this.graf.scrollbarXEnabled = false;
    this.graf.scrollbarYEnabled = false;

    this.grafData.matningar.forEach(m => {
      m.visible = true;
      this.graf.addMatningstyp(m);
    });
    this.grafData.referensdata.forEach(r => {
      r.visible = true;
      this.graf.addReferensdata(r);
    });
    this.grafData.gransvarden.forEach(g => {
      this.graf.addGransvarde(g);
    });

    this.graf.yAxisWidth = 30;
    this.graf.legendHeight = 15;
    this.graf.extendDateAxisRangeToShowToday();
  }

  get graph_height(): string {
    const height = 8 + 1.2 * (this.numOfMatningar() / 2);
    return Math.min(height, 16) + "cm";
  }

  private numOfMatningar(): number {
    return  this.grafData.matningar.length +
            this.grafData.referensdata.length +
            this.grafData.gransvarden.length;
  }
}
