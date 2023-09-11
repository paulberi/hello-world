import { Component, Input, OnChanges, OnInit, SimpleChanges } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { filter, flatMap, map } from "rxjs/operators";
import { EditElnatVp, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { AgareService } from "../../../services/agare.service";
import { AvtalService } from "../../../services/avtal.service";
import { DialogService } from "../../../services/dialog.service";
import { FastighetService } from "../../../services/fastighet.service";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { MkAvtalPageEvent, PageTyp } from "../../../model/avtalPageEvent";
import { uuid } from "../../../model/uuid";
import { MkAvtalSummary } from "../../../model/avtalSummary";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { forkJoin, Observable, of } from "rxjs";
import { replace, replaceInPlace } from "../../../common/array-util";
import { MkProjektkartaClickEvent, MkProjektkartaLayer, MkProjektkartaService } from "../../../services/projektkarta.service";
import { ActivatedRoute } from "@angular/router";
import { EditingContentsEvent } from "../avtal-actions/avtal-actions.container";
import { SelectionTyp } from "../../../model/actions";

@Component({
  selector: "mk-avtal-list",
  templateUrl: "./avtal-list.container.html"
})
export class MkAvtalListContainerComponent implements OnInit {
  projektId: uuid = this.activatedRoute.parent.snapshot.params.projektId;
  projektTyp: ProjektTyp = this.activatedRoute.parent.snapshot.params.projektTyp;

  avtalsfilter: MkAvtalsfilter = { status: null, search: null, registerenhetsIds: null };
  tabIndex = 0;
  fastighetPage: XpPage<MkAvtalSummary>;
  samfallighetPage: XpPage<MkAvtalSummary>;
  fastighetIndex: number;
  samfallighetIndex: number;
  hamtaMarkagareDisabled$: Observable<boolean>;
  skapaAvtalDisabled$;
  numOfAvtalSelected$: Observable<number>;
  registerenhetIds: uuid[] = [];
  editingContents = false;
  editElnatVps: EditElnatVp[] = [];

  readonly pageSizeOptions = [10, 20, 50];

  private pageFastighet = 0;
  private sizeFastighet = this.pageSizeOptions[0];
  private pageSamfallighet = 0;
  private sizeSamfallighet = this.pageSizeOptions[0];

  constructor(private activatedRoute: ActivatedRoute,
    private agareService: AgareService,
    private fastighetService: FastighetService,
    private avtalService: AvtalService,
    private notificationService: XpNotificationService,
    private translation: TranslocoService,
    private dialogService: DialogService,
    private mkProjektkartaService: MkProjektkartaService) { }

  ngOnInit() {
    this.onMapClick(this.mkProjektkartaService.selectedFastighet());
    this.hamtaMarkagareDisabled$ = this.agareService.hasUnimportedAgare(this.projektId);

    this.avtalService
      .getFastighetInfoPage(this.projektId, 0, this.pageSizeOptions[0], null)
      .subscribe(fastighetPage => this.fastighetPage = fastighetPage);

    this.avtalService
      .getSamfallighetInfoPage(this.projektId, 0, this.pageSizeOptions[0], null)
      .subscribe(samfallighetPage => this.samfallighetPage = samfallighetPage);

    this.skapaAvtalDisabled$ = this.fastighetService
      .fastighetIdsUnimported(this.projektId).pipe(map(num => num.length !== 0));

    this.avtalService.getAvtalListChange$().subscribe(_ => {
      this.updateBothPagesAndSetTabIndex();
      this.hamtaMarkagareDisabled$ = this.agareService.hasUnimportedAgare(this.projektId);
    });

    this.mkProjektkartaService.mapClick().subscribe(event => this.onMapClick(event));
  }

  onGenericPageChange(event: MkAvtalPageEvent) {
    this.avtalsfilter = event.filter;
    this.sizeFastighet = event.size;
    this.sizeSamfallighet = event.size;

    switch (event.type) {
      case PageTyp.FAST:
        this.pageFastighet = event.page;
        this.updateFastighetPage().subscribe();
        break;

      case PageTyp.SAMF:
        this.pageSamfallighet = event.page;
        this.updateSamfallighetPage().subscribe();
        break;

      default:
        this.pageFastighet = 0;
        this.pageSamfallighet = 0;
        this.updateBothPagesAndSetTabIndex();
        break;
    }

    this.clearMap();
    this.numOfAvtalSelected$ = this.avtalService.numOfAvtal(
      this.projektId, this.avtalsfilter
    );
  }

  onFastighetIndexChange(index: number) {
    this.fastighetIndex = index;

    if (index === null) {
      this.mkProjektkartaService.clearHighlights();
    } else {
      this.mkProjektkartaService.highlightFastighetInMap(this.fastighetPage.content[index].fastighetId, true);
    }
  }

  onSamfallighetIndexChange(index: number) {
    this.samfallighetIndex = index;

    if (index === null) {
      this.mkProjektkartaService.clearHighlights();
    } else {
      this.mkProjektkartaService.highlightFastighetInMap(this.samfallighetPage.content[index].fastighetId, true);
    }
  }


  onFastighetRemove(_fastighetId: uuid) {
    this.fastighetIndex = null;
    this.updateFastighetPage().subscribe();
  }

  onFastighetChange(fastighetId: uuid) {
    const idEqualFn = (f: MkAvtalSummary) => f.fastighetId === fastighetId;

    this.avtalService.getAvtalSummary(this.projektId, fastighetId).subscribe(avtalInfo => {
      replaceInPlace(this.fastighetPage.content, idEqualFn, avtalInfo);
    });
  }

  onSamfallighetRemove(_fastighetId: uuid) {
    this.samfallighetIndex = null;
    this.updateSamfallighetPage().subscribe();
  }

  onSamfallighetChange(fastighetId: uuid) {
    const idEqualFn = (f: MkAvtalSummary) => f.fastighetId === fastighetId;

    this.avtalService.getAvtalSummary(this.projektId, fastighetId).subscribe(avtalInfo => {
      replaceInPlace(this.samfallighetPage.content, idEqualFn, avtalInfo);
    });
  }

  onRegisterenhetIdsChange(ids: uuid[]): void {
    this.registerenhetIds = ids;
    this.avtalsfilter.registerenhetsIds = this.registerenhetIds;
  }

  onEditingContentsChange(editingContentsEvent: EditingContentsEvent): void {
    if (editingContentsEvent.editingContents) {
      this.editElnatVps = [];
      this.avtalService.getEditElnatVpByUUID(this.projektId, this.getFilterBySelection(editingContentsEvent.selectionTyp))
        .subscribe(res => {
          this.editElnatVps = res;
        });
      this.editingContents = editingContentsEvent.editingContents;
    } else {
      this.editingContents = editingContentsEvent.editingContents;
    }
  }

  onHamtaMarkagareClick() {
    this.fastighetService
      .fastighetIdsUnimported(this.projektId)
      .pipe(
        flatMap(ids =>
          this.dialogService.hamtaMarkagareDialog(ids.length,
            this.agareService.importAgare(this.projektId, { ids }))),
        filter(numOfAgare => numOfAgare !== null)
      ).subscribe(
        numOfAgare => {
          if (numOfAgare > 0) {
            this.notifySuccessTranslate(
              "mk.fastighetslista.markagareGenererade", { antal: numOfAgare }
            );
            this.updateFastighetPage().subscribe();
            this.clearMap();
            this.hamtaMarkagareDisabled$ = of(true);
            this.skapaAvtalDisabled$ = of(false);
          } else if ((numOfAgare as any) === false) {
            // When canceling numOfAgare is false
          } else {
            this.notifyErrorTranslate("mk.fastighetslista.ingaMarkagareGenererade");
          }
        },
        err => {
          this.notifyErrorTranslate(err);
        }
      );
  }

  onSelectionStatusChange() {
    this.updateFastighetPage().subscribe();
    this.updateSamfallighetPage().subscribe();
    this.mkProjektkartaService.refreshLayer(MkProjektkartaLayer.AVTALSSTATUS);
  }

  onSaveElnatVp(editElnatVps: EditElnatVp[]) {
    this.avtalService.updateElnatVpAndAvtalMetadata(this.projektId, editElnatVps)
      .subscribe(_ => {
        this.notificationService.success(this.translation.translate("mk.editVarderingsprotokoll.editSuccess"));
        this.editingContents = false;
      })
  }

  private getFilterBySelection(selectionTyp: SelectionTyp): MkAvtalsfilter {
    if (selectionTyp === SelectionTyp.ALL) {
      return { status: null, search: null, registerenhetsIds: null };
    } else if (selectionTyp === SelectionTyp.SELECTION) {
      return { status: null, search: null, registerenhetsIds: this.registerenhetIds };
    }

    return { ...this.avtalsfilter, registerenhetsIds: null };
  }

  private clearMap() {
    if (this.fastighetPage.content.length + this.samfallighetPage.content.length === 1) {
      this.mkProjektkartaService.clearHighlights();
    }
  }

  private updateFastighetPage(): Observable<number> {
    return this.avtalService
      .getFastighetInfoPage(this.projektId, this.pageFastighet, this.sizeFastighet, this.avtalsfilter)
      .pipe(map(fastighetPage => {
        this.fastighetPage = fastighetPage;
        this.fastighetIndex = fastighetPage.content.length === 1 ? 0 : this.fastighetIndex;
        return fastighetPage.content.length;
      }));
  }

  private updateSamfallighetPage(): Observable<number> {
    return this.avtalService
      .getSamfallighetInfoPage(this.projektId, this.pageSamfallighet, this.sizeSamfallighet, this.avtalsfilter)
      .pipe(map(samfPage => {
        this.samfallighetPage = samfPage;
        this.samfallighetIndex = samfPage.content.length === 1 ? 0 : this.samfallighetIndex;
        return samfPage.content.length;
      }));
  }

  private setTabIndex(fastLength: number, samfLength: number) {
    if (!(fastLength > 0 && samfLength > 0) && !(fastLength === 0 && samfLength === 0)) {
      this.tabIndex = samfLength === 0 ? 0 : 1;
    }
  }

  private updateBothPagesAndSetTabIndex() {
    const fastLength = this.updateFastighetPage();
    const samfLength = this.updateSamfallighetPage();

    forkJoin([fastLength, samfLength]).subscribe(result => {
      this.setTabIndex(result[0], result[1]);
    });
  }

  private notifySuccessTranslate(messageTranslate: string, args?: any) {
    this.notificationService.success(this.translation.translate(messageTranslate, args));
  }

  private notifyErrorTranslate(messageTranslate: string) {
    this.notificationService.error(this.translation.translate(messageTranslate));
  }

  private onMapClick(event: MkProjektkartaClickEvent) {
    this.avtalsfilter = { ...this.avtalsfilter, search: event?.fastighetsbeteckning };
    this.numOfAvtalSelected$ = this.avtalService.numOfAvtal(this.projektId, this.avtalsfilter);
    this.updateBothPagesAndSetTabIndex();
  }
}
