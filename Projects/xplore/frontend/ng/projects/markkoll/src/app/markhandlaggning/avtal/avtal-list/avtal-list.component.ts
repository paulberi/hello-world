import { Component, EventEmitter, Input, Output, SimpleChanges } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { OptionItem } from "../../../common/filter-option/filter-option.component";
import { MkAvtalSummary } from "../../../model/avtalSummary";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { uuid } from "../../../model/uuid";
import { MkAvtalListPresenter } from "./avtal-list.presenter";
import { PageTyp } from "../../../model/avtalPageEvent";
import { EditElnatVp, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { EditingContentsEvent } from "../avtal-actions/avtal-actions.container";

@Component({
  providers: [MkAvtalListPresenter],
  selector: "mk-avtal-list-ui",
  templateUrl: "./avtal-list.component.html",
  styleUrls: ["./avtal-list.component.scss"]
})
export class MkAvtalListComponent {
  @Input() projektId: uuid;
  @Input() projektTyp: ProjektTyp = ProjektTyp.FIBER;
  @Input() tabIndex = 0;
  @Input() fastighetPage: XpPage<MkAvtalSummary> = new XpPage<MkAvtalSummary>();
  @Input() fastighetIndex: number = null;
  @Input() samfallighetPage: XpPage<MkAvtalSummary> = new XpPage<MkAvtalSummary>();
  @Input() samfallighetIndex: number = null;
  @Input() pageSizeOptions = [10, 20, 50];
  @Input() statusFilterOptions: OptionItem[] = this.getFilterOptions();
  @Input() avtalsfilter: MkAvtalsfilter;
  @Input() hamtaMarkagareDisabled = false;
  @Input() skapaAvtalDisabled = false;
  @Input() showCompareToPreviousVersion = false;
  @Input() numOfAvtalSelected = 0;
  @Input() registerenheter = [];
  @Input() editElnatVps = [];
  @Input() editingContents = false;

  @Output() hamtaMarkagareClick = new EventEmitter<void>();
  @Output() fastighetIndexChange = new EventEmitter<number>();
  @Output() samfallighetIndexChange = new EventEmitter<number>();
  @Output() genericPageChange = this.presenter.genericPageChange;

  @Output() fastighetRemove = new EventEmitter<uuid>();
  @Output() fastighetChange = new EventEmitter<uuid>();
  @Output() samfallighetRemove = new EventEmitter<uuid>();
  @Output() samfallighetChange = new EventEmitter<uuid>();

  @Output() selectionStatusChange = new EventEmitter<void>();
  @Output() registerenhetIdsChange = new EventEmitter<uuid[]>();
  @Output() editingContentsChange = new EventEmitter<EditingContentsEvent>();

  @Output() saveElnatVp = new EventEmitter<EditElnatVp[]>();

  readonly PageTyp = PageTyp;
  pageSize = this.pageSizeOptions[0];
  fastSelection = [];
  samfSelection = [];

  constructor(private presenter: MkAvtalListPresenter) { }

  onFilterChange(filter: MkAvtalsfilter) {
    this.presenter.onGenericPageChange(0, this.pageSize, filter, null);
  }

  onGenericPageChange(pageEvent: PageEvent, type) {
    this.pageSize = pageEvent.pageSize;
    this.presenter.onGenericPageChange(pageEvent.pageIndex, pageEvent.pageSize, this.avtalsfilter, type);
  }

  onResetFilter() {
    this.onFilterChange({ status: null, search: null });
  }

  getFilterOptions(): OptionItem[] {
    return this.presenter.filterOptions();
  }

  fastighetNum(): number {
    return this.fastighetPage?.totalElements || 0;
  }

  samfallighetNum(): number {
    return this.samfallighetPage?.totalElements || 0;
  }

  hasAvtalSelected(): boolean {
    return this.numOfAvtalSelected > 0;
  }

  updateFastSelection(selection: uuid[]) {
    this.fastSelection = selection;
    this.registerenhetIdsChange.emit(this.registerenhetIds);
  }

  updateSamfSelection(selection: uuid[]) {
    this.samfSelection = selection;
    this.registerenhetIdsChange.emit(this.registerenhetIds);
  }

  get avtalsinfo(): MkAvtalSummary[] {
    return this.fastighetPage?.content;
  }

  get registerenhetIds() {
    return [...this.fastSelection, ...this.samfSelection];
  }
}
