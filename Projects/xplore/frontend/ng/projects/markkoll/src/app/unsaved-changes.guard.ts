import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { ConfirmExitDialogComponent } from "./common/confirm-exit-dialog/confirm-exit-dialog.component";

export interface MkConfirmUnsavedChanges {
  hasUnsavedChanges: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable()
export class CanDeactivateGuard implements CanDeactivate<MkConfirmUnsavedChanges> {
  constructor(private dialog: MatDialog) { }

  canDeactivate(component: MkConfirmUnsavedChanges,
                currentRoute: ActivatedRouteSnapshot,
                currentState: RouterStateSnapshot,
                nextState?: RouterStateSnapshot):
                boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    if (component.hasUnsavedChanges && component.hasUnsavedChanges()) {
        return this.dialog.open(ConfirmExitDialogComponent).afterClosed();
    } else {
      return true;
    }
  }
}
