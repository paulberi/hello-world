import { Component, Input, OnChanges, SimpleChanges, ViewChild } from "@angular/core";
import { Ledningsagare } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { MkLedningsagareService } from "../../services/ledningsagare.service";
import { MkLedningsagareComponent } from "./ledningsagare.component";

@Component({
  selector: "mk-ledningsagare",
  templateUrl: "./ledningsagare.container.html",
  providers: []
})
export class MkLedningsagareContainer implements OnChanges {
  @Input() kundId: string;

  @ViewChild(MkLedningsagareComponent, { static: true }) component: MkLedningsagareComponent;

  private _ledningsagare: Ledningsagare[] = [];

  constructor(private ledningsagareService: MkLedningsagareService) {}

  get ledningsagare(): Ledningsagare[] {
    return this._ledningsagare;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.kundId) {
      this.ledningsagareService.getLedningsagare(changes.kundId.currentValue)
          .subscribe(ledningsagare => this._ledningsagare = ledningsagare);
    }
  }

  onLedningsagareAdd(namn: string) {
    this.ledningsagareService.addLedningsagare(namn, this.kundId)
        .subscribe(newLedningsagare => {
          this._ledningsagare = [...this.ledningsagare, newLedningsagare];
          this.component.reset();
        });
  }

  onLedningsagareDelete(ledningsagareId: uuid) {
    this.ledningsagareService.deleteLedningsagare(ledningsagareId, this.kundId)
        .subscribe(() => this._ledningsagare = this.ledningsagare.filter(ag => ag.id !== ledningsagareId));
  }
}
