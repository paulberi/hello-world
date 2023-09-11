import { Injectable } from "@angular/core";
import { BehaviorSubject, concat, Observable, of } from "rxjs";
import { tap, toArray } from "rxjs/operators";
import { Kund } from "../../../../../generated/kundconfig-api";
import { UserAndRole } from "../../../../../generated/markkoll-api";
import { AdminApiService, KundApiService, MarkkollUser, ProjektApiService, Role, RoleType, UserApiService, UserInfo } from "../../../../../generated/markkoll-api";
import { UserWithRoll } from "../markhandlaggning/projekt/projekt-behorighet/projekt-behorighet.container";
import { uuid } from "../model/uuid";

interface UpdateUserRole {
  userId: string;
  roll: Role;
}
@Injectable({
  providedIn: "root"
})
export class MkUserService {
  markkollUser: BehaviorSubject<MarkkollUser>;

  constructor(private adminApiService: AdminApiService,
              private userApiService: UserApiService,
              private projektApiService: ProjektApiService,
              private kundApiService: KundApiService) {
    this.markkollUser = new BehaviorSubject(null);
  }

  setMarkkollUser(user: MarkkollUser) {
    this.markkollUser.next(user);
  }

  getMarkkollUser$(): Observable<MarkkollUser> {
    return this.markkollUser;
  }

  getMarkkollUser(): MarkkollUser {
    return this.markkollUser.getValue();
  }

  fetchMarkkollUser(userId: string): Observable<MarkkollUser> {
    return this.userApiService.getUser(userId);
  }

  fetchAndUpdateCurrentMarkkollUser(): Observable<MarkkollUser> {
    return this.fetchMarkkollUser(this.getMarkkollUser().id).pipe(tap(user => this.setMarkkollUser(user)));
  }

  createUser(kundId: uuid, userInfo: UserInfo): Observable<void> {
    return this.adminApiService.createUser(kundId, userInfo);
  }

  getKundUsers(kundId: uuid): Observable<MarkkollUser[]> {
    return this.kundApiService.getKundUsers(kundId);
  }

  getProjektUsers(projektId: uuid): Observable<MarkkollUser[]> {
    return this.projektApiService.getProjektUsers(projektId);
  }

  /**
   * TODO: Bör flyttas till backenden när tid finns. Eventuellt merga ihop med skapandet av projektet, så att det körs i samma transaktion
   */
  addProjektrolesToUserArray(projektId: uuid, users: UserWithRoll[]): Observable<void[]> {
    const apiCalls: Observable<void>[] = [];

    users?.forEach(user => {
      if (user.roll === RoleType.PROJEKTADMIN) {
        apiCalls.push(this.adminApiService.addRoleProjektadmin(projektId, user.user.email));
      } else if (user.roll === RoleType.PROJEKTHANDLAGGARE) {
        apiCalls.push(this.adminApiService.addRoleProjekthandlaggare(projektId, user.user.email));
      }
    });
    return concat(...apiCalls).pipe(toArray());
  }

  updateUserRoles(projektId: uuid, updatedUsers: UserWithRoll[]): Observable<void> {
    const updateUserRoleList: UserAndRole[] = [];
    updatedUsers.forEach(userWithRoll => {
      updateUserRoleList.push({userId: userWithRoll.user.id, roleType: userWithRoll.roll});
    });

    return this.adminApiService.setProjektUsersRoles(projektId, updateUserRoleList);
  }

  updateUserRolesSkipKundadmin(projektId: uuid, updatedUsers: UserWithRoll[]): Observable<void> {
    return this.updateUserRoles(projektId, updatedUsers.filter(u => u.roll !== RoleType.KUNDADMIN));
  }

  editUser(userId: string, userInfo: UserInfo): Observable<MarkkollUser> {
    return this.userApiService.updateUserInfo(userId, userInfo);
  }

  deleteUser(user: MarkkollUser) {
    return this.userApiService.deleteUser(user.id);
  }

  getKund(kundId): Observable<Kund> {
    return this.kundApiService.getKund(kundId);
  }
}
