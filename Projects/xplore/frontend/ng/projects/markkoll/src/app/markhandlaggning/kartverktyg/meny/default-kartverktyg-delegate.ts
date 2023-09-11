import { Observable } from "rxjs";

export interface MkKartmenyvalDelegate {
  activateMenyval(): void;
  inactivateMenyval(): void;

  get onActivate$(): Observable<void>;
  get onDeactivate$(): Observable<void>;
}
