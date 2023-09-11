import { EventEmitter } from "@angular/core";
import { FormControl } from "@angular/forms";
import { Dokumentmall } from "../../../../../../../generated/markkoll-api";

export class ProjektdokumentTablePresenter {
  selectedDokumentmallControl: FormControl;
  selectedDokumentChange = new EventEmitter<Dokumentmall>();

  init(dokumentmallar: Dokumentmall[]) {
    var selectedDokumentmall;
    if (!dokumentmallar || dokumentmallar.length === 0) {
        selectedDokumentmall = null;
    }
    else {
        selectedDokumentmall = dokumentmallar.find(d => d.selected);
    }

    this.selectedDokumentmallControl = new FormControl(selectedDokumentmall);
    this.selectedDokumentmallControl.valueChanges.subscribe(dokumentmall => {
      dokumentmall.selected = true;
      this.selectedDokumentChange.emit(dokumentmall);
    });
  }
}
