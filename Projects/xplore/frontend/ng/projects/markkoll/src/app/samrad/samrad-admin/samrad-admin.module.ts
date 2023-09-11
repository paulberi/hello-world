import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MkAppRoutingModule } from '../../app-routing.module';
import { SamradKartaModule } from './samrad-karta/samrad-karta.module';
import { SamradAdminComponent } from './samrad-admin.component';
import { SamradAdminContainerComponent } from './samrad-admin.container';
import { TranslocoModule } from '@ngneat/transloco';
import { MatTabsModule} from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { SamradProjektModule } from './samrad-projekt/samrad-projekt.module';
import { SamradEditProjektModule } from './samrad-edit-projekt/samrad-edit-projekt.module';



@NgModule({
  imports: [
    CommonModule,
    MkAppRoutingModule,
    SamradKartaModule,
    SamradProjektModule,
    SamradEditProjektModule,
    TranslocoModule,
    MatTabsModule,
    MatListModule,
    MatIconModule
  ],
  declarations: [
    SamradAdminComponent, 
    SamradAdminContainerComponent,
  ]
})
export class SamradAdminModule { }