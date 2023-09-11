import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { Sort } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { flatMap } from "rxjs/operators";
import { ElnatBilaga, ElnatVarderingsprotokoll } from "../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { uuid } from "../../model/uuid";
import { DialogService } from "../../services/dialog.service";
import { FilService } from "../../services/fil.service";
import { SortService } from "../../services/sort.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { MkBilagorComponent } from "./bilagor.component";
import { MkAddBilagaEvent } from "./bilagor.presenter";

@Component({
  selector: "mk-bilagor",
  templateUrl: "bilagor.container.html",
})
export class MkBilagorContainer implements OnInit {
  constructor(private varderingsprotokollService: ElnatVarderingsprotokollService,
              private filService: FilService,
              private dialogService: DialogService,
              private notificationService: XpNotificationService,
              private translate: TranslocoService,
              private sortService: SortService) {}

  @Input() projektId: uuid;
  @Input() varderingsprotokollId: uuid;

  @Output() bilagorChange = new EventEmitter<void>();

  @ViewChild(MkBilagorComponent, { static: true}) component: MkBilagorComponent;

  bilagor: ElnatBilaga[] = [];

  sort: Sort = {
    active: "",
    direction: ""
  };

  ngOnInit() {
    this.varderingsprotokollService.getBilagor(this.projektId, this.varderingsprotokollId)
        .subscribe(bilagor => this.bilagor = bilagor);
  }

  onAddBilaga(event: MkAddBilagaEvent) {
    this.varderingsprotokollService
        .addBilaga(this.projektId, this.varderingsprotokollId, event.fil, event.kategori)
        .subscribe(bilaga => {
          this.bilagor = [...this.bilagor, bilaga];
          this.bilagor = this.sortService.sortBilagor(this.bilagor, this.sort);
          this.bilagorChange.emit();
          this.notificationService.success(this.translate.translate("mk.bilagor.addedBilaga"));
          this.component.resetForm();
        });
  }

  onRemoveBilaga(bilagaId: uuid) {
    const filNamn = this.bilagor.find(b => b.id === bilagaId).fil.filnamn;

    this.dialogService.confirmRemoveBilagaDialog(filNamn)
      .pipe(flatMap(() => this.varderingsprotokollService.removeBilaga(this.projektId,
        this.varderingsprotokollId, bilagaId)))
      .subscribe(() => {
        this.bilagor = this.bilagor.filter(b => b.id !== bilagaId);
        this.bilagorChange.emit();
        this.notificationService.success(this.translate.translate("mk.bilagor.removedBilaga"));
      });
  }

  onDownloadBilaga(filId: uuid) {
    this.filService.getFilData(filId).subscribe();
  }

  onSortChange(sort: Sort) {
    this.sort = sort;
    this.bilagor = this.sortService.sortBilagor(this.bilagor, sort);
  }
}
