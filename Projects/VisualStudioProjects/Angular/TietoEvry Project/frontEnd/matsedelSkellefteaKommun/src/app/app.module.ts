import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatPaginatorModule} from '@angular/material/paginator';
import { MatDividerModule} from '@angular/material/divider';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WeeksComponent } from './weeks/weeks.component';
import { DaysComponent } from './days/days.component';
import { MondayComponent } from './days/monday/monday.component';
import { TuesdayComponent } from './days/tuesday/tuesday.component';
import { WednesdayComponent } from './days/wednesday/wednesday.component';
import { ThursdayComponent } from './days/thursday/thursday.component';
import { FridayComponent } from './days/friday/friday.component';
import { ImagesComponent } from './images/images.component';
import { DatePipe } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    WeeksComponent,
    DaysComponent,
    MondayComponent,
    TuesdayComponent,
    WednesdayComponent,
    ThursdayComponent,
    FridayComponent,
    ImagesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatPaginatorModule,
    MatDividerModule
  ],
  providers: [
    DatePipe
  ],
  bootstrap: [AppComponent],

})
export class AppModule { }
