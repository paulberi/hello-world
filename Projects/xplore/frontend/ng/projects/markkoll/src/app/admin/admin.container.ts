import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { switchMap } from "rxjs/operators";
import { Kund } from "../../../../../generated/kundconfig-api";
import { MarkkollUser, NisKalla } from "../../../../../generated/markkoll-api";
import { MkInstallningarService } from "../services/installningar.service";
import { MkUserService } from "../services/user.service";

@Component({
  selector: "mk-admin",
  templateUrl: "./admin.container.html",
})
export class MkAdminContainerComponent implements OnInit {
  loggedInUser: MarkkollUser;
  activeKund: Kund;

  constructor(private userService: MkUserService) { }

  ngOnInit(): void {
    this.userService.getMarkkollUser$()
      .pipe(
        switchMap(user => {
          this.loggedInUser = user;
          return this.userService.getKund(user.kundId);
        })
      ).subscribe(kund => this.activeKund = kund);
  }
}
