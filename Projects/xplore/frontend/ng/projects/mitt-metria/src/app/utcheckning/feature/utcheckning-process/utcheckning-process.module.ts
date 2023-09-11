import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UtcheckningProcessComponent } from './utcheckning-process.component';
import { MMUtcheckningProcessRoutingModule } from './utcheckning-process-routing.module';
import { MMUtcheckningProcessUiModule } from '../../ui/utcheckning-process-ui/utcheckning-process-ui.module';
import { MMUtcheckningWrapperUiModule } from '../../ui/utcheckning-wrapper/utcheckning-wrapper.module';



@NgModule({
  declarations: [UtcheckningProcessComponent],
  exports: [UtcheckningProcessComponent],
  imports: [
    CommonModule,
    MMUtcheckningProcessRoutingModule,
    MMUtcheckningProcessUiModule,
    MMUtcheckningWrapperUiModule
  ]
})
export class MMUtcheckningProcessModule { }
