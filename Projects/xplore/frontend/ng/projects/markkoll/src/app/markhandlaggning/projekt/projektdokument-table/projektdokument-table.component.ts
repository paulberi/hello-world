import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatRadioModule } from "@angular/material/radio";
import { MatTableModule } from "@angular/material/table";
import { TranslocoModule } from "@ngneat/transloco";
import { Dokumentmall } from "../../../../../../../generated/markkoll-api";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { uuid } from "../../../model/uuid";
import { DokumentService } from "../../../services/dokument.service";
import { ProjektdokumentTablePresenter } from "./projektdokument-table.presenter";

@Component({
  selector: 'mk-projektdokument-table',
  templateUrl: './projektdokument-table.component.html',
  styleUrls: ['./projektdokument-table.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatRadioModule,
    TranslocoModule,
    MkPipesModule
  ],
  providers: [ProjektdokumentTablePresenter]
})
export class ProjektdokumentTableComponent implements OnChanges {
  @Input() dataSource: Dokumentmall[];
  @Input() title: string;

  @Output() delete = new EventEmitter<Dokumentmall>();
  @Output() selectedDokumentChange = this.presenter.selectedDokumentChange;

  readonly columns = ["avtal", "uppladdat", "andrat", "selected", "download"];

  constructor(private presenter: ProjektdokumentTablePresenter,
              private dokumentService: DokumentService) { }

  ngOnChanges() {
    this.presenter.init(this.dataSource);
  }

  getDokumentmall(kundId: string, dokument: Dokumentmall) {
    this.dokumentService.getDokumentmall(kundId, dokument.id);
  }

  get selectedDokumentmallControl(): FormControl {
    return this.presenter.selectedDokumentmallControl;
  }
}
