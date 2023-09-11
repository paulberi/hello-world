import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { BehaviorSubject, from, Observable } from "rxjs";
import { map, mergeMap } from "rxjs/operators";
import { XpUser } from "../../../generated/kundconfig-api";
import { User } from "../oidc/login.service";

export interface PasswordChange {
  currentPassword: string;
  newPassword: string;
  confirmation: string;
}

/**
 * Den här servicen hanterar 2 olika sorters useronjekt
 * @param XpUser Ett userobjekt med information som hämtas från databasen med hjälp av kundconfig
 * @param User Ett userobjekt som skapas i login.service när man loggar in en användare mot keycloak
 */

@Injectable({
  providedIn: "root"
})
export class XpUserService {
  private user: BehaviorSubject<User>;
  private xpUser: BehaviorSubject<XpUser>;
  headers = new HttpHeaders();
  constructor(private http: HttpClient, private oAuthService: OAuthService) {
    this.headers.append("Access-Control-Allow-Origin", "*");
    this.user = new BehaviorSubject(null);
    this.xpUser = new BehaviorSubject(null);
  }

  public getXpUser(): XpUser {
    return this.xpUser.getValue();
  }

  public getXpUser$(): Observable<XpUser> {
    return this.xpUser;
  }

  public getXpUserRoles$(): Observable<string[]> {
    return this.user.pipe(map(user => user.roles));
  }

  public setXpUser(user: XpUser): void {
  this.xpUser.next(user);
  }

  public getUser(): User {
    return this.user.getValue();
  }

  public getUser$(): Observable<User> {
    return this.user;
  }

  public getUserRoles$(): Observable<string[]> {
    return this.user.pipe(map(user => user.roles));
  }

  public setUser(user: User): void {
  this.user.next(user);
  }

  private refreshUserClaims$(): Observable<any> {
    return from(this.oAuthService.loadUserProfile())
    .pipe(map(claims => {
      const currentUser = this.getUser();
      currentUser.claims = claims;
      this.setUser(currentUser);
    }));
  }

  public updateKeyCloakPassword$(data: PasswordChange, authIssuer: string): Observable<Object> {
    return this.http.post(authIssuer + "/account/credentials/password", data, {headers: this.headers});
  }

  private getKeyCloakUserInfo$(authIssuer: string): Observable<Object> {
    return this.http.get(authIssuer + "/account", {headers: this.headers});
  }

  public updateKeyCloakEmail$(email: string, authIssuer: string): Observable<void | Object> {
    return this.getKeyCloakUserInfo$(authIssuer)
      .pipe(
        mergeMap(userInfo => {
          userInfo["email"] = email;

          return this.http.post(authIssuer + "/account", userInfo, {headers: this.headers});
        }),
        mergeMap(() => {
          return this.refreshUserClaims$();
        })
      );
  }
}
