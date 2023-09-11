import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AdmKundvyComponent } from "./kundvy.component";
import { TranslocoModule } from "@ngneat/transloco";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatDividerModule } from "@angular/material/divider";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { MatIconModule } from "@angular/material/icon";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { RouterModule } from "@angular/router";
import { MatDialogModule } from "@angular/material/dialog";
import { MatPaginatorModule } from '@angular/material/paginator';
import { AdmKundvyContainerComponent } from "./kundvy.container";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";
import { AdmKunduppgifterModule } from "../kunduppgifter/kunduppgifter.module";
import { AdmSysteminloggningarModule } from "../systeminloggningar/systeminloggningar.module";
import { MatTabsModule } from "@angular/material/tabs";
import { AdmUsersModule } from "../list-users/list-users.module";
import { AdmAbonnemangModule } from "../abonnemang/abonnemang.module";
import { MatMenuModule } from "@angular/material/menu";

@NgModule({
  declarations: [AdmKundvyComponent, AdmKundvyContainerComponent],
  imports: [
    AdmUsersModule,
    AdmKunduppgifterModule,
    AdmSysteminloggningarModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatPaginatorModule,
    MatTabsModule,
    ReactiveFormsModule,
    RouterModule,
    TranslocoModule,
    AdmSysteminloggningarModule,
    MatTabsModule,
    AdmAbonnemangModule
  ],
  exports: [AdmKundvyComponent]
})
export class AdmKundvyModule { }
