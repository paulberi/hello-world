import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UtcheckningBekraftelseComponent } from './utcheckning-bekraftelse.component';

const routes: Routes = [
  {
    path: '',
    component: UtcheckningBekraftelseComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMUtcheckningBekraftelseRoutingModule {}
