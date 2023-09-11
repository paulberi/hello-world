import { Component, OnInit } from '@angular/core';
import { ConfirmationDialogModel } from '../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component';
import { DialogService } from '../../../../lib/dialogs/dialog.service';
import { LoginService, User } from '../../../../lib/oidc/login.service';
import { XpLayoutUserInfo } from '../../../../lib/ui/layout/layout.component';
import { XpUserService } from '../../../../lib/user/user.service';
import { Customer } from '../../../../../generated/graphql/admin/admin-api-types';
import { roles } from '../shared/utils/roles';
import { MenuItemV2 } from '../../../../lib/ui/layout-v2/layout-v2.component';
import { AuthService } from '../shared/data-access/auth/auth.service';
import { CartService } from '../../../../lib/vendure/cart/cart.service';
import { Cart } from '../../../../lib/vendure/cart/cart/cart.component';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'mm-app-shell',
  templateUrl: './app-shell.component.html',
  styleUrls: ['./app-shell.component.scss']
})
export class AppShellComponent implements OnInit {
  user: XpLayoutUserInfo;
  activeCustomer: Customer;
  menuItems: MenuItemV2[] = [];
  cartBadgeNumber: Subject<number> = new ReplaySubject();
  cart: Cart;
  cartState$: Observable<boolean>;

  constructor(
    private dialogService: DialogService,
    private xpUserService: XpUserService,
    private loginService: LoginService,
    private authService: AuthService,
    private cartService: CartService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.getUser();
    this.getCart();
  }

  setupMenu() {
    if (this.authService.hasAccess(roles.METRIA_SALJARE)) {
      this.menuItems = [
        {
          title: "Beställningar",
          path: "/bestallningar",
          icon: "lightbulb"
        }
      ];
    } else {
      this.menuItems = [
        {
          title: "Produkter",
          path: "/produkter",
          iconPath: "../../assets/icons/icon_box_white.svg",
        },
        {
          title: "Mina abonnemang",
          path: "/mina-abonnemang",
          icon: "subscriptions",
        },
        {
          title: "Orderhistorik",
          path: "/orderhistorik",
          iconPath: "../../assets/icons/icon_package_white.svg",
        }
      ];
    }
  }

  getUser() {
    this.xpUserService.getUser$().subscribe((user: User) => {
      if (user) {
        this.user = {
          id: "ID",
          email: user?.claims?.email,
          fornamn: user?.claims?.given_name,
          efternamn: user?.claims?.family_name,
          kund: ""
        }
        this.setupMenu();
        return user;
      }
    })
  }

  /**
   * Öppna dialog för att logga ut.
   */
  openLogoutDialog() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Logga ut",
        "Är du säker på att du vill logga ut?",
        "Avbryt",
        "Logga ut"),
      dialogResult => {
        if (dialogResult) {
          this.loginService.logout();
          this.authService.logoutVendure();
        }
      });
  }


  /**
   * Hämta varukorgen och antal produkter.
   */
  getCart() {
    this.cartService.getActiveOrder().subscribe(cart => {
      this.cart = cart;
      this.cartBadgeNumber.next(cart?.cartItems?.length);
    });
  }

  /**
   * Ta bort en produkt i varukorgen.
   */
  deleteCartItem(id: string) {
    this.cartService.deleteItemFromActiveOrder(id);
  }

  /**
   * Navigering till utcheckning.
   */
  toCheckout() {
    this.router.navigateByUrl("/utcheckning");
  }
}
