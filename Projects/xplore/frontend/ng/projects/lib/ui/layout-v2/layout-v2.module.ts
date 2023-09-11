import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { RouterModule } from "@angular/router";
import { MatDividerModule } from "@angular/material/divider";
import { XpLayoutV2Component } from "./layout-v2.component";
import { MatMenuModule } from "@angular/material/menu";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatListModule } from "@angular/material/list";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatBadgeModule } from '@angular/material/badge';
import { XpCartModule } from "../../vendure/cart/cart/cart.module";

@NgModule({
  declarations: [
    XpLayoutV2Component
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatBadgeModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatMenuModule,
    RouterModule,
    TranslocoModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    XpCartModule
  ],
  exports: [
    XpLayoutV2Component,
  ]
})
export class XpLayoutV2Module { }
