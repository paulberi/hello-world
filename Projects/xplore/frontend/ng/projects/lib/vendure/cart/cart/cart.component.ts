import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, Input, Output, EventEmitter, ViewChild, ElementRef, HostListener } from '@angular/core';
import { OrderLine } from '../../../ui/order-line/order-line.component';

export interface Cart {
  cartItems: OrderLine[];
  totalPrice: number | string;
  vat: number | string;
  currency: string;
}

@Component({
  selector: 'xp-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})

export class XpCartComponent {
  @Input() cart: Cart;
  @Output() closeCart = new EventEmitter<void>();
  @Output() deleteCartItem = new EventEmitter<string>();
  @Output() checkout = new EventEmitter<void>();

  @ViewChild("cartElement") cartElement: ElementRef;

  constructor(private breakpointObserver: BreakpointObserver) { }

  @HostListener("document:mousedown", ["$event"])
  clickOutsideCart(event): void {
    this.breakpointObserver.observe('(max-width: 991px)').subscribe(result => {
      if (!result.matches) {
        if (!this.cartElement.nativeElement.contains(event.target)) {
          this.closeCart.emit();
        }
      }
    })
  }
}