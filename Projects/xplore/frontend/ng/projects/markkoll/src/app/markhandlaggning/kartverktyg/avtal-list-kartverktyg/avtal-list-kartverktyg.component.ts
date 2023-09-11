import { Component, Input, Output, ViewChild } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
import { MkHanteraIntrangDelegate } from "../delegates/hantera-intrang-delegate.impl";
import { MkHanteraFastigheterDelegate } from "../meny/hantera-fastigheter/hantera-fastigheter.component";
import { MkIntrangVerktyg } from "../meny/hantera-intrang/verktyg/intrang-verktyg";
import { MkVerktygsladaComponent, VerktygsladaButton } from "../meny/hantera-intrang/verktygslada/verktygslada.component";
import { MkAvtalListKartverktygPresenter } from "./avtal-list-kartverktyg.presenter";


@Component({
  selector: "mk-avtal-list-kartverktyg-ui",
  templateUrl: "./avtal-list-kartverktyg.component.html",
  styleUrls: ["./avtal-list-kartverktyg.component.scss"],
  providers: [
    MkAvtalListKartverktygPresenter
  ]
})
export class MkAvtalListKartverktygComponent  {

  @Input() hanteraFastigheterDelegate: MkHanteraFastigheterDelegate;

  @Input() hanteraIntrangDelegate: MkHanteraIntrangDelegate;

  @Output() toolStateChange = this.presenter.toolStateChange;

  @Input() verktygsladaButtons: VerktygsladaButton[];

  @ViewChild(MkVerktygsladaComponent, { static: true })
  intrangVerktygslada: MkVerktygsladaComponent;

  constructor(private presenter: MkAvtalListKartverktygPresenter) {}

  onIsPanelOpenChange(isOpen: boolean) {
    this.presenter.isPanelOpen = isOpen;
  }

  onActiveTabChange(activeTab: string) {
    this.presenter.activeTab = activeTab;
  }

  get activeTab() { return this.presenter.activeTab; }
}
