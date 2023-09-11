import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MkSearchInputComponent } from "./search-input/search-input.component";
import { MkFilterOptionComponent } from "./filter-option/filter-option.component";
import { ConfirmExitDialogComponent } from "./confirm-exit-dialog/confirm-exit-dialog.component";
import { TranslocoModule } from "@ngneat/transloco";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { MatListModule } from '@angular/material/list';
import { MkConfirmationDialogModule } from "./confirmation-dialog/confirmation-dialog.module"
import { MkNotificationDialogModule } from "./notification-dialog/notification-dialog.module";
import { MkConfirmationWarningDialogModule } from "./confirmation-warning-dialog/confirmation-warning-dialog.module";
import { MkLegendComponent } from './legend/legend.component';
import { SecureLoadImagePipe } from "../../../../lib/util/secureLoadImage.pipe";


@NgModule({
  declarations: [
    MkSearchInputComponent,
    MkFilterOptionComponent,
    ConfirmExitDialogComponent,
    MkLegendComponent,
    SecureLoadImagePipe
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    CommonModule,
    MatIconModule,
    MatSelectModule,
    MatListModule,
    TranslocoModule,
    MkConfirmationDialogModule,
    MkNotificationDialogModule,
  ],
  exports: [
    MkSearchInputComponent,
    MkFilterOptionComponent,
    ConfirmExitDialogComponent,
    MkLegendComponent,
    SecureLoadImagePipe
  ]
})
export class MkCommonModule { }
