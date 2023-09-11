import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { Extent } from "ol/extent";
import Layer from "ol/layer/Layer";
import { MkMapComponent, FeatureInfoResult } from "../../../common/map/map.component";
import { uuid } from "../../../model/uuid";

@Component({
  selector: "mk-projektkarta-ui",
  templateUrl: "./projektkarta.component.html",
  styleUrls: ["./projektkarta.component.scss"],
  providers: []
})
export class MkProjektkartaComponent {
  @Input() projektId: uuid;
  @Input() layers: Layer[];
  @Input() backgroundLayers: Layer[];
  @Input() mapExtent: Extent;
  @Output() mapClick = new EventEmitter<FeatureInfoResult[]>();
  paddingInMap = [20, 100, 20, 20]

  @ViewChild("map", { static: false }) map: MkMapComponent;
}
