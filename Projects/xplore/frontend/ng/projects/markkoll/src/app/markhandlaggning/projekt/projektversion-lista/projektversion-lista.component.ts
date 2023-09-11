import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {Version} from "../../../../../../../generated/markkoll-api";

@Component({
  selector: "mk-projektversion-lista",
  templateUrl: "./projektversion-lista.component.html",
  styleUrls: ["./projektversion-lista.component.scss"]
})
export class MkProjektversionListaComponent {

  /**
   * Lista med projektversioner
   */
  @Input() versioner: Version[] = [];

  /**
   * Event när en projektversion ska återställas
   */
  @Output() restoreVersionChange = new EventEmitter<Version>();

  /**
   * Event när en projektversion ska tas bort
   */
  @Output() deleteVersionChange = new EventEmitter<Version>();

  onRestore(version: Version) {
    this.restoreVersionChange.emit(version);
  }

  onDelete(version: Version) {
    this.deleteVersionChange.emit(version);
  }

}
