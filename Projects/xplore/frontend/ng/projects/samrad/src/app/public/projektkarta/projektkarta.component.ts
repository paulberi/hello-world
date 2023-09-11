import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Extent } from 'ol/extent';
import Layer from 'ol/layer/Layer';
import { FeatureInfoResult, SrMapComponent } from '../../utils/map/map.component';

@Component({
  selector: 'sr-projektkarta-ui',
  templateUrl: './projektkarta.component.html',
  styleUrls: ['./projektkarta.component.scss']
})
export class SrProjektkartaComponent  {
  @Input() projektId: string;
  @Input() layers: Layer[];
  @Input() backgroundLayers: Layer[];
  @Input() mapExtent: Extent;
  @Output() mapClick = new EventEmitter<FeatureInfoResult[]>();
  paddingInMap = [20, 100, 20, 20]

  @ViewChild("map", { static: false }) map: SrMapComponent;
}
