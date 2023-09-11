import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { Observable, of } from "rxjs";
import { MkConfirmationDialogComponent } from "../common/confirmation-dialog/confirmation-dialog.component";
import { filter, switchMap, tap } from "rxjs/operators";
import { Avtalsstatus } from "../../../../../generated/markkoll-api";
import { MkNotificationDialogComponent } from "../common/notification-dialog/notification-dialog.component";
import { MkConfirmationWarningDialogComponent } from "../common/confirmation-warning-dialog/confirmation-warning-dialog.component";

@Injectable({
  providedIn: "root"
})
export class DialogService {
  constructor(private matDialog: MatDialog,
    private translationService: TranslocoService) { }

  appNotUpToDateDialog(): Observable<void> {
    return this.matDialog.open(MkNotificationDialogComponent,
      {
        data: {
          title: "",
          message: this.translationService.translate("mk.appNotUpToDateDialog.message"),
          confirmLabel: this.translationService.translate("mk.appNotUpToDateDialog.confirmLabel")
        }
      }).afterClosed();
  }

  deleteOmbudDialog(): Observable<boolean> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.agareinformation.deleteOmbudTitle"),
          message: this.translationService.translate("mk.agareinformation.deleteOmbudConfirmation"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.delete")
        }
      }).afterClosed();
  }

  deleteProjektDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationWarningDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.redigeraProjekt.deleteProjekt"),
          message: this.translationService.translate("mk.redigeraProjekt.confirmDelete"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.delete")
        },
        width: "650px"
      }
    ).afterClosed().pipe(
      filter(confirm => confirm === true),
      switchMap(_ => of(void 0) as Observable<void>)
    );
  }

  confirmAvtalstatusDialog(): Observable<boolean> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.statusConfirmationDialog.updateStatus"),
          message: this.translationService.translate("mk.statusConfirmationDialog.confirmUpdate"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("mk.statusConfirmationDialog.change")
        }
      }).afterClosed();
  }

  confirmAllAvtalstatusDialog(numOfFastigheter: number, avtalsstatus: Avtalsstatus): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.avtalsstatusSelectionTitle(avtalsstatus),
          message: this.avtalsstatusSelectionMessage(avtalsstatus, numOfFastigheter),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("mk.statusConfirmationDialog.change")
        }
      }).afterClosed().pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  confirmCancelAvtalsjobbDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: "",
          message: "Vill du avbryta avtalsgenereringen",
          dismissLabel: "Ångra och fortsätt skapa",
          confirmLabel: "Ja, avbryt"
        }
      }).afterClosed().pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  confirmErsattningUtbetaldDialog(): Observable<boolean> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.ersattningUtbetaldDialog.ersattningUtbetaldTitle"),
          message: this.translationService.translate("mk.ersattningUtbetaldDialog.ersattningUtbetaldMessage"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("mk.statusConfirmationDialog.change")
        }
      }).afterClosed();
  }

  hamtaMarkagareDialog(numOfFastigheter: number, hamtaMarkagare$: Observable<number>): Observable<number> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        width: "500px",
        data: {
          title: this.translate("mk.agareConfirmationDialog.getMarkagare"),
          message: this.translationService.translate("mk.agareConfirmationDialog.fetchingOwners", { fastigheter: numOfFastigheter }),
          dismissLabel: this.translate("xp.common.cancel"),
          confirmLabel: this.translate("xp.common.ok"),
          preConfirm$: hamtaMarkagare$
        }
      }).afterClosed();
  }

  confirmAvtalSelectionDialog(numOfAvtal: number): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        width: "500px",
        data: {
          title: this.translate("mk.avtalSelectionDialog.title"),
          message: this.translationService.translate("mk.avtalSelectionDialog.message", { numOfAvtal: numOfAvtal }),
          dismissLabel: this.translate("xp.common.cancel"),
          confirmLabel: this.translate("xp.common.ok"),
        }
      }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  confirmInfobrevSelectionDialog(numOfAvtal: number): Observable<boolean> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        width: "500px",
        data: {
          title: this.translate("mk.infobrevSelectionDialog.title"),
          message: this.translationService.translate("mk.infobrevSelectionDialog.message", { numOfAvtal: numOfAvtal }),
          dismissLabel: this.translate("xp.common.cancel"),
          confirmLabel: this.translate("xp.common.ok"),
        }
      }).afterClosed();
  }

  confirmDeleteDokument(dokumentNamn: string): Observable<boolean> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.projektdokument.deleteDialog.title"),
          message: this.translationService.translate("mk.projektdokument.deleteDialog.message", { namn: dokumentNamn }),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.delete")
        }
      }).afterClosed();
  }

  confirmRemoveBilagaDialog(bilagaNamn: string): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.removeBilagaDialog.title"),
          message: this.translationService.translate("mk.removeBilagaDialog.message", { bilagaNamn: bilagaNamn }),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.delete")
        }
      }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  confirmProjektVersionDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationWarningDialogComponent, {
      data: {
        title: this.translationService.translate("mk.redigeraProjekt.editGeometryModalTitle"),
        message: this.translationService.translate("mk.redigeraProjekt.editGeometryMessage"),
        dismissLabel: this.translationService.translate("xp.common.cancel"),
        confirmLabel: this.translationService.translate("mk.redigeraProjekt.editGeometryOk")
      },
      width: "650px"
    }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  confirmBerakningVaghallareDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationWarningDialogComponent, {
      data: {
        title: this.translationService.translate("mk.redigeraProjekt.saveProjectTitle"),
        message: this.translationService.translate("mk.redigeraProjekt.saveProjectMessage"),
        dismissLabel: this.translationService.translate("xp.common.cancel"),
        confirmLabel: this.translationService.translate("xp.common.ok")
      },
      width: "650px"
    }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  deleteVersionDialog(filnamn: string): Observable<void> {
    return this.matDialog.open(MkConfirmationWarningDialogComponent, {
      data: {
        title: this.translationService.translate("mk.redigeraProjekt.deleteGeometryModalTitle", { filnamn: filnamn }),
        message: "",
        dismissLabel: this.translationService.translate("xp.common.cancel"),
        confirmLabel: this.translationService.translate("mk.redigeraProjekt.deleteGeometryOk")
      },
      width: "620px"
    }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  restoreVersionDialog(filnamn: string): Observable<void> {
    return this.matDialog.open(MkConfirmationWarningDialogComponent, {
      data: {
        title: this.translationService.translate("mk.redigeraProjekt.restoreGeometryModalTitle", { filnamn: filnamn }),
        message: this.translationService.translate("mk.redigeraProjekt.restoreGeometryMessage"),
        dismissLabel: this.translationService.translate("xp.common.cancel"),
        confirmLabel: this.translationService.translate("mk.redigeraProjekt.restoreGeometryOk")
      },
      width: "650px"
    }).afterClosed()
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  cancelIntrangDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.kartverktyg.hanteraIntrang.cancelChangesTitle"),
          message: this.translationService.translate("mk.kartverktyg.hanteraIntrang.cancelChanges"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.yes"),
        }
      }).afterClosed()
      .pipe(
        filter(confirm => !!confirm),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  saveIntrangDialog(saveIntrang$: Observable<any>): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.kartverktyg.hanteraIntrang.saveChangesTitle"),
          message: this.translationService.translate("mk.kartverktyg.hanteraIntrang.saveChanges"),
          dismissLabel: this.translationService.translate("xp.common.cancel"),
          confirmLabel: this.translationService.translate("xp.common.yes"),
          preConfirm$: saveIntrang$
        },
        width: "620px"
      }).afterClosed()
      .pipe(
        filter(confirm => !!confirm),
        switchMap(_ => of(void 0) as Observable<void>)
      );
  }

  private translate(msg: string, ...args: any): string {
    return this.translationService.translate(msg, args);
  }

  private avtalsstatusSelectionTitle(avtalsstatus: Avtalsstatus) {
    return avtalsstatus == Avtalsstatus.ERSATTNINGUTBETALD ?
      this.translationService.translate("mk.ersattningUtbetaldDialog.ersattningUtbetaldTitle") :
      this.translationService.translate("mk.statusConfirmationDialog.updateStatus");
  }

  private avtalsstatusSelectionMessage(avtalsstatus: Avtalsstatus, numOfFastigheter: number) {
    if (avtalsstatus == Avtalsstatus.ERSATTNINGUTBETALD) {
      return this.translationService.translate("mk.ersattningUtbetaldDialog.ersattningUtbetaldAllMessage",
        { fastigheter: numOfFastigheter });
    } else {
      return this.translationService.translate("mk.statusConfirmationDialog.confirmUpdateForSelection",
        { antal: numOfFastigheter })
    };
  }

  arkiveraSamradDialog(): Observable<void> {
    return this.matDialog.open(MkConfirmationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.samrad.arkiveraTitel"),
          message: this.translationService.translate("mk.samrad.arkiveraText"),
          confirmLabel: this.translationService.translate("mk.samrad.arkivera"),
          dismissLabel: this.translationService.translate("mk.samrad.avbryt")

        },
        width: "650px"
      }
    ).afterClosed().pipe(
      filter(confirm => confirm === true),
      switchMap(_ => of(void 0) as Observable<void>)
    );
  }

  slugInformationDialog(): Observable<void> {
    return this.matDialog.open(MkNotificationDialogComponent,
      {
        data: {
          title: this.translationService.translate("mk.samrad.urlNamn"),
          message: this.translationService.translate("mk.samrad.slugInformationDialog"),
          confirmLabel: this.translationService.translate("xp.common.ok")
        },
        width: "650px"
      }).afterClosed();
  }
}
