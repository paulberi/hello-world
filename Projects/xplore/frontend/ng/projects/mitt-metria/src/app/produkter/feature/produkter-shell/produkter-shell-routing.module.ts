import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import("../products/products.module").then(
        (m) => m.MMProductsModule
      ),
  },
  {
    path: ':id',
    loadChildren: () =>
      import("../product-page/product-page.module").then(
        (m) => m.MMProductPageModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMProdukterShellRoutingModule {}