import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule , ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'; 

import {
  MatToolbarModule,
  MatIconModule,
  MatCardModule,
  MatPaginatorModule,
  MatSortModule,
  MatTableModule,
  MatInputModule
  
} from '@angular/material';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './header/header.component';
import { BodyareaComponent } from './bodyarea/bodyarea.component';

@NgModule({
  exports: [
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatInputModule
  ],
  declarations: []
})
export class DemoMaterialModule {}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    BodyareaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    DemoMaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
