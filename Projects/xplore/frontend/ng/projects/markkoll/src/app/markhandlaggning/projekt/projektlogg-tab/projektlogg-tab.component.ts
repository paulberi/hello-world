import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { PageEvent } from "@angular/material/paginator";
import { SortDirection } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { ProjektLoggFilter, ProjektLoggItem } from "../../../../../../../generated/markkoll-api";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { MkProjektloggTabPresenter } from "./projektlogg-tab.presenter";

@Component({
  providers: [MkProjektloggTabPresenter],
  selector: "mk-projektlogg-tab-ui",
  templateUrl: "./projektlogg-tab.component.html",
  styleUrls: ["./projektlogg-tab.component.scss"],
})
export class MkProjektloggTabComponent implements OnInit {
  @Input() projektloggPage: XpPage<ProjektLoggItem>;

  @Input() sortDirection: SortDirection = "desc";

  @Output() filDownload = new EventEmitter<ProjektLoggItem>();

  @Output() filterChange = this.presenter.filterChange;

  @Output() pageChange = new EventEmitter<PageEvent>();

  @Output() sortDirectionChange = new EventEmitter<SortDirection>();

  readonly pageSizeOptions = [5, 10, 20];
  readonly filter = Object.values(ProjektLoggFilter);

  constructor(private presenter: MkProjektloggTabPresenter,
              private translate: TranslocoService) {}

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  ngOnInit() {
    this.presenter.initializeForm();
  }
}
