import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MkProjektloggPresenter } from "./projektlogg.presenter";
import { ProjektLoggItem, ProjektLoggType } from "../../../../../../../generated/markkoll-api";
import { Sort, SortDirection } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";

@Component({
  providers: [MkProjektloggPresenter],
  selector: "mk-projektlogg",
  templateUrl: "./projektlogg.component.html",
  styleUrls: ["./projektlogg.component.scss"],
})
export class MkProjektloggComponent {
  @Input() projektlogg: ProjektLoggItem[] = [];

  @Input() title = "Datum";

  @Input() sortDirection: SortDirection = "desc";

  @Output() filDownload = new EventEmitter<ProjektLoggItem>();

  @Output() sortDirectionChange = new EventEmitter<SortDirection>();

  constructor(private presenter: MkProjektloggPresenter,
              private translate: TranslocoService) {}

  itemTitle(item: ProjektLoggItem): string {
    return this.presenter.itemTitle(item);
  }

  itemText(item: ProjektLoggItem): string {
    return this.presenter.itemText(item);
  }

  isDokumenthandelse(item: ProjektLoggItem): boolean {
    return item.projektLoggType === ProjektLoggType.AVTALHANDELSE ||
           item.projektLoggType === ProjektLoggType.INFOBREVHANDELSE;
  }

  onSortChange(event: Sort) {
    this.sortDirection = event.direction;
    this.sortDirectionChange.emit(this.sortDirection);
  }

  showSortDirection(): string {
    if (this.sortDirection === "desc") {
      return this.translate.translate<string>("xp.common.descending").toLowerCase();
    } else if (this.sortDirection === "asc") {
      return this.translate.translate<string>("xp.common.ascending").toLowerCase();
    } else {
      return "";
    }
  }
}
