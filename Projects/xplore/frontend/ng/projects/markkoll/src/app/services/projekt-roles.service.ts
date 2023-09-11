import { Injectable } from "@angular/core";
import { forkJoin, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { MarkkollUser, RoleType } from "../../../../../generated/markkoll-api";
import { UserRoleEntry, UserOption } from "../markhandlaggning/projekt/user-roles/user-roles.component";
import { uuid } from "../model/uuid";
import { MkUserService } from "./user.service";

export class ProjektRoles {
  entries: UserRoleEntry[];
  userOptions: UserOption[];
}

@Injectable({
  providedIn: "root"
})
export class ProjektRolesService {
  constructor(private mkUserService: MkUserService) {}

  getProjektRoles(projektId: uuid): Observable<ProjektRoles> {
    const loggedInUser = this.mkUserService.getMarkkollUser();

    return this.mkUserService.getKundUsers(loggedInUser.kundId)
      .pipe(map(kundUsers => ({
        entries: this.entriesFromUsers(kundUsers, loggedInUser, projektId),
        userOptions: this.optionsFromUsers(kundUsers, loggedInUser)
      })));
  }

  private entriesFromUsers(kundUsers: MarkkollUser[],
                           loggedInUser: MarkkollUser,
                           projektId: uuid): UserRoleEntry[] {

    const users = kundUsers
      .map(user => this.entryFromUser(user, loggedInUser, projektId))
      .filter(entry => !!entry.roll);

    return users;
  }

  private isDisabled(user: MarkkollUser, loggedInUser: MarkkollUser): boolean {
    return (user.id === loggedInUser.id) || this.isKundAdmin(user);
  }

  private isKundAdmin(user: MarkkollUser): boolean {
    return user.roles
      .some(role => role?.roleType === RoleType.KUNDADMIN && role?.objectId === user.kundId);
  }

  private entryFromUser(user: MarkkollUser, loggedInUser: MarkkollUser, projektId: uuid): UserRoleEntry {
    const projektRole = user.roles.find(role => role.objectId === projektId)?.roleType;

    return {
      user: user,
      roll: this.isKundAdmin(user) ? RoleType.KUNDADMIN : projektRole,
      disabled: this.isDisabled(user, loggedInUser)
    }
  }

  private optionsFromUsers(kundUsers: MarkkollUser[], loggedInUser: MarkkollUser): UserOption[] {
    return kundUsers.map(user => ({
      user: user,
      disabled: this.isDisabled(user, loggedInUser)
    }));
  }
}
