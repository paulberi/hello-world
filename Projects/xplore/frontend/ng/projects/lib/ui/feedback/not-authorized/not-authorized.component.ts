import { Component } from "@angular/core";
import { LoginService } from "../../../oidc/login.service";
import { Router } from "@angular/router";

@Component({
  selector: "xp-not-authorized",
  templateUrl: "./not-authorized.component.html",
  styleUrls: ["./not-authorized.component.scss"]
})
export class XpNotAuthorizedComponent {

  constructor(private router: Router, 
              private loginService: LoginService) { }

  logout() {
    this.loginService.logout();
    this.router.navigate([""]);
  }
}
