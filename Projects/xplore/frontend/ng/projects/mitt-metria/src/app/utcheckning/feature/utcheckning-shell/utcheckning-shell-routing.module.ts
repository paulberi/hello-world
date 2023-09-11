import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import("../utcheckning-process/utcheckning-process.module").then(
        (m) => m.MMUtcheckningProcessModule
      ),
  },
  {
    path: 'bekraftelse/:code',
    loadChildren: () =>
      import("../utcheckning-bekraftelse/utcheckning-bekraftelse.module").then(
        (m) => m.MMUtcheckningBekraftelseModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MMUtcheckningShellRoutingModule { }
