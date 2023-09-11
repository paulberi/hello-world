import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { XpUserService } from "../../../../lib/user/user.service";
import { AuthService } from "../services/auth.service";

@Component({
  selector: "sr-app-shell",
  templateUrl: "./app-shell.component.html",
  styleUrls: ["./app-shell.component.scss"],
})
export class AppShellComponent {
  menuItems = [
    {
      title: "Mina Sidor",
      selected: false,
      path: "/mina-sidor",
      icon: "format_list_bulleted",
    },
  ];

  loggedInUserInfo = {
    id: "test@test",
    fornamn: "Test",
    efternamn: "Testsson",
    email: "test@test",
    kund: "Kund",
  };
  constructor(private router: Router, private authService: AuthService) {}

  logoutClick() {
    this.authService.logout();
  }
  helpClick(value: any) {
    console.log(value);
  }
  changePage(event: any) {
    this.router.navigate([event.path]);
  }
}
