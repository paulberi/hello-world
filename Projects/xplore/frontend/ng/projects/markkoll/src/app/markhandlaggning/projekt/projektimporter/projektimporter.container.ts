import { Component, Input } from "@angular/core";
import { NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { ImportTyp } from "../../../model/importTyp";
import { uuid } from "../../../model/uuid";

@Component({
  selector: "mk-projektimporter",
  templateUrl: "./projektimporter.container.html"
})
export class MkProjektimporterContainer {
  @Input() projektId: uuid;

  @Input() projektTyp: ProjektTyp;

  importTyp: ImportTyp = ImportTyp.VERSION;

}
