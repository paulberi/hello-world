import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { MkAgareImportDialogComponent } from "./agare-import-dialog.component";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";
import { MatButtonModule } from "@angular/material/button";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule } from "@angular/common";

@NgModule({
    declarations: [MkAgareImportDialogComponent],
    imports: [
        CommonModule,
        TranslocoModule,
        MatProgressSpinnerModule,
        XpSpinnerButtonModule,
        MatButtonModule,
        BrowserAnimationsModule,
    ],
    exports: [MkAgareImportDialogComponent]
})
export class MkAgareImportDialogModule {}
