import { MatPaginatorIntl } from "@angular/material/paginator";
import { Injectable } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";

@Injectable({
  providedIn: "root"
})
export class XpMatPaginatorIntl extends MatPaginatorIntl {
  itemsPerPageLabel = this.translateService.translate("xp.paginator.itemsPerPageLabel");
  firstPageLabel = this.translateService.translate("xp.paginator.firstPageLabel");
  previousPageLabel = this.translateService.translate("xp.paginator.previousPageLabel");
  nextPageLabel = this.translateService.translate("xp.paginator.nextPageLabel");
  lastPageLabel = this.translateService.translate("xp.paginator.lastPageLabel");

  constructor(private translateService: TranslocoService) {
    super();
  }

  getRangeLabel = (page: number, pageSize: number, length: number) => {
    if (length === 0 || pageSize === 0) {
      return `0 ${this.translateService.translate("xp.paginator.of")} ${length}`;
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return `${startIndex + 1} - ${endIndex} ${this.translateService.translate("xp.paginator.of")} ${length}`;
  }
}
