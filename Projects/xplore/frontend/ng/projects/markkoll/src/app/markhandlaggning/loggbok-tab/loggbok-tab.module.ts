import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { TranslocoModule } from "@ngneat/transloco";
import { MkLoggbokModule } from "../loggbok/loggbok.module";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkLoggbokTabComponent } from "./loggbok-tab.component";
import { MatInputModule } from "@angular/material/input";

@NgModule({
  declarations: [MkLoggbokTabComponent],
  imports: [
    MkLoggbokModule,
    XpSpinnerButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    TranslocoModule
  ],
  exports: [MkLoggbokTabComponent]
})
export class MkLoggbokTabModule {}
