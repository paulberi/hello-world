import { UtcheckningProcessComponent } from './utcheckning-process.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UtcheckningGuard } from '../../utils/utcheckning.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [UtcheckningGuard],
    component: UtcheckningProcessComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMUtcheckningProcessRoutingModule {}
