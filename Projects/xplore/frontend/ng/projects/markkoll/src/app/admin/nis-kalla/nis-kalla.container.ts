import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { Observable } from "rxjs";
import { Kund, NisKalla } from "../../../../../../generated/markkoll-api";
import { MkInstallningarService } from "../../services/installningar.service";

@Component({
  selector: "mk-nis-kalla",
  templateUrl: "./nis-kalla.container.html",
  providers: []
})
export class MkNisKallaContainerComponent implements OnChanges {
  @Input() kund: Kund;

  nisKalla: Observable<NisKalla>;

  constructor(private installningarService: MkInstallningarService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.kund && this.kund?.id) {
      this.nisKalla = this.installningarService.getNisKalla(this.kund.id);
    }
  }

  onNisKallaChange(nisKalla: NisKalla) {
    this.installningarService.updateNisKalla(this.kund.id, nisKalla).subscribe();
  }
}
