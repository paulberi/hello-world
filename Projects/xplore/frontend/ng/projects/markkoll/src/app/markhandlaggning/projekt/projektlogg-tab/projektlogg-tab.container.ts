import { Component, Input, OnInit } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { SortDirection } from "@angular/material/sort";
import { ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { Avtalhandelse, Infobrevhandelse, ProjektLoggFilter, ProjektLoggItem, ProjektLoggType } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { uuid } from "../../../model/uuid";
import { AvtalsjobbService } from "../../../services/avtalsjobb.service";
import { InfobrevsjobbService } from "../../../services/infobrevsjobb.service";
import { ProjektService } from "../../../services/projekt.service";

@Component({
  selector: "mk-projektlogg-tab",
  templateUrl: "./projektlogg-tab.container.html"
})
export class MkProjektloggtabContainer implements OnInit {

  projektId: uuid = this.activatedRoute.parent.snapshot.params.projektId;

  pageNum = 0;
  pageSize = 5;
  filter: ProjektLoggFilter[] = [];
  sortDirection: SortDirection = "desc";

  projektloggPage: XpPage<ProjektLoggItem> = new XpPage<ProjektLoggItem>();

  constructor(private activatedRoute: ActivatedRoute,
              private projektService: ProjektService,
              private avtalsjobbService: AvtalsjobbService,
              private infobrevsjobbService: InfobrevsjobbService) {}

  ngOnInit() {
    this.updatePage();
  }

  onFilDownload(item: ProjektLoggItem) {
    switch (item.projektLoggType) {
      case ProjektLoggType.AVTALHANDELSE:
        this.avtalsjobbService.getData(this.projektId, (item as Avtalhandelse).avtalsjobbId);
        break;
      case ProjektLoggType.INFOBREVHANDELSE:
        this.infobrevsjobbService.getData(this.projektId, (item as Infobrevhandelse).infobrevsjobbId);
        break;
      default:
        throw new Error("Okänd projektloggstyp: " + item.projektLoggType);
    }
  }

  onFilterChange(filter: ProjektLoggFilter[]) {
    this.filter = filter;
    this.pageNum = 0;
    this.updatePage();
  }

  onPageChange(event: PageEvent) {
    this.pageNum = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePage();
  }

  onSortDirectionChange(sortDirection: SortDirection) {
    this.sortDirection = sortDirection;
    this.updatePage();
  }

  private updatePage() {
    this.projektService.getProjektloggPage(this.projektId, this.pageNum, this.pageSize, this.filter,
      this.sortDirection).subscribe(projektloggPage => this.projektloggPage = projektloggPage);
  }
}

