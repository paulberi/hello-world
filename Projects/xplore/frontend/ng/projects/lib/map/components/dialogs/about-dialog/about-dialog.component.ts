import {Component} from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";
import {Observable, of, pipe} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {ConfigService} from "../../../../config/config.service";
import {DomSanitizer} from "@angular/platform-browser";
import {OAuthService} from "angular-oauth2-oidc";
import {HttpClient} from "@angular/common/http";
import { SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'xp-about-dialog',
  templateUrl: "./about-dialog.component.html",
  styleUrls: ["./about-dialog.component.scss"],
})
export class AboutDialogComponent {
  aboutFile: Observable<SafeHtml | string>;
  error: boolean;

  constructor(private oauthService: OAuthService, public dialogRef: MatDialogRef<AboutDialogComponent>, private http: HttpClient,  private configService: ConfigService, private sanitizer: DomSanitizer) {
   this.aboutFile = this.configService.getAboutFile(null).pipe(
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
