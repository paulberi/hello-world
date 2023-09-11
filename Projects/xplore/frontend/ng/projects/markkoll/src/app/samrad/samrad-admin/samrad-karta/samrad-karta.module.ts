import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SamradKartaComponent } from './samrad-karta.component';
import { MkMapModule } from '../../../common/map/map.module';
import { SamradKartaContainerComponent } from './samrad-karta.container';



@NgModule({
  declarations: [SamradKartaComponent, SamradKartaContainerComponent],
  imports: [
    CommonModule,
    MkMapModule,
    
  ],
  exports: [SamradKartaComponent, SamradKartaContainerComponent]
  
})
export class SamradKartaModule { }
