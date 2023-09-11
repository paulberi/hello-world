import { BreakpointObserver } from "@angular/cdk/layout";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { NavigationStart, Router } from "@angular/router";
import { Observable } from "rxjs";
import { map, shareReplay } from "rxjs/operators";
import { cartAnimation } from "../../vendure/cart/cart/cart-animation";
import { Cart } from "../../vendure/cart/cart/cart.component";

export interface MenuItemV2 {
  title: string;
  path: string;
  icon?: string;
  iconPath?: string;
}

export interface MenuIcon {
  name: string;
  icon: string;
  ariaLabel: string;
}

@Component({
  selector: 'xp-layout-v2-ui',
  templateUrl: './layout-v2.component.html',
  styleUrls: ['./layout-v2-desktop.component.scss', './layout-v2-mobile.component.scss'],
  animations: [
    cartAnimation
  ]
})

export class XpLayoutV2Component implements OnInit {
  @Input() appName: string;
  @Input() menuIcons: MenuIcon[];
  @Input() menuItems: MenuItemV2[];
  @Input() isHeaderSticky: boolean = true;
  @Input() isFooterVisible: boolean = true;

  @Output() menuIconClick = new EventEmitter<MenuIcon>();
  @Output() profileClick = new EventEmitter<void>();
  @Output() logoutClick = new EventEmitter<void>();

  //Varukorg
  @Input() isCartIconVisible: boolean;
  @Input() cartBadgeNumber: Observable<number>;
  @Input() cart: Cart;

  @Output() deleteCartItem = new EventEmitter<string>();
  @Output() toCheckout = new EventEmitter<void>();

  isFirstValue: boolean = true;
  itemsInCart: number;
  cartState: boolean = false;
  timer: ReturnType<typeof setTimeout>;;

  isTabletOrMobile$: Observable<boolean> = this.breakpointObserver.observe('(max-width: 991px)')
    .pipe(
      map(result => {
        return result.matches;
      }),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, private router: Router) { }

  ngOnInit(): void {
    this.routerChanges();
    this.itemsInCartChanges();
  }

  itemsInCartChanges() {
    this.cartBadgeNumber.subscribe(number => {
      if (this.isFirstValue) {
        this.itemsInCart = number;
        this.isFirstValue = false;
      } else if (!this.isFirstValue) {
        if (number > this.itemsInCart) {
          this.itemsInCart = number;
          this.cartState ? null : this.cartState = true;
          this.timer = setTimeout(() => {
            this.cartState = false;
          }, 3000);
        } else {
          this.itemsInCart = number;
        }
      }
    })
  }

  routerChanges() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.cartState = false;
      }
    })
  }

  toggleCart() {
    this.cartState ? this.cartState = false : this.cartState = true;
    if (!this.cartState) {
      clearTimeout(this.timer);
    }
  }
}