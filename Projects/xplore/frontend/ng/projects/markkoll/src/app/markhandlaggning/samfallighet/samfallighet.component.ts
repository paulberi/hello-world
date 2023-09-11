import { animate, state, style, transition, trigger } from "@angular/animations";
import { Component, EventEmitter, OnChanges, Output, SimpleChanges, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { XpMessageSeverity } from "../../../../../lib/ui/feedback/message/message.component";
import { MkAgare } from "../../model/agare";
import { MkOmbudAddComponent } from "../ombud-add/ombud-add.component";
import { RegisterenhetComponent } from "../registerenhet/registerenhet.component";
import { MkSamfallighetPresenter } from "./samfallighet.presenter";

/**
 * Redigerbar samfällighetsinformation för ett avtal.
 */
@Component({
  selector: "mk-samfallighet-ui",
  animations: [
    trigger("expandOmbud", [
      state("true", style({ height: "*" })),
      state("false", style({height: "0px", minHeight: "0"})),
      transition("false <=> true", animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)"))
    ])
  ],
  templateUrl: "./samfallighet.component.html",
  styleUrls: ["./samfallighet.component.scss"],
  providers: [MkSamfallighetPresenter]
})
export class MkSamfallighetComponent extends RegisterenhetComponent implements OnChanges {
  /** Event när intrångsersättning sparas. */
  @Output() intrangChange = new EventEmitter<number>();

  /** Event när användaren sparar ombud. */
  @Output() ombudChange = new EventEmitter<MkAgare>();

  dataSource: MatTableDataSource<string>;
  pageSizeOptions = [10];

  readonly MessageSeverity = XpMessageSeverity;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MkOmbudAddComponent) addOmbudComponent: MkOmbudAddComponent;

  constructor(private presenter: MkSamfallighetPresenter) {
    super();
  }

  onResetForm() {
    this.addOmbudComponent.resetForm();
  }

  isAvtalsstatusEnabled(): boolean {
    return this.hasOmbud() && !this.noAgareChecked();
  }

  isSkapaAvtalEnabled(): boolean {
    return this.hasOmbud() && !this.noAgareChecked();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.avtal) {
      this.dataSource = new MatTableDataSource(this.avtal?.samfallighet?.delagandeFastigheter);
      this.dataSource.paginator = this.paginator;
    }
  }

  hasDelagandeFastigheter(): boolean {
    return this.delagandeFastigheter.length > 0;
  }

  get delagandeFastigheter(): string[] {
    return this.avtal?.samfallighet?.delagandeFastigheter || [];
  }

  get isMittlinjeRedovisad(): boolean {
    return this.presenter.isMittlinjeRedovisad(this.avtal?.avtalskarta);
  }
}
