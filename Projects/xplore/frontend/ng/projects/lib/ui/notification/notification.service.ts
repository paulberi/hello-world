import { Injectable, Inject } from "@angular/core";
import { NOTIFICATION } from "./notification.config";
import { Notyf } from "notyf";

/**
 * Visa notifikationer för systemstatus av typen success eller error.
 * Wrapper för biblioteket Notyf, https://github.com/caroso1222/notyf
 */

@Injectable({
  providedIn: "root"
})
export class XpNotificationService {

  constructor(@Inject(NOTIFICATION) private notification: Notyf) {
  }

  success(message: string) {
    this.notification.success(message);
  }

  error(message: string) {
    this.notification.error(message);

  }
}
