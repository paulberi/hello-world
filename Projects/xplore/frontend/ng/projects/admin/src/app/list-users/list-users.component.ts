import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { MarkkollUser, UserInfo } from "../../../../../generated/kundconfig-api";

@Component({
  selector: "adm-list-users-ui",
  templateUrl: "./list-users.component.html",
  styleUrls: ["./list-users.component.scss"]
})
export class AdmListUsersComponent implements OnInit {
  @Input() users: MarkkollUser[];
  @Input() spinnerActive = false;
  @Input() isAddKundAdminVisible = false;

  @Output() create = new EventEmitter<UserInfo>();
  @Output() delete = new EventEmitter<UserInfo>();

  constructor() { }

  ngOnInit(): void {
  }

  showAddKundAdmin() {
    this.isAddKundAdminVisible = true;
  }

  submit(event: UserInfo) {
    this.create.emit(event);
  }

  hideAddKundAdmin() {
    this.isAddKundAdminVisible = false;
  }
}
