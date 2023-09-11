import { LoginService } from "../../../../../lib/oidc/login.service";
import { XpUserService } from "../../../../../lib/user/user.service";
import { LogoutGQL as LogoutAdminGQL } from './auth/auth.admin.generated';
import { LogoutGQL as LogoutShopGQL } from './auth/auth.shop.generated';
import { Injectable, Injector } from "@angular/core";
import { Apollo } from "apollo-angular";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";

export enum MMDefaultErrorMessage {
  BadRequest = "Något gick tyvärr fel.",
  Unauthorized = "Behörighet saknas.",
  Forbidden = "Du har inte behörighet att komma åt informationen.",
  NotFound = "Kunde tyvärr inte hitta sidan.",
  InternalServerError = "Ett okänt fel uppstod. Vänligen försök igen om en stund.",
  BadGateway = "Kommer inte åt servern. Vänligen försök igen om en stund.",
  ServiceUnavailable = "Kommer inte åt servern. Vänligen försök igen om en stund.",
  GatewayTimeout = "Det tog för lång tid att få svar från servern. Vänligen försök igen om en stund.",
  Default = "Ett okänt fel uppstod. Vänligen försök igen om en stund."
}


@Injectable({
  providedIn: "root"
})
//Ny service för att undvika cirkulära beroenden
//Används i graphql.module för auth och notification
export class GraphQlModuleService {

  constructor(
    private loginService: LoginService,
    private xpUserService: XpUserService,
    private injector: Injector,
  ) { }

  public logout() {
    this.xpUserService.setUser(null);
    this.loginService.logout();
    sessionStorage.clear();
  }

  public logoutAdminVendure() {
    const logoutGQL = this.injector.get<LogoutAdminGQL>(LogoutAdminGQL);
    const apollo = this.injector.get<Apollo>(Apollo);
    logoutGQL.mutate().subscribe();
    apollo.client.clearStore();
    apollo.use("adminApi").client.clearStore();
  }

  public logoutShopVendure() {
    const logoutGQL = this.injector.get<LogoutShopGQL>(LogoutShopGQL);
    const apollo = this.injector.get<Apollo>(Apollo);
    logoutGQL.mutate().subscribe();
    apollo.client.clearStore();
  }

  public getUser() {
    return this.xpUserService.getUser();
  }

  public notifyUserOnError(message: string) {
    const notificationService = this.injector.get<XpNotificationService>(XpNotificationService);
    notificationService.error(message || "")
  }
}
