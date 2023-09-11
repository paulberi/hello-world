import {Component, EventEmitter, OnInit, Output, ViewChild} from "@angular/core";
import {Avtalsinstallningar, Beredare, ProjektTyp, RoleType} from "../../../../../../../generated/markkoll-api";
import {flatMap, switchMap, tap} from "rxjs/operators";
import {forkJoin, from, Observable, of} from "rxjs";
import {ProjektService} from "../../../services/projekt.service";
import {ActivatedRoute, Router} from "@angular/router";
import {XpErrorService} from "../../../../../../lib/error/error.service";
import {XpNotificationService} from "../../../../../../lib/ui/notification/notification.service";
import {TranslocoService} from "@ngneat/transloco";
import {uuid} from "../../../model/uuid";
import {State} from "../../../model/loadState";
import { MkProjektinformationPresenter } from "./projektinformation.presenter";
import { MkUserService } from "../../../services/user.service";
import { MkLedningsagareService } from "../../../services/ledningsagare.service";
import { UpdateProjektEvent } from "../edit-projekt/edit-projekt.component";
import { DialogService } from "../../../services/dialog.service";
import { Projekt } from "../../../model/projekt";
import { UserRoleEntry, UserOption } from "../user-roles/user-roles.component";
import { ProjektRolesService } from "../../../services/projekt-roles.service";
import { BeredareService } from "../../../services/beredare.service";
import { MkProjektinformationComponent } from "./projektinformation.component";
import { MkConfirmUnsavedChanges } from "../../../unsaved-changes.guard";
import { AvtalsinstallningarService } from "../../../services/avtalsinstallningar.service";
import { MkProjektkartaLayer, MkProjektkartaService } from "../../../services/projektkarta.service";

interface ProjektUpdateAction<T> {
  update: (event: UpdateProjektEvent) => Observable<T>;
  afterUpdate: (event: UpdateProjektEvent, value: T) => void
}

@Component({
  selector: "mk-projektinformation",
  templateUrl: "./projektinformation.container.html",
  providers: [MkProjektinformationPresenter]
})
export class MkProjektinformationContainerComponent implements OnInit, MkConfirmUnsavedChanges {

  projektId: uuid = this.activatedRoute.parent.snapshot.params.projektId;
  projektTyp: ProjektTyp = this.activatedRoute.parent.snapshot.params.projektTyp;

  @ViewChild(MkProjektinformationComponent) private projektInformationComponent: MkProjektinformationComponent;

  projekt: Projekt;
  beredare: Beredare;
  projektRoleEntries: UserRoleEntry[];
  avtalsinstallningar: Avtalsinstallningar;

  projektState: State;
  ledningsagareOptions: string[] = [];
  isDeletingProjekt = false;

  userOptions: UserOption[];

  projektHasVersioner$: Observable<boolean> = of(false);

  /** Ett uppdateringsevent kan trigga flera endpoints för uppdateringar. Stuva in all information
   * som behövs för det här */
  private readonly projektUpdateActions: ProjektUpdateAction<any>[] = [
    {
      update: event => this.updateProjektMethod(event.projekt.projektInfo?.projektTyp)(this.projektId, event.projekt),
      afterUpdate: (_, projekt) => this.projekt = projekt
    },
    {
      update: event => this.mkUserService.updateUserRolesSkipKundadmin(this.projektId, event.users),
      afterUpdate: (event, _) => this.projektRoleEntries = event.users
    },
    {
      update: event => this.beredareService.editBeredare(event.beredare, this.projektId),
      afterUpdate: (event, _) => this.beredare = event.beredare
    },
    {
      update: event => this.avtalsinstallningarService.updateAvtalsinstallningar(this.projektId, event.avtalsinstallningar),
      afterUpdate: (event, _) => this.avtalsinstallningar = event.avtalsinstallningar
    }
  ];

  @Output() projectUpdated = new EventEmitter<string>();

  constructor(private beredareService: BeredareService,
              private activatedRoute: ActivatedRoute,
              private projektService: ProjektService,
              private projektRolesService: ProjektRolesService,
              private router: Router,
              private errorService: XpErrorService,
              private notificationService: XpNotificationService,
              private mkUserService: MkUserService,
              private translate: TranslocoService,
              private ledningsagareService: MkLedningsagareService,
              private avtalsinstallningarService: AvtalsinstallningarService,
              private dialogService: DialogService,
              private projektkartaService: MkProjektkartaService) {}

  hasUnsavedChanges(): boolean {
    return this.projektInformationComponent.hasUnsavedChanges();
  }

  ngOnInit(): void {
    const mkUser = this.mkUserService.getMarkkollUser();

    this.beredareService.getBeredare(this.projektId)
      .subscribe(beredare => this.beredare = beredare);

    this.ledningsagareService.getLedningsagareNamn(mkUser.kundId)
      .subscribe(ledningsagare => this.ledningsagareOptions = ledningsagare);

    this.avtalsinstallningarService.getAvtalsinstallningar(this.projektId)
      .subscribe(avtalsinstallningar => this.avtalsinstallningar = avtalsinstallningar);

    this.getProjekt();

    this.projektRolesService.getProjektRoles(this.projektId).subscribe(pr => {
      this.userOptions = pr.userOptions;
      this.projektRoleEntries = pr.entries;
    });

    this.projektHasVersioner$ = this.projektService.hasVersions(this.projektId);
  }

  onUpdateProjekt(event: UpdateProjektEvent) {
    this.projektState = State.Loading;

    const actions$ = this.projektUpdateActions$(event);
    actions$.subscribe(observedValues => {
      observedValues.forEach((value, index) => this.projektUpdateActions[index].afterUpdate(event, value));

      this.projektState = State.Loaded;
      this.projektkartaService.refreshLayer(MkProjektkartaLayer.INTRANG);
      this.notificationService.success(this.translate.translate("xp.common.saved"));
      this.projektInformationComponent
    },
    error => {
      this.projektState = State.Error;
      this.errorService.notifyError(error);
    });
  }

  onDeleteProjekt(projektId: uuid) {
    this.dialogService.deleteProjektDialog()
      .pipe(
        tap(() => this.isDeletingProjekt = true),
        flatMap(() => this.projektService.deleteProjekt(projektId)),
        flatMap(_ => from(this.router.navigate(["projekt"])))
      )
      .subscribe(_ => {
          window.scroll(0, 0);
          this.notificationService.success(this.translate.translate("mk.redigeraProjekt.projektDeleted"));
        },
        error => {
          this.errorService.notifyError(error);
        });
  }

  private getProjekt() {
    const getProjekt = this.getProjektMethod(this.projektTyp);

    getProjekt(this.projektId).subscribe(
      projekt => {
        this.projekt = projekt;
        this.projektState = State.Loaded;
      }, error => {
        this.projektState = State.Error;
        this.errorService.notifyError(error);
      }
    );
  }

  private getProjektMethod(projektTyp: ProjektTyp): (uuid) => Observable<Projekt> {
    switch (projektTyp) {
      case ProjektTyp.LOKALNAT:
      case ProjektTyp.REGIONNAT:
        return this.projektService.getElnatProjekt.bind(this.projektService);
      case ProjektTyp.FIBER:
        return this.projektService.getFiberProjekt.bind(this.projektService);
      default:
        throw new Error("Unknown projektTyp");
    }
  }

  private updateProjektMethod(projektTyp: ProjektTyp): (uuid, Projekt) => Observable<Projekt> {
    switch (projektTyp) {
      case ProjektTyp.LOKALNAT:
      case ProjektTyp.REGIONNAT:
        return this.projektService.updateElnatProjekt.bind(this.projektService);
      case ProjektTyp.FIBER:
        return this.projektService.updateFiberProjekt.bind(this.projektService);
      default:
        throw new Error("Unknown projektTyp");
    }
  }

  private hasBerakningVaghallareChanged(event: UpdateProjektEvent): boolean {
    return event.avtalsinstallningar?.berakningAbel07 !== this.avtalsinstallningar?.berakningAbel07 ||
        event.avtalsinstallningar?.berakningRev !== this.avtalsinstallningar?.berakningRev
  }

  private projektUpdateActions$(event: UpdateProjektEvent): Observable<any> {
    const actions$ = forkJoin(this.projektUpdateActions.map(pua => pua.update(event)));
    if (this.hasBerakningVaghallareChanged(event)) {
      return this.dialogService.confirmBerakningVaghallareDialog().pipe(
        switchMap(_ => actions$)
      );
    }
    else {
      return actions$;
    }
  }
}
