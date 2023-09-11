import { Component} from "@angular/core";
import { LoginService } from "../../../../../lib/oidc/login.service";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmationDialogComponent } from "../confirmation-dialog/confirmation-dialog.component";


@Component({
  selector: "mh-sidhuvud",
  templateUrl: "./sidhuvud.component.html",
  styleUrls: ["./sidhuvud.component.scss"]
})
export class SidhuvudComponent {

  constructor(private loginService: LoginService, public dialog: MatDialog) { 
  }

  openConfirmationDialog() {
    this.dialog.open(ConfirmationDialogComponent, {data: {
      title: "Logga ut",
      body: "Är du säker på att du vill logga ut?",
      button: "Logga ut",
      color: "accent"
    },
    autoFocus: false,
  }).afterClosed().subscribe((result) => { 
      if (result) {
        this.logout();
      }
    });
  }

  logout() {
    this.loginService.logout();
  }
}
