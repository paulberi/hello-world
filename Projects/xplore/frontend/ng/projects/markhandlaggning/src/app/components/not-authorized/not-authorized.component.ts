import { Component, OnInit } from "@angular/core";
import { LoginService } from "../../../../../lib/oidc/login.service";

@Component({
  selector: "mh-not-authorized",
  templateUrl: "./not-authorized.component.html",
  styleUrls: ["./not-authorized.component.scss"]
})
export class NotAuthorizedComponent implements OnInit {

  constructor(private loginService: LoginService) { }

  ngOnInit() {
  }
  logout() {
    this.loginService.logout();
  }

}
