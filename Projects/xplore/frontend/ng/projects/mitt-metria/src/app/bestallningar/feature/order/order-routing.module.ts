import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OrderComponent } from './order.component';

const routes: Routes = [
  {
    path: "",
    component: OrderComponent,
    children: [
      { path: "", redirectTo: "lagg-bestallning", pathMatch: "full" },
      {
        path: 'lagg-bestallning',
        loadChildren: () =>
          import("../create-order/create-order.module").then(
            (m) => m.MMCreateOrderModule
          ),
      },
      {
        path: 'alla-bestallningar',
        loadChildren: () =>
          import("../all-orders/all-orders.module").then(
            (m) => m.MMAllOrdersModule
          ),
      }
    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMOrderRoutingModule {}