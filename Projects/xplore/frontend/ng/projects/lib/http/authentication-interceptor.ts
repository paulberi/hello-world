import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {OAuthErrorEvent, OAuthModuleConfig, OAuthService, OAuthStorage} from "angular-oauth2-oidc";
import {catchError, flatMap, map} from "rxjs/operators";
import {LoginService} from "../oidc/login.service";

export const RETURN_URL_PROPERTY = "returnUrl";

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
  constructor(private loginService: LoginService,
              private authStorage: OAuthStorage,
              private oauthService: OAuthService,
              private moduleConfig: OAuthModuleConfig) {
  }

  private checkUrl(url: string): boolean {
    const found = this.moduleConfig.resourceServer.allowedUrls.find(u => url.startsWith(u));
    return !!found;
  }

  // * Om vi har en giltig refresh token, refresha och försök igen vid 401
  // * Försök inte igen om det är refresh anropet som får 401 :)
  // * Om det inte går att refresha, redirecta till login-sidan.
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.moduleConfig) { return next.handle(request); }
    if (!this.moduleConfig.resourceServer) { return next.handle(request); }
    if (!this.moduleConfig.resourceServer.allowedUrls) { return next.handle(request); }

    const url = request.url.toLowerCase();

    if (!this.checkUrl(url)) { return next.handle(request); }

    let header;
    if (this.loginService.specialAuth) {
      header = this.loginService.specialAuth;
    } else {
      const token = this.authStorage.getItem("access_token");

      if (token !== null) {
        header = "Bearer " + token;
      }
    }

    let headers = request.headers;

    if (header) {
      headers = headers.set("Authorization", header);
    }

    request = request.clone({ headers });

    return next.handle(request).pipe(
      catchError(error => {
        if (!this.loginService.specialAuth && !(error.error instanceof ErrorEvent)) {
          if (error.status === 401) {
            if (this.loginService.shouldRefresh()) {
              return this.loginService.refreshToken().pipe(flatMap((tokenResponse) => {
                const newRequest = request.clone({headers: request.headers.set("authorization", "Bearer " + tokenResponse.access_token)});
                return next.handle(newRequest);
              }),
                catchError((refreshError: OAuthErrorEvent | HttpErrorResponse) => {
                  if (refreshError instanceof HttpErrorResponse) {
                    if (refreshError.error && refreshError.error.error && refreshError.error.error === "invalid_grant") {
                      if (!window.location.pathname.startsWith("/oauthLogin")) {
                        sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
                      }

                      this.oauthService.initLoginFlow();
                    }
                  } else {
                    if (refreshError.reason && refreshError.reason["error"] === "invalid_grant") {
                      if (!window.location.pathname.startsWith("/oauthLogin")) {
                        sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
                      }

                      this.oauthService.initLoginFlow();
                    }
                  }

                  throw refreshError;
                }));
            } else {
              sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
              this.oauthService.initLoginFlow();
            }
          }
        }
        return throwError(error);
      })
    );
  }
}
