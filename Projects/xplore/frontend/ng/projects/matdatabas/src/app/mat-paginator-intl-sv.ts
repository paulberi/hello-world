import { MatPaginatorIntl } from "@angular/material/paginator";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class MatPaginatorIntlSv extends MatPaginatorIntl {
  itemsPerPageLabel = "Antal per sida";
  firstPageLabel = "Första sidan";
  previousPageLabel = "Föregående";
  nextPageLabel = "Nästa";
  lastPageLabel = "Sista sidan";

  getRangeLabel = (page: number, pageSize: number, length: number) => {
    if (length === 0 || pageSize === 0) {
      return `0 av ${length}`;
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return `${startIndex + 1} - ${endIndex} av ${length}`;
  }
}
