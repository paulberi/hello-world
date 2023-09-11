import {NgModule} from "@angular/core";
import {MkAvtalActionsComponent} from "./avtal-actions.component";
import {CommonModule} from "@angular/common";
import {TranslocoModule} from "@ngneat/transloco";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {XpPrefixedSelectionModule} from "../../../../../../lib/ui/prefixed-selection/prefixed-selection.module";
import {MkAvtalGenerateButtonModule} from "../avtal-generate-button/avtal-generate-button.module";
import {MatButtonModule} from "@angular/material/button";
import { MkAvtalActionsContainer } from "./avtal-actions.container";

@NgModule({
  declarations: [
    MkAvtalActionsComponent,
    MkAvtalActionsContainer
  ],
    imports: [
        CommonModule,
        TranslocoModule,
        FormsModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatOptionModule,
        MatSelectModule,
        XpPrefixedSelectionModule,
        MkAvtalGenerateButtonModule,
        MatButtonModule
    ],
  exports: [
    MkAvtalActionsComponent,
    MkAvtalActionsContainer
  ]
})
export class MkAvtalActionsModule {}
