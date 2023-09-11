import { Component, OnInit } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { Sort, SortDirection } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { Router } from "@angular/router";
import {XpPage} from "../../../../../../lib/ui/paginated-table/page";
import {ProjektInfo} from "../../../../../../../generated/markkoll-api";
import {ProjektService} from "../../../services/projekt.service";

@Component({
  selector: "mk-projektlista",
  templateUrl: "./projektlista.component.html",
  styleUrls: ["./projektlista.component.scss"],
})
export class MkProjektlistaComponent implements OnInit {
  readonly displayedColumns: string[] = ["namn", "projektTyp", "skapadDatum", "more"];

  projektPage: XpPage<ProjektInfo> = new XpPage<ProjektInfo>();

  pageIndex = 0;
  pageSize = 10;
  direction: SortDirection = "desc";
  active = "skapadDatum";
  search = null;

  selectedRow = 0;

  constructor(private projektService: ProjektService,
              private translate: TranslocoService,
              private router: Router) { }

  ngOnInit(): void {
    this.updatePage();
  }

  pageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;

    this.updatePage();
  }

  onSortChange(sort: Sort) {
    this.direction = sort.direction;
    this.active = sort.active;

    this.updatePage();
  }

  onSearchChange(search: String) {
    this.search = search;
    this.pageIndex = 0;

    this.updatePage();
  }

  updatePage() {
    this.projektService
        .getProjektPage(this.pageIndex, this.pageSize, this.direction, this.active, this.search)
        .subscribe(projektPage => this.projektPage = projektPage);
  }

  highlightRow(rowId: number) {
    this.selectedRow = rowId;
  }

  navigateToProjekt(projekt: ProjektInfo) {
    this.router.navigate(["/projekt/", projekt.projektTyp, projekt.id, "avtal"]);
  }
}
