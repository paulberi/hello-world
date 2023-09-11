import { Component, Injector, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild } from "@angular/core";
import moment from "moment";
import { Subscription } from "rxjs";
import { map } from "rxjs/operators";
import { DialogService } from "../../../../services/dialog.service";
import { MkHanteraIntrangDelegate } from "../../delegates/hantera-intrang-delegate.impl";
import { IntrangLogItem } from "./verktyg/intrang-form/intrang-form.component";
import { MkIntrangsVerktygslada } from "./verktygslada/verktygslada.component";
import { IntrangMap } from "./intrang-map";
import { IntrangUpdateEvent } from "./verktyg/intrang-update-event";
import { IntrangVerktygToken } from "./verktyg/intrang-verktyg-token";
import { IntrangVerktygComponent } from "./verktyg/intrang-verktyg.component";
import { IntrangVerktygDirective } from "./verktyg/intrang-verktyg.directive";
import { IntrangVerktygEnum } from "./verktyg/intrang-verktyg-enum";
import { ProjektIntrang } from "../../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../../lib/ui/notification/notification.service";
import { Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { MkIntrangVerktyg } from "./verktyg/intrang-verktyg";
import { IntrangVerktygOption } from "./verktyg/intrang-verktyg-option";

@Component({
  selector: "mk-hantera-intrang",
  templateUrl: "./hantera-intrang.component.html",
  styleUrls: ["./hantera-intrang.component.scss"],
  providers: []
})
export class MkHanteraIntrangComponent implements OnInit, OnChanges, OnDestroy {
  @Input() delegate: MkHanteraIntrangDelegate;
  @Input() verktygslada: MkIntrangsVerktygslada;

  @ViewChild(IntrangVerktygDirective, { static: true })
  mkIntrangVerktygHost!: IntrangVerktygDirective;

  changeLog: IntrangLogItem[] = [];

  private intrangMap: IntrangMap;
  private activeVerktyg: MkIntrangVerktyg<unknown>;

  private verktygChangeSubscription: Subscription;
  private verktygUpdateSubscription: Subscription;
  private intrangUpdateSubscription: Subscription;

  constructor(private dialogService: DialogService,
    private notificationService: XpNotificationService,
    private router: Router,
    private translate: TranslocoService) { }

  ngOnInit() {
    this.delegate.getProjektIntrang()
      .pipe(
        map((intrang: ProjektIntrang[]) => new IntrangMap(intrang)))
      .subscribe(intrangMap => this.setIntrang(intrangMap));

    this.delegate.onDeactivate$.subscribe(() => {
      this.verktygslada.setOpen(false);
      this.activeVerktyg.inactivate();
    });
    this.delegate.onActivate$.subscribe(() => this.verktygslada.selectTool(IntrangVerktygEnum.SELECT));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.verktygslada) {
      this.verktygChangeSubscription?.unsubscribe();
      this.verktygChangeSubscription = this.verktygslada?.toolChange$.subscribe(tool => this.setActiveVerktyg(tool));
    }
  }

  ngOnDestroy(): void {
    this.activeVerktyg?.inactivate();
    this.intrangUpdateSubscription?.unsubscribe();
    this.verktygChangeSubscription?.unsubscribe();
    this.verktygUpdateSubscription?.unsubscribe();
  }

  openToolbox() {
    this.verktygslada.setOpen(true);
  }

  onSaveChanges() {
    const saveIntrang$ = this.delegate.saveChanges(this.intrangMap.getAll());

    this.dialogService.saveIntrangDialog(saveIntrang$).subscribe(() => {
      this.notificationService.success(this.translate.translate("mk.redigeraProjekt.versionSaved"));
      this.changeLog = [];
      const currentUrl = this.router.url;
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate([currentUrl]);
      });
    });
  }

  onCancelChanges() {
    this.dialogService.cancelIntrangDialog().subscribe(() => {
      this.changeLog = [];
      this.delegate.getProjektIntrang().subscribe(intrang => {
        const projektIntrang = new IntrangMap(intrang);
        this.setIntrang(projektIntrang);
        this.delegate.setMapFeatures(this.intrangMap);
      });
    })
  }

  private setActiveVerktyg(tool: IntrangVerktygEnum) {
    this.activeVerktyg?.inactivate();
    this.verktygUpdateSubscription?.unsubscribe();
    this.intrangUpdateSubscription?.unsubscribe();

    const option = this.delegate.getVerktygComponent(tool);

    const verktygComponent = this.updateComponentRef(option);
    this.intrangUpdateSubscription = verktygComponent.intrangUpdate$.subscribe(event =>
      this.onIntrangUpdate(event)
    );

    this.activeVerktyg = option.verktyg
    this.verktygUpdateSubscription = this.activeVerktyg.verktygUpdate$.subscribe(event =>
      verktygComponent.onVerktygUpdate(event)
    );
    this.activeVerktyg.activate();
  }

  private updateComponentRef<T>(option: IntrangVerktygOption<T>): IntrangVerktygComponent<T> {
    const viewContainerRef = this.mkIntrangVerktygHost.viewContainerRef;
    viewContainerRef.clear();

    const injector: Injector = Injector.create({
      providers: [
        {
          provide: IntrangVerktygToken,
          useValue: option.data
        }
      ],
    });

    return viewContainerRef.createComponent<IntrangVerktygComponent<T>>(option.component,
      { injector: injector }).instance;
  }

  private setIntrang(intrangMap: IntrangMap) {
    this.intrangMap = intrangMap;
    this.delegate.setMapFeatures(intrangMap);
  }

  private onIntrangUpdate(event: IntrangUpdateEvent) {
    this.changeLog.push({
      timestamp: moment.now().toString(),
      message: event.message
    });

    event.updateFn(this.intrangMap);
    this.setIntrang(this.intrangMap);

    this.activeVerktyg.reset();
  }
}
