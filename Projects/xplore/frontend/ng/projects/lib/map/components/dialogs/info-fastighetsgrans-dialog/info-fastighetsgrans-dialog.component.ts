import {Component} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";
import {ConfigService} from "../../../../config/config.service";
import {Observable, of} from "rxjs";
import {OAuthService} from "angular-oauth2-oidc";
import {catchError, map} from "rxjs/operators";
import {DomSanitizer} from "@angular/platform-browser";
import { SafeHtml } from "@angular/platform-browser";

@Component({
  selector: "xp-info-fastighetsgrans-dialog",
  templateUrl: "./info-fastighetsgrans-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./info-fastighetsgrans-dialog.component.scss"],
})
export class InfoFastighetsgransDialogComponent {

  infoFile: Observable<SafeHtml | string>;
  error: boolean;

  constructor(private oauthService: OAuthService, public dialogRef: MatDialogRef<InfoFastighetsgransDialogComponent>,
              private configService: ConfigService, private sanitizer: DomSanitizer) {
    if (this.configService.config.app.rightBottomPanelElements != undefined) {
      if (this.configService.config.app.rightBottomPanelElements.infoOmFastighetsgranser != undefined && this.configService.config.app.rightBottomPanelElements.infoOmFastighetsgranser != "") {
        this.infoFile = this.configService.getOwnInfoFastighetsgransFile(null).pipe(
          map(html => {
            return this.sanitizer.bypassSecurityTrustHtml(html);
          }),
          catchError(response => {
            this.error = true;
            return of(undefined);
          })
        );
      } else {
        this.getOrdinaryInfoFile();
      }
    } else {
      this.getOrdinaryInfoFile();
    }
  }

  getOrdinaryInfoFile() {
    this.infoFile = this.configService.getInfoFastighetsgransFile(null).pipe(
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
