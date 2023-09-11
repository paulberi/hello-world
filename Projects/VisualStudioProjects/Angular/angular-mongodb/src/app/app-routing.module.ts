import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BodyareaComponent } from './bodyarea/bodyarea.component';


const routes: Routes = [
  {
    path: '',
    component: BodyareaComponent
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
