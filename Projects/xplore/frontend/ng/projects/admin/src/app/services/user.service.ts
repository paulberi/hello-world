import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import { UserService as UserApiService, MarkkollUser, UserInfo, MarkkollService } from "../../../../../generated/kundconfig-api";

export class UserDetails {
  username: string;
  fullname: string;
  company: string;
  authorities: string[];

  constructor(username: string, fullname: string, company: string, authorities: string[]) {
    this.username = username;
    this.fullname = fullname;
    this.company = company;
    this.authorities = authorities;
  }
}


@Injectable({
  providedIn: "root"
})
export class UserService {
  userDetails: UserDetails;

  constructor(private http: HttpClient, private userApiService: UserApiService, private markkollApiService: MarkkollService) {
  }

  /**
   * Loads user details from the backend.
   *
   * In most use cases the application will access the public member userDetails. This is used
   * by the auth guard so the public member will always be initialized.
   */
  loadUserDetails() {
    this.userDetails = new UserDetails("Test", "Test testson",
      "Företaget",
      [""],
      );
    return of(this.userDetails);
  }

  createKundAdmin(kundId: string, user: UserInfo): Observable<MarkkollUser> {
    return this.markkollApiService.createKundAdmin(kundId, user);
  }

  deleteUser(id: string) {
    return this.markkollApiService.deleteUser(id);
  }

  getPermissions(id, produkt) {
    return this.userApiService.getPermissions(id, produkt);
  }

  getAllUsers() {
    return this.userApiService.getAllUsers();
  }

  getUsersForKund(kundId: string): Observable<MarkkollUser[]> {
    return this.markkollApiService.getUsersForKund(kundId);
  }
}
