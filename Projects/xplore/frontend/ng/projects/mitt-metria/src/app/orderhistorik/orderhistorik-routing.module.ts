import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderhistorikComponent } from './orderhistorik.component';

const routes: Routes = [
  {
    path: "",
    component: OrderhistorikComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MMOrderhistorikRoutingModule { }