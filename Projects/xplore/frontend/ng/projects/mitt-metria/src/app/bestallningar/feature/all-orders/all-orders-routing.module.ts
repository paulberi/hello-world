import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AllOrdersComponent } from './all-orders.component';

const routes: Routes = [
  {
    path: "",
    component: AllOrdersComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMAllOrdersRoutingModule {}