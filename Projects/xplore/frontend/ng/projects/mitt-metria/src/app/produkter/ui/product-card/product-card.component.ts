import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'mm-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent {
  @Input() imageUrl: string;
  @Input() header: string;
  @Input() description: string;
  @Input() tags: [{
    name: string;
    icon: string;
  }];
  @Output() selectedItem = new EventEmitter<string>();

  setDefaultImage() {
    this.imageUrl = "../../assets/default_image_small.jpeg";
  }

}


