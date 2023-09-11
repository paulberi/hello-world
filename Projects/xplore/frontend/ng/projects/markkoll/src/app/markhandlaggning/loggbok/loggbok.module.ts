import { CommonModule, DatePipe } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { MkLoggbokComponent } from "./loggbok.component";
import { MatDividerModule } from "@angular/material/divider";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [
    MkLoggbokComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
  ],
  providers: [
    DatePipe,
  ],  
  exports: [
    MkLoggbokComponent
  ],
})
export class MkLoggbokModule { }
