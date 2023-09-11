import { CommonModule } from "@angular/common";
import { ConfirmationDialogComponent } from "../../dialogs/confirmation-dialog/confirmation-dialog.component";
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { InformationDialogComponent } from "../../dialogs/information-dialog/information-dialog.component";
import { MatButtonModule} from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { TranslocoModule } from "@ngneat/transloco";
import { XpLayoutComponent } from "./layout.component";
import { XpFeedbackModule } from "../feedback/feedback.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
    declarations: [
        ConfirmationDialogComponent,
        InformationDialogComponent,
        XpLayoutComponent,
    ],
    imports: [
        BrowserAnimationsModule,
        CommonModule,
        FormsModule,
        HttpClientModule,
        MatButtonModule,
        MatDialogModule,
        MatDividerModule,
        MatIconModule,
        MatMenuModule,
        MatSlideToggleModule,
        RouterModule,
        TranslocoModule,
        XpFeedbackModule,
    ],
    exports: [
        XpLayoutComponent,
    ]
})
export class XpLayoutModule {}
