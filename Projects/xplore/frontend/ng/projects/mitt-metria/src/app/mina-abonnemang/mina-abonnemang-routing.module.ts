import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MinaAbonnemangComponent } from './mina-abonnemang.component';

const routes: Routes = [
  {
    path: "",
    component: MinaAbonnemangComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MMMinaAbonnemangRoutingModule { }