import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { Observable } from "rxjs";
import { Kund, MarkkollUser, UserInfo } from "../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { MkUserService } from "../../services/user.service";

@Component({
  selector: "mk-users-container",
  templateUrl: "./users.container.html",
  providers: []
})
export class MkUsersContainer implements OnChanges {
  @Input() kund: Kund;

  @Input() loggedInUser: MarkkollUser;

  usersForKund: Observable<MarkkollUser[]>;

  constructor(private userService: MkUserService,
              private notificationService: XpNotificationService,
              private translate: TranslocoService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.loggedInUser && changes.loggedInUser.currentValue) {
      this.getUsers();
    }
  }

  userEdit(user: MarkkollUser) {
    this.userService.editUser(user.id, {...user} as UserInfo).subscribe(_ => {
      this.notificationService.success(this.translate.translate("mk.admin.userEditSuccess"));
    },
    error => {
      this.notificationService.error(this.translate.translate("mk.admin.userEditError"));
    }
  );
  }

  getUsers(): void {
    this.usersForKund = this.userService.getKundUsers(this.loggedInUser.kundId);
  }

  userDelete(user: MarkkollUser) {
    if (user.email !== this.loggedInUser.email) {
      this.userService.deleteUser(user).subscribe(_ => {
        this.getUsers();
        this.notificationService.success(this.translate.translate("mk.admin.userDeleteSuccess"));
      },
      error => {
        this.notificationService.error(this.translate.translate("mk.admin.userDeleteError"));
      }
    );
    }
  }

  userCreate(userInfo: UserInfo) {
    this.userService.createUser(this.loggedInUser.kundId, userInfo)
      .subscribe(
        () => {
          this.getUsers();
          this.notificationService.success(this.translate.translate("mk.admin.userCreateSuccess"));
        },
        error => {
          this.notificationService.error(this.translate.translate("mk.admin.userCreateError"));
        }
      );
  }
}
