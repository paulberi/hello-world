import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from "@angular/core";
import { Kund } from "../../../../../generated/kundconfig-api";
import { XpPage } from "../../../../lib/ui/paginated-table/page"
import { roles } from "../roles";

@Component({
  selector: "adm-kundvy",
  templateUrl: "./kundvy.component.html",
  styleUrls: ["./kundvy.component.scss"],
})
export class AdmKundvyComponent {
  @Input() page: XpPage<Kund>;
  @Input() roles: string[];
  @Input() selectedIndex: number = null;

  @Output() kundDelete = new EventEmitter<string>();
  @Output() kundChange = new EventEmitter<Kund>();
  @Output() kundAdd = new EventEmitter();
  @Output() pageChange = new EventEmitter();
  @Output() resetGeofenceRulesClick = new EventEmitter();

  readonly pageSizeOptions = [10, 20, 50];

  constructor() {}

  canCreate() {
    return this.roles.includes(roles.GLOBAL_ADMIN);
  }

  isKundExpanded(kundIndex: number) {
    return this.selectedIndex === kundIndex;
  }

  onKundClick(kundIndex: number) {
    this.selectedIndex = this.selectedIndex === kundIndex ? null : kundIndex;
  }
}
