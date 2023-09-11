import { Component, ContentChild, EventEmitter, Input, OnChanges, Output, SimpleChanges, TemplateRef } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { XpPage } from "../../../../../lib/ui/paginated-table/page";
import { MkAvtalSummary } from "../../model/avtalSummary";
import { uuid } from "../../model/uuid";

@Component({
  selector: "mk-fastighet-list",
  templateUrl: "./fastighet-list.component.html",
  styleUrls: ["./fastighet-list.component.scss"]
})
export class MkFastighetListComponent {
  @Input() page: XpPage<MkAvtalSummary>;
  @Input() pageSizeOptions: [number];
  @Input() selectedIndex: number = null;
  @Output() selectedIndexChange = new EventEmitter<number>();
  @Output() selectionChange = new EventEmitter<uuid[]>();
  @Output() pageChange = new EventEmitter<PageEvent>();

  @Input() selection: uuid[] = [];

  @ContentChild(TemplateRef) templateRef: TemplateRef<any>;

  isFastighetExpanded(i: number): boolean {
    return this.selectedIndex === i;
  }

  onFastighetClick(i: number) {
    this.selectedIndexChange.emit(this.selectedIndex === i ? null : i);
  }

  onFastighetPageChange(event: PageEvent) {
    this.pageChange.emit(event);
  }

  agareSaknas(avtal: MkAvtalSummary): boolean {
    return avtal.information.agareSaknas;
  }

  allFastigheterChecked() {
    return this.page.content.every(avtal => this.selection.find(s => s === avtal.fastighetId));

  }

  noFastighetChecked(): boolean {
    return this.page.content.every(avtal => !this.selection.find(s => s === avtal.fastighetId));
  }

  someFastigheterChecked() {
    return !this.noFastighetChecked() && !this.allFastigheterChecked()
  }

  fastigheterCheckAllChange(checked) {
    if (checked) {
      this.page.content.forEach(avtal => {
        if (!this.selection.find(s => s === avtal.fastighetId)) {
          this.selection.push(avtal.fastighetId)
        }
      })
    } else {
      this.page.content.forEach(avtal => {
        this.selection.splice(this.selection.indexOf(avtal.fastighetId), 1)
      })
    }
    this.selectionChange.emit(this.selection);
  }

  change(checked, fastighetId) {
    checked ? this.selection.push(fastighetId) : this.selection.splice(this.selection.indexOf(fastighetId), 1);
    this.selectionChange.emit(this.selection);
  }

  fastighetSelected(fastighetId) {
    return this.selection.find(f => f === fastighetId) ?? false;
  }
}
