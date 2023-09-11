import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { EMPTY, Observable, of } from "rxjs";
import { catchError, map, switchMap, tap } from "rxjs/operators";
import { ElnatProjekt, FiberProjekt, Ledningsagare, NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { XpErrorService } from "../../../../../../lib/error/error.service";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { MkInstallningarService } from "../../../services/installningar.service";
import { MkLedningsagareService } from "../../../services/ledningsagare.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkUserService } from "../../../services/user.service";
import { MkElnatProjekt, MkFiberProjekt } from "./create-projekt.component";

@Component({
  selector: "mk-create-projekt-page",
  templateUrl: "./create-projekt.container.html"
})
export class MkCreateProjektContainerComponent implements OnInit {

  public isCreatingProjekt = false;

  nisKalla: NisKalla;
  ledningsagare: string[] = [];

  constructor(private notificationService: XpNotificationService,
              private translation: TranslocoService,
              private router: Router,
              private projektService: ProjektService,
              private mkUserService: MkUserService,
              private errorService: XpErrorService,
              private installningarService: MkInstallningarService,
              private ledningsagareService: MkLedningsagareService) {}

  ngOnInit() {
    const user = this.mkUserService.getMarkkollUser();

    this.installningarService.getNisKalla(user.kundId).subscribe(res => this.nisKalla = res);

    this.ledningsagareService.getLedningsagareNamn(user.kundId)
        .subscribe(ledningsagare => this.ledningsagare = ledningsagare);
  }

  create(projekt: MkFiberProjekt | MkElnatProjekt) {
    this.isCreatingProjekt = true;
    if (this.isFiberProjekt(projekt.projekt.projektInfo.projektTyp)) {

      const fiberProjekt = (projekt.projekt as FiberProjekt);

      this.projektService.createFiberProjekt(this.mkUserService.getMarkkollUser().kundId, fiberProjekt, projekt.file).pipe(
        switchMap(skapatProjekt => {
          return this.mkUserService.addProjektrolesToUserArray(skapatProjekt.projektInfo.id, projekt.users).pipe(
            catchError(err => {
              console.error("Kunde inte skapa till projektroller: " + err);
              throw err;
            }),
            map(_ => skapatProjekt)
          );
        }),
        switchMap(skapatProjekt => {
          return this.mkUserService.fetchAndUpdateCurrentMarkkollUser().pipe(
            catchError(err => {
              console.error("Kunde inte uppdatera markollanvändaren: " + err);
              throw err;
            }),
            map(_ => skapatProjekt)
          );
        })
      ).subscribe({
        next: (skapatProjekt) => {
          this.isCreatingProjekt = false;
          this.notificationService.success(
            this.translation.translate("mk.createProjekt.projektCreated")
          );
          this.router.navigate(["projekt/", skapatProjekt.projektInfo.projektTyp, skapatProjekt.projektInfo.id, "avtal"]);
        },
        error: (error) => {
          console.error("Kunde inte skapa projektet: " + error);
          this.isCreatingProjekt = false;
          this.errorService.notifyError(error);
        }
      });

    } else if (this.isElnatProjekt(projekt.projekt.projektInfo.projektTyp)) {

      const elnatProjekt = (projekt.projekt as ElnatProjekt);

      this.projektService.createElnatProjekt(this.mkUserService.getMarkkollUser().kundId, elnatProjekt, projekt.file).pipe(
        switchMap(skapatProjekt => {
          return this.mkUserService.addProjektrolesToUserArray(skapatProjekt.projektInfo.id, projekt.users).pipe(
            catchError(err => {
              console.error("Kunde inte skapa till projektroller: " + err);
              throw err;
            }),
            map(_ => skapatProjekt)
          );
        }),
        switchMap(skapatProjekt => {
          return this.mkUserService.fetchAndUpdateCurrentMarkkollUser().pipe(
            catchError(err => {
              console.error("Kunde inte uppdatera markollanvändaren: " + err);
              throw err;
            }),
            map(_ => skapatProjekt)
          );
        })
      ).subscribe({
        next: (skapatProjekt) => {
          this.isCreatingProjekt = false;
          this.notificationService.success(
            this.translation.translate("mk.createProjekt.projektCreated")
          );
          this.router.navigate(["projekt/", skapatProjekt.projektInfo.projektTyp, skapatProjekt.projektInfo.id, "avtal"]);
        },
        error: (error) => {
          console.error("Kunde inte skapa projektet: " + error);
          this.isCreatingProjekt = false;
          this.errorService.notifyError(error);
        }
      });

    }
  }

  private isFiberProjekt(projektTyp: ProjektTyp): boolean {
    return projektTyp === ProjektTyp.FIBER;
  }

  private isElnatProjekt(projektTyp: ProjektTyp): boolean {
    return (projektTyp === ProjektTyp.LOKALNAT) ||
        (projektTyp === ProjektTyp.REGIONNAT);
  }
}
