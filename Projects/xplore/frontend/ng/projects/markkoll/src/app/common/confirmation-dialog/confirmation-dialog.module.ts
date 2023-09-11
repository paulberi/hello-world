import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { MkConfirmationDialogComponent } from "./confirmation-dialog.component";
import { MatButtonModule } from "@angular/material/button";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule } from "@angular/common";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";

@NgModule({
    declarations: [
        MkConfirmationDialogComponent
    ],
    imports: [
        CommonModule,
        TranslocoModule,
        XpSpinnerButtonModule,
        MatButtonModule,
        BrowserAnimationsModule,
    ],
    exports: [
        MkConfirmationDialogComponent
    ]
})
export class MkConfirmationDialogModule {}
