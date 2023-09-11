import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit, Output,
  SimpleChanges,
  ViewChild
} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Matrunda, MatrundaService, RapporteringMatningstyp} from "../../services/matrunda.service";
import {MatPaginator} from "@angular/material/paginator";
import {Matning, Matobjekt, MatobjektService} from "../../services/matobjekt.service";
import {ViewportScroller} from "@angular/common";
import {MatCheckboxChange} from "@angular/material/checkbox";

@Component({
  selector: "mdb-rapportering-matningstyper",
  template: `
    <div #scrolltarget id="scrolltarget"></div>
    <h3 *ngIf="matrunda">Mätrunda: {{matrunda.namn}}</h3>
    <h3 *ngIf="matobjekt">Mätobjekt: {{matobjekt.namn}}</h3>
    <mat-checkbox (change)="toggleRapporterade($event)">Visa rapporterade</mat-checkbox>
    <mat-checkbox (change)="toggleAutomatiska($event)">Visa automatiska</mat-checkbox>
    <div>
      <div class="main-content">
        <span *ngIf="loading || !hasSenasteMatningar">Hämtar mätvärden...</span>
        <span *ngIf="!loading && viewData.length === 0">Alla mätvärden är rapporterade.</span>
        <span *ngIf="!loading && hasSenasteMatningar">
          <div *ngFor="let matningstyp of viewData; let last = last">
            <mdb-rapportering-matningstyp [matningstyp]="matningstyp" [statusStore]="statusStore"
                                          [rapportor]="rapportor"
                                          [senasteMatningarStore]="senasteMatningarStore"
                                          (isDirty)="setDirty(matningstyp.matningstypId, $event)">
            </mdb-rapportering-matningstyp>
            <hr *ngIf="!last"/>
          </div>
        </span>
      </div>
      <mat-paginator [length]="matningarCount()" [pageSize]="10" [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }
    mat-checkbox {
      padding: 0.3rem;
    }
  `]
})

export class RapporteringMatningstyperComponent implements AfterViewInit, OnChanges {
  matningstyper: RapporteringMatningstyp[] = [];
  viewData: RapporteringMatningstyp[] = [];
  loading = true;
  matrunda: Matrunda;
  matobjekt: Matobjekt;

  @Input() matrundaId: number;
  @Input() matobjektId: number;
  @Input() startDate: string;
  @Input() rapportor: string;
  @Input() matobjektInArea: number[];
  @Output() isDirty = new EventEmitter<boolean>();
  showRapporterad = false;
  showAutomatiska = false;

  isDirtyMap: Map<number, boolean> = new Map<number, boolean>();
  statusStore: Map<number, string> = new Map<number, string>();
  senasteMatningarStore: Map<number, Matning> = new Map<number, Matning>();
  hasSenasteMatningar = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private route: ActivatedRoute,
              private matobjektService: MatobjektService,
              private viewportScroller: ViewportScroller,
              private matrundaService: MatrundaService) {
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(() => {
      this.isDirtyMap = new Map<number, boolean>();
      this.isDirty.emit(false);
      this.updateViewData();
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.matrundaId || changes.matobjektId || changes.startDate || changes.rapportor) {
      this.reloadData();
    } else if (changes.showRapporterad || changes.matobjektInArea) {
      this.updateViewData();
    }
  }

  resetDirtyState() {
    this.isDirtyMap = new Map<number, boolean>();
    this.isDirty.emit(false);
  }

  setDirty(id: number, isDirty: boolean) {
    this.isDirtyMap.set(id, isDirty);
    this.isDirty.emit(this.hasDirtyForm());
  }

  hasDirtyForm() {
    let hasDirty = false;
    this.isDirtyMap.forEach((isDirty, id) => {
      if (isDirty) {
        hasDirty = true;
      }
    });

    return hasDirty;
  }

  private reloadData() {
    this.loading = true;
    this.matningstyper = [];
    this.viewData = [];
    this.senasteMatningarStore = new Map<number, Matning>();
    this.hasSenasteMatningar = false;

    if (this.matobjektId) {
      this.matrunda = null;

      this.matobjektService.getSenasteMatningar(this.matobjektId).subscribe(map => this.setSenasteMatningar(map));

      this.matobjektService.get(this.matobjektId).subscribe(matobjekt => {
        this.matobjekt = matobjekt;
      });

      this.matrundaService.getMatrundaFromMatobjektMatningstyper(this.matobjektId, this.startDate).subscribe(matningstyper => {
        this.loading = false;
        this.matningstyper = matningstyper;

        this.updateViewData();
        setTimeout(() => {
          this.viewportScroller.scrollToAnchor("scrolltarget");
        });
      });
    } else if (this.matrundaId) {
      this.matrundaService.getSenasteMatningar(this.matrundaId).subscribe(map => this.setSenasteMatningar(map));
      this.matobjekt = null;

      this.matrundaService.get(this.matrundaId).subscribe(matrunda => {
        this.matrunda = matrunda;
      });

      this.matrundaService.getMatrundaMatningstyper(this.matrundaId, this.startDate).subscribe(matningstyper => {
        this.loading = false;
        this.matningstyper = matningstyper;

        this.updateViewData();

        setTimeout(() => {
          this.viewportScroller.scrollToAnchor("scrolltarget");
        });
      });
    } else {
      this.loading = false;
      this.matningstyper = null;
      this.matrunda = null;
      this.matobjekt = null;
    }
  }

  private setSenasteMatningar(map) {
    for (const key of Object.keys(map)) {
      this.senasteMatningarStore.set(parseInt(key, 10), map[key]);
    }
    this.hasSenasteMatningar = true;
  }

  private updateViewData() {
    const indexStart = this.paginator.pageSize * this.paginator.pageIndex;
    const indexStop = Math.min(this.paginator.pageSize * (this.paginator.pageIndex + 1), this.matningstyper.length);

    const matningstyper = this.filterMatningstyper(this.matningstyper);

    this.viewData = matningstyper.slice(indexStart, indexStop);
  }

  matningarCount() {
    return this.filterMatningstyper(this.matningstyper).length;
  }

  private filterMatningstyper(matningstyper) {
    if (!this.showRapporterad) {
      matningstyper = matningstyper.filter(m => !this.isRapporterad(m));
    }

    if (!this.showAutomatiska) {
      matningstyper = matningstyper.filter(m => !this.isAutomatisk(m));
    }

    if (this.matobjektInArea != null) {
      matningstyper = matningstyper.filter(m => this.matobjektInArea.includes(m.matobjektId));
    }

    return matningstyper;
  }

  isRapporterad(matningstyp: RapporteringMatningstyp) {
    if (matningstyp.senasteAvlastDatum == null) {
      return false;
    }

    const startDate = new Date(this.startDate);
    const avlastDate = new Date(matningstyp.senasteAvlastDatum);

    return avlastDate.getTime() > startDate.getTime();
  }

  toggleRapporterade(event: MatCheckboxChange) {
    this.showRapporterad = event.checked;
    this.updateViewData();
  }

  isAutomatisk(matningstyp: RapporteringMatningstyp) {
    const giltigtInstrument = matningstyp.instrument && matningstyp.instrument.length > 0;
    return matningstyp.automatiskInrapportering && giltigtInstrument;
  }

  toggleAutomatiska(event: MatCheckboxChange) {
    this.showAutomatiska = event.checked;
    this.updateViewData();
  }

}
