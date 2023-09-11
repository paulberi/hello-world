import { InjectionToken } from "@angular/core";
import { Notyf } from "notyf";

export const NOTIFICATION = new InjectionToken<Notyf>("NotificationToken",
{
  providedIn: "root",
  factory: () => notificationFactory()
});

/**
 * Konfiguration av notifikationer. Dokumentation finns på https://github.com/caroso1222/notyf 
 */
export function notificationFactory(): Notyf {  
  return new Notyf({
    duration: 5000,
    position: { x: "center", y: "top"},
    ripple: false,
    types: [
      {
        type: "error",
        className: "xp-notification-error",        
      },
      {
        type: "success",
        className: "xp-notification-success",        
      }
    ]
  });
}
