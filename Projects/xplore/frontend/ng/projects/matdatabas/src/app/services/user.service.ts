import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {catchError, map, tap} from "rxjs/operators";

export class UserDetails {
  username: string;
  fullname: string;
  company: string;
  defaultKartlagerId: number | undefined;
  authorities: string[];

  constructor(username: string, fullname: string, company: string, authorities: string[], defaultKartlagerId: number | undefined) {
    this.username = username;
    this.fullname = fullname;
    this.company = company;
    this.authorities = authorities;
    this.defaultKartlagerId = defaultKartlagerId;
  }

  isAdmin() {
    return this.authorities.includes("ROLE_ADMINISTRATÖR");
  }

  isMatrapportor() {
    return this.authorities.includes("ROLE_MÄTRAPPORTÖR");
  }

  isObservator() {
    return this.authorities.includes("ROLE_OBSERVATÖR");
  }

  isTillstandshandlaggare() {
    return this.authorities.includes("ROLE_TILLSTÅNDSHANDLÄGGARE");
  }
}


@Injectable({
  providedIn: "root"
})
export class UserService {
  userDetails: UserDetails;

  constructor(private http: HttpClient) {
  }

  /**
   * Loads user details from the backend.
   *
   * In most use cases the application will access the public member userDetails. This is used
   * by the auth guard so the public member will always be initialized.
   */
  loadUserDetails() {
    return this.http.get<UserDetails>("/api/user/user-details").pipe(
      map(resp => new UserDetails(resp.username, resp.fullname, resp.company, resp.authorities, resp.defaultKartlagerId)),
      tap(userDetails => this.userDetails = userDetails)
    );
  }

  getLarmCount(): Observable<number> {
    return this.http.get("/api/user/larm").pipe(
      map(result => result["count"]),
      catchError(err => undefined)
    );
  }

  getOgranskadeMatvardenCount(): Observable<number> {
    return this.http.get("/api/user/matvarden/ogranskade").pipe(
      map(result => result["count"]),
      catchError(err => of(undefined))
    );
  }

  getPaminnelserCount(): Observable<number> {
    return this.http.get("/api/user/paminnelser").pipe(
      map(result => result["count"]),
      catchError(err => undefined)
    );
  }
}
