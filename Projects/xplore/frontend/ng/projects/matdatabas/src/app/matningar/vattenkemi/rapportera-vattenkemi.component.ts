import {Component} from "@angular/core";
import {MatobjektTyp} from "../../matobjekt/matobjekt-typ";
import {Observable} from "rxjs";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";

@Component({
  selector: "mdb-matningar-vattenkemi",
  template: `
    <div class="main-content">
      <h2>Rapportera vattenkemi</h2>
      <mdb-search-matobjekt-namn placeholder="Namn på mätobjekt" [matobjektTyp]="vattenkemiIndex"
                                 (selected)="onSelected($event)"></mdb-search-matobjekt-namn>
      <mdb-edit-vattenkemi *ngIf="matobjektId" [matobjektId]="matobjektId" (isDirty)="setDirty($event)"></mdb-edit-vattenkemi>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class RapporteraVattenkemiComponent {

  matobjektId: number;
  isDirty = false;
  vattenKemiInitialized: boolean;

  constructor() { }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.isDirty) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  setDirty(dirty: boolean) {
    this.isDirty = dirty;
  }

  onSelected(id: number) {
    this.matobjektId = id;
  }

  get vattenkemiIndex() {
    return Object.keys(MatobjektTyp).indexOf("MT_4_VATTENKEMI");
  }
}
