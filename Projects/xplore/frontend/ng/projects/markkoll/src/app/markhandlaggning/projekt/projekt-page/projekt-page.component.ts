import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { Abonnemang } from "../../../../../../../generated/kundconfig-api";
import { ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { uuid } from "../../../model/uuid";

interface RouteLink {
  link: string;
  label: string;
}
@Component({
  selector: "mk-projekt-page-ui",
  providers: [],
  templateUrl: "./projekt-page.component.html",
  styleUrls: ["./projekt-page.component.scss"]
})
export class MkProjektPageComponent {
  @Input() projektId: uuid;
  @Input() projektTyp: ProjektTyp;
  @Input() projektnamn: string;
  @Input() tabIndex;
  @Input() samradAbonnemang: Abonnemang[];

  readonly goBackRouterLink = ["/projekt/"];
  readonly links: RouteLink[] = [
    { link: "./avtal", label: this.translate.translate("mk.projektPage.estatesAndAgreements")},
    { link: "./projektinformation", label: this.translate.translate("mk.projektPage.projectInformation") },
    { link: "./projektlogg", label: this.translate.translate("mk.projektPage.projectLog") }
  ];

  constructor(private translate: TranslocoService) {}
}
