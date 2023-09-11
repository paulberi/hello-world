import { Component, OnInit } from "@angular/core";
import {LoginService} from "../../../../lib/oidc/login.service";

@Component({
  selector: "mdb-not-authorized",
  template: `
      <div class="header">
        <h2>Metria Miljökoll</h2>
      </div>
      <div class="main-content">
        <h4>Behörighet saknas.</h4>
        <button mat-raised-button color="primary" (click)="logout()">Logga ut</button>
      </div>
  `,
  styles: [`
    .header {
      grid-area: header;
      display: grid;
      grid-template-columns: auto 1fr auto;
      align-items: center;
      padding: 1rem;
      border-top: 0.3rem solid var(--separator-color);
    }
    .main-content {
      padding-left: 1.5rem;
    }
  `]
})
export class NotAuthorizedComponent implements OnInit {

  constructor(private loginService: LoginService) { }

  ngOnInit() {
  }

  logout() {
    this.loginService.logout();
  }
}
