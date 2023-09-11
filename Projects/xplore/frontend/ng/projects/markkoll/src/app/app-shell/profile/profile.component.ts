import { Component, OnInit } from "@angular/core";
import { User } from "../../../../../lib/oidc/login.service";
import { XpUserService } from "../../../../../lib/user/user.service";

@Component({
  selector: "mk-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.scss"]
})
export class ProfileComponent implements OnInit {
  user: User;
  constructor(private xpUserService: XpUserService) { 
  }

  ngOnInit() {
    this.user = this.xpUserService.getUser();
  }

}
