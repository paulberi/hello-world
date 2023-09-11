import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {catchError, map, tap} from "rxjs/operators";

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

  constructor(private http: HttpClient) {
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
}
