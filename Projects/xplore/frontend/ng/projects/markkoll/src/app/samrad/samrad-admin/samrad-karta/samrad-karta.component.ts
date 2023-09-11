import { Component, Input, OnInit } from '@angular/core';
import { Extent } from 'ol/extent';
import Layer from 'ol/layer/Layer';

@Component({
  selector: 'mk-samrad-karta-ui',
  templateUrl: './samrad-karta.component.html',
  styleUrls: ['./samrad-karta.component.scss']
})
export class SamradKartaComponent {

  @Input() layers: Layer[];
  @Input() backgroundLayers: Layer[];
  @Input() mapExtent: Extent;
  paddingInMap = [20, 100, 20, 20]
 

}
