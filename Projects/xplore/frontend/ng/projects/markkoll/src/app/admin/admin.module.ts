import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkAdminComponent } from "./admin.component";
import { MkAdminContainerComponent } from "./admin.container";
import { TranslocoModule } from "@ngneat/transloco";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MkPipesModule } from "../common/pipes/pipes.module";
import { MatDialogModule } from "@angular/material/dialog";
import { MkUserFormModule } from "../user/user-form/user-form.module";
import { MatTabsModule } from "@angular/material/tabs";
import { MkProjektdokumentModule } from "../markhandlaggning/projekt/projektdokument/projektdokument.module";
import { MkUsersModule } from "./users/users.module";
import { MkNisKallaModule } from "./nis-kalla/nis-kalla.module";
import { MkLedningsagareModule } from "./ledningsagare/ledningsagare.module";
import { MkErsattningFiberModule } from "./ersattning-fiber/ersattning-fiber.module";


@NgModule({
  declarations: [
    MkAdminComponent,
    MkAdminContainerComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatTabsModule,
    MkErsattningFiberModule,
    MkLedningsagareModule,
    MkNisKallaModule,
    MkPipesModule,
    MkProjektdokumentModule,
    MkUserFormModule,
    MkUsersModule,
    TranslocoModule,
  ],
  exports: [
    MkAdminComponent,
    MkAdminContainerComponent
  ]
})
export class MkAdminModule { }
