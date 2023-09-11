import { MMUtcheckningBekraftelseRoutingModule } from './utcheckning-bekraftelse-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UtcheckningBekraftelseComponent } from './utcheckning-bekraftelse.component';
import { MMUtcheckningBekraftelseUiModule } from '../../ui/utcheckning-bekraftelse-ui/utcheckning-bekraftelse-ui.module';
import { MMUtcheckningWrapperUiModule } from '../../ui/utcheckning-wrapper/utcheckning-wrapper.module';



@NgModule({
  declarations: [UtcheckningBekraftelseComponent],
  exports: [UtcheckningBekraftelseComponent],
  imports: [
    CommonModule,
    MMUtcheckningBekraftelseRoutingModule,
    MMUtcheckningBekraftelseUiModule,
    MMUtcheckningWrapperUiModule
  ]
})
export class MMUtcheckningBekraftelseModule { }
