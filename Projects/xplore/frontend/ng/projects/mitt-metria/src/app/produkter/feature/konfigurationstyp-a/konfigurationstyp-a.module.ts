import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KonfigurationstypAComponent } from './konfigurationstyp-a.component';
import { MMKonfigurationstypAUiModule } from '../../ui/konfigurationstyp-a-ui/konfigurationstyp-a-ui.module';
import { MMDinKartproduktModule } from '../../ui/din-kartprodukt/din-kartprodukt.module';
import { TranslocoModule } from '@ngneat/transloco';
import { XpMessageModule } from '../../../../../../lib/ui/feedback/message/message.module';
import { MMStepperModule } from '../../ui/stepper/stepper.module';

@NgModule({
  declarations: [KonfigurationstypAComponent],
  imports: [
    CommonModule,
    MMKonfigurationstypAUiModule,
    MMDinKartproduktModule,
    TranslocoModule,
    XpMessageModule,
    MMStepperModule
  ],
  exports: [KonfigurationstypAComponent]
})
export class MMKonfigurationstypAModule { }
