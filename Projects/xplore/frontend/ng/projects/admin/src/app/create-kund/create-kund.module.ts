import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TranslocoModule } from "@ngneat/transloco";
import { MatDividerModule } from "@angular/material/divider";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import { AdmCreateKundComponent } from "./create-kund.component";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { RouterModule } from "@angular/router";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";
import { AdmCreateKundContainerComponent } from "./create-kund.container";
import { MatIconModule } from "@angular/material/icon";


@NgModule({
  declarations: [AdmCreateKundComponent, AdmCreateKundContainerComponent],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    ReactiveFormsModule,
    RouterModule,
    TranslocoModule
  ],
  exports: [AdmCreateKundComponent]
})
export class AdmCreateKundModule { }
