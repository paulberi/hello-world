import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { MkIntrang } from "../../model/intrang";
import { MkIntrangPresenter } from "./intrang.presenter";

/**
 * Visar aktuellt intrång på fastigheten och
 * hur mycket markägaren ska få i ersättning.
 */
@Component({
  providers: [MkIntrangPresenter],
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "mk-intrang",
  templateUrl: "./intrang.component.html",
  styleUrls: ["./intrang.component.scss"]
})
export class MkIntrangComponent implements OnChanges {

  /**
   * Titel högst upp.
   **/
  @Input() title = "Intrångsersättning";

  /**
   * Information om intrång på fastighet.
   */
  @Input() intrang: MkIntrang;

  /**
   * Ersättning till markägare i SEK.
   */
  @Input() ersattning: number;

  /**
   * Etikett för spara-knapp.
   */
  @Input() saveLabel = "Spara";

  /**
   * Om indikator för sparning av ändringar ska visas
   */
  @Input() isSaving = false;

  /**
   * Event när användaren sparar med ersättning som data.
   **/
  @Output() intrangChange: EventEmitter<number> = this.mkIntrangPresenter.onSubmit;

  constructor(private mkIntrangPresenter: MkIntrangPresenter) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      this.mkIntrangPresenter.initializeForm(this.intrang, this.ersattning);
    }
  }

  save() {
    this.mkIntrangPresenter.submit();
  }

  canSave(): boolean {
    return this.mkIntrangPresenter.canSave();
  }

  get form(): UntypedFormGroup {
    return this.mkIntrangPresenter.form;
  }
}
