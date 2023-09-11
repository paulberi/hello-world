import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface Product {
  id: string,
  image: string,
  name: string,
  description: string;
}

@Component({
  selector: 'mm-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent {
  @Input() products: Product[];
  @Input() showLoadMore: boolean;
  @Input() loadMoreButtonText: string;

  @Output() loadMore = new EventEmitter<void>();
  @Output() selectedProduct = new EventEmitter<string>();

  constructor() { }
}
