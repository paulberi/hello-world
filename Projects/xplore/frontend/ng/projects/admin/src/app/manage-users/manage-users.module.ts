import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AdmManageUsersComponent } from "./manage-users.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { AdmStartComponent } from "../start/start.component";
import { MatSelectModule } from "@angular/material/select";
import { MatOptionModule } from "@angular/material/core";
import { TranslocoModule } from "@ngneat/transloco";
import {MatPaginatorModule} from "@angular/material/paginator";



@NgModule({
  declarations: [AdmManageUsersComponent, AdmStartComponent],
    imports: [
        CommonModule,
        MatButtonModule,
        MatCardModule,
        MatIconModule,
        MatOptionModule,
        MatSelectModule,
        MatTableModule,
        TranslocoModule,
        MatPaginatorModule
    ],
  exports: [
    AdmManageUsersComponent,
    AdmStartComponent
  ]
})
export class AdmManageUsersModule { }
