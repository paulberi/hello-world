import { Component, OnInit } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { SortDirection } from "@angular/material/sort";
import { Router } from "@angular/router";
import { map } from "rxjs/operators";
import { ProjektRsp as Projekt, PubliceringStatus} from "../../../../../../generated/samrad-api";
import { XpPage } from "../../../../../lib/ui/paginated-table/page";
import { InitieraKundService } from "../../services/initiera-kund.service";
import { SamradService } from "../../services/samrad-admin.service";
import { paginate } from "../../utils/paginate";

@Component({
  selector: "sr-arkiverade-samrad-lista",
  templateUrl: "./arkiverade-samrad-lista.component.html",
  styleUrls: ["./arkiverade-samrad-lista.component.scss"],
})
export class ArkiveradeSamradListaComponent implements OnInit {
  readonly displayedColumns: string[] = ["samråd", "more"];

  samradPage: XpPage<Projekt> = new XpPage<Projekt>();

  pageIndex = 0;
  pageSize = 10;
  direction: SortDirection = "desc";
  active = "samråd";

  selectedRow = 0;

  constructor(private router: Router, private samradService: SamradService, private initieraKundService: InitieraKundService) {}

  ngOnInit(): void {
    this.updatePage();
  }

  updatePage() {
    this.samradService
      .getSamrad$(this.initieraKundService.kundInfo.id)
      .pipe(
        map((samrad) =>
          samrad.filter((samrad) => samrad.publiceringStatus === PubliceringStatus.ARKIVERAD)
        )
      )
      .subscribe(
        (samrad: Projekt[]) =>
          (this.samradPage = paginate(samrad, this.pageIndex, this.pageSize))
      );
  }

  pageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;

    this.updatePage();
  }

  navigateToProjekt(samrad: Projekt) {
    this.router.navigate(["admin", samrad.slug]);
  }

  highlightRow(rowId: number) {
    this.selectedRow = rowId;
  }
}
