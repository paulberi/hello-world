import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Version } from "../../../../../../../generated/markkoll-api";
import { DialogService } from "../../../services/dialog.service";

/**
 * Skapa ett nytt projekt i Markkoll.
 */
@Component({
  selector: "mk-projektversion",
  templateUrl: "./projektversion.component.html",
  styleUrls: ["./projektversion.component.scss"]
})

export class MkProjektversionComponent {

  /**
   * Projektversion
   */
  @Input() projektversion: Version;

  /**
   * Är detta den aktuella versionen
   */
  @Input() isCurrent = false;

  /**
   * Är detta den föregående versionen
   */
  @Input() isPrevious = false;

  /**
   * Event när denna projektversionen ska återställas till den aktuella versionen
   */
  @Output() restoreVersionChange = new EventEmitter<Version>();

  /**
   * Event när denna projektversion skall tas bort
   */
  @Output() deleteVersionChange = new EventEmitter<Version>();

  isDeleting = false;
  isRestoring = false;

  constructor(private dialogService: DialogService) { }

  async restoreVersion() {
    this.dialogService.restoreVersionDialog(this.projektversion.filnamn).subscribe(() => {
      this.restoreVersionChange.emit(this.projektversion);
      this.isRestoring = !this.isRestoring;
    });
  }

  deleteVersion() {
    this.dialogService.deleteVersionDialog(this.projektversion.filnamn).subscribe(() => {
      this.deleteVersionChange.emit(this.projektversion);
      this.isDeleting = !this.isDeleting;
    });
  }
}
