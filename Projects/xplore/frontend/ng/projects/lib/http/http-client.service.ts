import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {catchError} from "rxjs/operators";
import {Observable, throwError} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class ErrorHandlingHttpClient {

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
  }

  get<T>(url: string, options?: object): Observable<T> {
    return this.http.get<T>(url, options).pipe(
      catchError((err: HttpErrorResponse) => {
        const shortUrl = url.substring(0, url.indexOf("?"));
        this.snackBar.open("Fel vid anrop till " + shortUrl + ": HTTP " + err.status + " " + err.statusText, "OK", {
          verticalPosition: "top"
        });
        return throwError(err);
      })
    );
  }

  post(url: string, body: any | null, options?: any): Observable<any> {
    return this.http.post(url, body, options).pipe(
      catchError((err: HttpErrorResponse)  => {
        this.snackBar.open("Fel vid anrop till " + url + ": HTTP " + err.status + " " + err.statusText, "OK", {
          verticalPosition: "top"
        });
        return throwError(err);
      })
    );
  }
}
