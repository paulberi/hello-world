import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Kund, MarkkollUser, UserInfo } from "../../../../../../generated/markkoll-api";

@Component({
  selector: "mk-users",
  templateUrl: "./users.component.html",
  styleUrls: ["./users.component.scss"],
  providers: []
})
export class MkUsersComponent {
  @Input() kund: Kund;

  @Input() loggedInUser: MarkkollUser;

  @Input() usersForKund: MarkkollUser[];

  @Output() userCreate = new EventEmitter<UserInfo>();

  @Output() userEdit = new EventEmitter<MarkkollUser>();

  @Output() userDelete = new EventEmitter<UserInfo>();
}
