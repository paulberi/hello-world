import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {ConfigService} from "../../../../config/config.service";
import {Observable, of} from "rxjs";
import {OAuthService} from "angular-oauth2-oidc";
import {catchError, map} from "rxjs/operators";
import {DomSanitizer} from "@angular/platform-browser";
import { SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'xp-help-dialog',
  templateUrl: "./help-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./help-dialog.component.scss"],
})
export class HelpDialogComponent {

  helpFile: Observable<SafeHtml | string>;
  error: boolean;

  constructor(private oauthService: OAuthService, public dialogRef: MatDialogRef<HelpDialogComponent>,
              private configService: ConfigService, private sanitizer: DomSanitizer) {

    if (this.configService.config.app.rightBottomPanelElements != undefined) {
      if (this.configService.config.app.rightBottomPanelElements.egenHjalp_Path != undefined && this.configService.config.app.rightBottomPanelElements.egenHjalp_Path != "") {
        this.helpFile = this.configService.getOwnHelpFile(null).pipe(
          map(html => {
            return this.sanitizer.bypassSecurityTrustHtml(html);
          }),
          catchError(response => {
            this.error = true;
            return of(undefined);
          })
        );
      } else {
        this.getOrdinaryHelpFile();
      }
    } else {
      this.getOrdinaryHelpFile();
    }
  }

  getOrdinaryHelpFile() {
    this.helpFile = this.configService.getHelpFile(null).pipe(
      map(html => {
        return this.sanitizer.bypassSecurityTrustHtml(html);
      }),
      catchError(response => {
        this.error = true;
        return of(undefined);
      })
    );
  }

  close(): void {
    this.dialogRef.close();
  }
}
