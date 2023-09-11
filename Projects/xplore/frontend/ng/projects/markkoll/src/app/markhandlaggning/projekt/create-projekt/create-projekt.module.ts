import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MkCreateProjektComponent } from "./create-projekt.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { XpMessageModule } from "../../../../../../lib/ui/feedback/message/message.module";
import { MatStepperModule } from "@angular/material/stepper";
import { ReactiveFormsModule } from "@angular/forms";
import { MkProjektInfoModule } from "../projekt-info/projekt-info.module";
import { MkFiberInfoModule } from "../fiber-info/fiber-info.module";
import { MkCreateProjektContainerComponent } from "./create-projekt.container";
import { XpSpinnerButtonModule } from "../../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkUploadFileModule } from "../upload-file/upload-file.module";
import { MkElnatInfoModule } from "../elnat-info/elnat-info.module";
import { RouterModule } from "@angular/router";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MkProjektBehorighetModule } from "../projekt-behorighet/projekt-behorighet.module";

@NgModule({
  declarations: [
    MkCreateProjektComponent,
    MkCreateProjektContainerComponent
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatStepperModule,    
    MkElnatInfoModule,
    MkFiberInfoModule,
    MkProjektBehorighetModule,
    MkProjektInfoModule,
    MkUploadFileModule,
    ReactiveFormsModule,
    RouterModule,            
    TranslocoModule,
    XpMessageModule,
    XpSpinnerButtonModule,

  ],
  exports: [
    MkCreateProjektComponent,
    MkCreateProjektContainerComponent
  ]
})
export class MkCreateProjektModule {}
