import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { NgModule } from "@angular/core";
import { AppRoutingModule } from "./app-routing.module";

import { AppComponent } from "./app.component";
import { ProjectPageComponent } from "./components/project-page/project-page.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule} from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatDialogModule } from "@angular/material/dialog";
import { MatButtonModule } from "@angular/material/button";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatIconModule } from "@angular/material/icon";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatTooltipModule } from "@angular/material/tooltip";
import { SidfotComponent } from "./components/sidfot/sidfot.component";
import { SidhuvudComponent } from "./components/sidhuvud/sidhuvud.component";
import { HttpClientModule } from "@angular/common/http";
import { OAuthModule } from "angular-oauth2-oidc";
import { ProjektlistaComponent } from "./components/projektlista/projektlista.component";
import { ConfirmationDialogComponent } from "./components/confirmation-dialog/confirmation-dialog.component";
import { DndDirective } from "./dnd.directive";
import { NotAuthorizedComponent } from "./components/not-authorized/not-authorized.component";

@NgModule({
    declarations: [
        AppComponent,
        ProjectPageComponent,
        SidfotComponent,
        SidhuvudComponent,
        ProjektlistaComponent,
        ConfirmationDialogComponent,
        DndDirective,
        NotAuthorizedComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatDialogModule,
        MatIconModule,
        MatPaginatorModule,
        MatSnackBarModule,
        MatTableModule,
        MatTooltipModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatProgressSpinnerModule,
        HttpClientModule,
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: ["/api"],
                sendAccessToken: true
            }
        }),
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
