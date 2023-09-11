import { Component, Input, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { Observable } from "rxjs";
import { Kund, MarkkollUser, UserInfo } from "../../../../../generated/kundconfig-api";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { UserService } from "../services/user.service";

@Component({
  selector: "adm-list-users",
  templateUrl: "./list-users.container.html"
})
export class AdmListUsersContainerComponent implements OnInit {
  @Input() kund: Kund;
  users: Observable<MarkkollUser[]> = null;
  spinnerActive = false;
  isAddKundAdminVisible = false;

  @ViewChild("deleteDialog") deleteDialog: TemplateRef<any>;

  constructor(private userService: UserService,
              private translate: TranslocoService,
              private matDialog: MatDialog,
              private notificationService: XpNotificationService,
    ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.users = this.userService.getUsersForKund(this.kund.id).pipe();
  }
  createUser(user: UserInfo) {
    this.spinnerActive = true;
    this.userService.createKundAdmin(this.kund.id, user).subscribe(() => {
      this.isAddKundAdminVisible = false;
      this.spinnerActive = false;
      this.getUsers();
      this.notificationService.success(this.translate.translate("adm.notifications.userAdded"));
    },
    error => {
      this.isAddKundAdminVisible = false;
      this.spinnerActive = false;
    });
  }

  deleteUser(user: MarkkollUser) {
    this.matDialog.open(this.deleteDialog, {autoFocus: false}).afterClosed().subscribe(res => {
      if (res) {
        this.userService.deleteUser(user.id).subscribe(() => {
          this.getUsers();
        });
      }
    });
  }

}
