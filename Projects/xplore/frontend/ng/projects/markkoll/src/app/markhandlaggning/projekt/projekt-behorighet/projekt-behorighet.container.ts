import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Observable } from "rxjs";
import { MarkkollUser, RoleType } from "../../../../../../../generated/markkoll-api";
import { MkUserService } from "../../../services/user.service";

export interface UserWithRoll {
  user: MarkkollUser;
  roll?: RoleType;
}
@Component({
  selector: "mk-projekt-behorighet",
  templateUrl: "./projekt-behorighet.container.html"
})
export class MkProjektBehorighetContainerComponent implements OnInit {
  users: Observable<MarkkollUser[]>;
  loggedInUser: MarkkollUser;

  @Input() createProjekt = false;

  @Input() readonly = false;

  @Input() originalProjektUsers$: Observable<UserWithRoll[]>;

  @Output() formChange = new EventEmitter<UserWithRoll[]>();

  @Output() valid = new EventEmitter<boolean>();

  constructor(private mkUserService: MkUserService) { }

  ngOnInit(): void {
    this.mkUserService.getMarkkollUser$().subscribe(user => {
      this.loggedInUser = user;
      this.users = this.mkUserService.getKundUsers(user.kundId);
    });
  }

}
