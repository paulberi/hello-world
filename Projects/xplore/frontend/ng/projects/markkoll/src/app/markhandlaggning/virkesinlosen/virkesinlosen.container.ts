import { Component, EventEmitter, Input, Output } from "@angular/core";
import FileSaver from "file-saver";
import { TillvaratagandeTyp } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { AvtalService } from "../../services/avtal.service";
import { DokumentService } from "../../services/dokument.service";

@Component({
  selector: "mk-virkesinlosen",
  templateUrl: "./virkesinlosen.container.html"
})
export class MkVirkesinlosenContainer {
  @Input() projektId: uuid;
  @Input() fastighetId: uuid;

  @Input() skogligVardering = false;
  @Input() tillvaratagandeTyp: TillvaratagandeTyp = TillvaratagandeTyp.EJBESLUTAT;
  @Input() rotnetto = 0;
  @Input() egetTillvaratagande = 0;

  @Output() skogligVarderingChange = new EventEmitter<void>();

  constructor(private avtalService: AvtalService, private dokumentService: DokumentService) {}

  onSkogligVarderingChange(skogligVardering: boolean) {
    this.avtalService
        .setSkogsfastighet(this.projektId, this.fastighetId, skogligVardering)
        .subscribe(() => {
          this.skogligVardering = skogligVardering;
          this.skogligVarderingChange.emit();
        });
  }

  onTillvaratagandeTypChange(tillvaratagandeTyp: TillvaratagandeTyp) {
    this.avtalService
        .setTillvaratagandeTyp(this.projektId, this.fastighetId, tillvaratagandeTyp)
        .subscribe(() => this.tillvaratagandeTyp = tillvaratagandeTyp);
  }

  onExportXlsxClick() {
    // TODO User has exported Haglöf HMS, add log entry
    this.dokumentService.getVarderingSkogsmarkXlsx(this.projektId, this.fastighetId).subscribe(
      file => FileSaver.saveAs(file.blob, file.name)
    );
  }
}
