import { Directive, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import FileSaver from "file-saver";
import { filter, finalize, switchMap } from "rxjs/operators";
import {
  Agartyp,
  Avtalsstatus,
  ElnatProjekt,
  FastighetsProjektInfo,
  FiberProjekt,
  FiberVarderingsprotokoll,
  Geometristatus,
  ProjektInfo,
  ProjektTyp,
  ElnatRotnetto,
  ElnatVarderingsprotokoll,
} from "../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { MkAgare } from "../../model/agare";
import { MkAvtal } from "../../model/avtal";
import { uuid } from "../../model/uuid";
import { AgareService } from "../../services/agare.service";
import { AvtalService } from "../../services/avtal.service";
import { DialogService } from "../../services/dialog.service";
import { DokumentService } from "../../services/dokument.service";
import { FastighetService } from "../../services/fastighet.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { VersionMessage } from "../fastighet/fastighet.component";
import { replace } from "../../common/array-util";
import { ProjektService } from "../../services/projekt.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";
import { XpErrorService } from "../../../../../lib/error/error.service";
import { MkProjektkartaService } from "../../services/projektkarta.service";
import { Projekt } from "../../model/projekt";

@Directive({selector:"registerenhet-container"})
export abstract class MkRegisterenhetContainer implements OnInit, OnChanges {
  /** Projekt-ID */
  @Input() projektId: uuid;

  /** Fastighets-ID */
  @Input() fastighetId: uuid;

  /** Projekttyp */
  @Input() projektTyp: string;

  /** Event när samfälligheten tas bort. */
  protected registerenhetRemove = new EventEmitter<void>();

  /** Event när samfälligheten ändras. */
  protected registerenhetChange = new EventEmitter<void>();

  avtal: MkAvtal;
  projekt: Projekt;
  vp: ElnatVarderingsprotokoll;
  fiberVp: FiberVarderingsprotokoll;
  versionMessage: VersionMessage;
  isSavingAnteckningar = false;
  isSavingIntrang = false;
  isGeneratingAvtal = false;

  constructor(protected activatedRoute: ActivatedRoute,
              protected agareService: AgareService,
              protected avtalService: AvtalService,
              protected dialogService: DialogService,
              protected dokumentService: DokumentService,
              protected fastighetService: FastighetService,
              protected notificationService: XpNotificationService,
              protected translation: TranslocoService,
              protected projektService: ProjektService,
              protected varderingsprotokollService: ElnatVarderingsprotokollService,
              protected fiberVarderingsprotokollService: MkFiberVarderingsprotokollService,
              protected errorService: XpErrorService,
              protected mkProjektkartaService: MkProjektkartaService) {}

  abstract onOmbudCreated();

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.projektId && this.projektTyp) {
      this.getProjekt();
      this.avtalService.getAvtal(this.projektId, this.fastighetId).subscribe(avtal => {
        this.avtal = avtal;
        this.versionMessage = this.avtalService.getGeometristatusMessage(avtal.geometristatus);
        this.getVarderingsprotokoll();
      });
    }
  }

  getProjekt() {
    switch(this.projektTyp) {
      case ProjektTyp.FIBER:
        this.getFiberProjekt();
        break;
      case ProjektTyp.LOKALNAT:
      case ProjektTyp.REGIONNAT:
        this.getElnatProjekt();
        break;
      default:
        throw new Error("Okänd projekttyp: " + this.projektTyp);
    }
  }

  private getElnatProjekt() {
    this.projektService.getElnatProjekt(this.projektId).subscribe(
      elnatProjekt => {
        this.projekt = elnatProjekt;
      }
    );
  }

  private getFiberProjekt() {
    this.projektService.getFiberProjekt(this.projektId).subscribe(
      fiberProjekt => {
        this.projekt = fiberProjekt;
      }
    );
  }

  onAnteckningarChange(anteckning: string) {
    const info: FastighetsProjektInfo = {
      fastighetsId: this.fastighetId,
      projektId: this.projektId,
      anteckning: anteckning,
      ersattning: this.avtal.ersattning
    };

    this.isSavingAnteckningar = true;
    this.fastighetService
        .setFastighetProjektInfo(this.fastighetId, this.projektId, info)
        .pipe(finalize(() => this.isSavingAnteckningar = false))
        .subscribe(
          _ => {
            this.avtal.anteckning = anteckning;
            this.registerenhetChange.emit();
            this.notifySuccessTranslate("xp.common.saved");
          },
          error => {
            this.notificationService.error(error);
          });
  }

  onAvtalStatusChange(status: Avtalsstatus) {
    const dialog = status == Avtalsstatus.ERSATTNINGUTBETALD ?
      this.dialogService.confirmErsattningUtbetaldDialog() :
      this.dialogService.confirmAvtalstatusDialog();

    dialog
      .pipe(
        filter(confirm => confirm === true),
        switchMap(_ => this.avtalService.setAvtalstatus(this.projektId, this.fastighetId, status))
      )
      .subscribe(() => {
        this.avtal.lagfarnaAgare = this.avtal.lagfarnaAgare.map(ag => ({...ag, status: status}));
        this.avtal.tomtrattsinnehavare = this.avtal.tomtrattsinnehavare.map(ag => ({...ag, status: status}));
        this.avtal.ombud = this.avtal.ombud.map(ag => ({ ...ag, status: status}));
        this.avtal = { ...this.avtal, status: status };
        this.mkProjektkartaService.highlightFastighetInMap(this.fastighetId, false);
        this.getLoggbok();
        this.registerenhetChange.emit();
        this.notifySuccessTranslate("mk.fastighetsinformation.statusSaved");
      });
  }

  onConfirmVersionChange() {
    switch (this.avtal.geometristatus) {
      case Geometristatus.NY:
      case Geometristatus.UPPDATERAD:
        this.fastighetService
            .resetGeometristatus(this.projektId, this.fastighetId)
            .subscribe(status => {
          this.avtal.geometristatus = status;
          this.versionMessage = null;
          this.registerenhetChange.emit();
        });
        break;
      case Geometristatus.BORTTAGEN:
        this.fastighetService.removeFastighet(this.projektId, this.fastighetId).subscribe(() => {
          this.versionMessage = null;
          this.registerenhetRemove.emit();
        });
        break;
    }
    this.getAvtalStatusAndUpdateMap();
  }

  onIntrangChange(ersattning: number) {
    const info: FastighetsProjektInfo = {
      fastighetsId: this.fastighetId,
      projektId: this.projektId,
      anteckning: this.avtal.anteckning,
      ersattning: ersattning
    };

    this.isSavingIntrang = true;
    this.fastighetService
        .setFastighetProjektInfo(this.fastighetId, this.projektId, info)
        .pipe(finalize(() => this.isSavingIntrang = false))
        .subscribe(
          _ => {
            this.avtal.ersattning = ersattning;
            this.notifySuccessTranslate("xp.common.saved");
          },
          error => {
            this.notificationService.error(error);
          });
  }

  onOmbudCreate(ombud: MkAgare) {
    this.agareService.addAgare(this.projektId, this.fastighetId, ombud).subscribe(o => {
      this.avtal.ombud = [...this.avtal.ombud, o];
      this.onOmbudCreated();
      this.registerenhetChange.emit();
      this.getAvtalStatusAndUpdateMap();
      this.notifySuccessTranslate("mk.agareinformation.ombudAdded");
    });
  }

  onAgareChange(agare: MkAgare) {
    const idEqualAgareFn = (ag: MkAgare) => ag.id === agare.id;

    // Kontaktperson är unik, rensa om vi har ny kontaktperson
    if (!this.allAgare.find(idEqualAgareFn).kontaktperson && agare.kontaktperson) {
      this.avtal.lagfarnaAgare = this.avtal.lagfarnaAgare.map(ag => ({...ag, kontaktperson: false}));
      this.avtal.ombud = this.avtal.ombud.map(ag => ({...ag, kontaktperson: false}));
    }

    this.agareService.editAgare(agare).subscribe(ag => {
      switch (ag.agartyp) {
        case Agartyp.LF:
          this.avtal.lagfarnaAgare = replace(this.avtal.lagfarnaAgare, idEqualAgareFn, ag);
          break;
        case Agartyp.TR:
          this.avtal.tomtrattsinnehavare = replace(this.avtal.tomtrattsinnehavare, idEqualAgareFn, ag);
          break;
        case Agartyp.OMBUD:
          this.avtal.ombud = replace(this.avtal.ombud, idEqualAgareFn, ag);
          break;
      }

      this.registerenhetChange.emit();
      this.getAvtalStatusAndUpdateMap();
      this.getLoggbok();
      this.notifySuccessTranslate("xp.common.saved");
    });
  }

  onOmbudDelete(ombud: MkAgare) {
    this.dialogService
        .deleteOmbudDialog()
        .pipe(
          filter(confirm => confirm === true),
          switchMap(_ => this.agareService.deleteAgare(ombud))
        )
        .subscribe(() => {
          this.avtal.ombud = this.avtal.ombud.filter(o => o.id !== ombud.id);
          this.registerenhetChange.emit();
          this.getAvtalStatusAndUpdateMap();
          this.notifySuccessTranslate("mk.agareinformation.ombudDeleted");
        });
  }

  onExportXlsxClick() {
    // TODO User has exported Haglöf HMS, add log entry
    this.dokumentService.getVarderingSkogsmarkXlsx(this.projektId, this.fastighetId).subscribe(
      file => FileSaver.saveAs(file.blob, file.name)
    );
  }

  signAvtalCheckAllChange(isChecked: boolean) {
    this.agareService.setInkluderaIAvtal(this.allAgare, isChecked).subscribe(() => {
      this.allAgare.forEach(ag => ag.inkluderaIAvtal = isChecked);
      this.getAvtalStatusAndUpdateMap();
      this.notifySuccessTranslate("xp.common.saved");
    });
  }

  onSkapaAvtalClick() {
    this.isGeneratingAvtal = true;
    this.dokumentService
      .getAvtalPDF(this.projektId, this.fastighetId)
      .pipe(finalize(() => this.isGeneratingAvtal = false))
      .subscribe(
        file => {
          FileSaver.saveAs(file.blob, file.name);
          this.notifySuccessTranslate("mk.fastighetsinformation.avtalGenerated");
        },
        error => this.errorService.handleBlobError(error)
      );
  }

  private get allAgare(): MkAgare[] {
    return [
      ...this.avtal.lagfarnaAgare,
      ...this.avtal.tomtrattsinnehavare,
      ...this.avtal.ombud
    ];
  }

  private getAvtalStatusAndUpdateMap() {
    this.avtalService.getAvtalstatus(this.projektId, this.fastighetId).subscribe(status => {
      this.avtal.status = status;
      this.mkProjektkartaService.highlightFastighetInMap(this.fastighetId, false);
    });
  }

  private getLoggbok() {
    this.avtalService.getLoggbok$(this.projektId, this.fastighetId).subscribe(loggbok => {
      this.avtal.loggbok = loggbok;
    });
  }

  private getVarderingsprotokoll(): void {
    if (this.projektTyp === ProjektTyp.FIBER) {
      this.fiberVarderingsprotokollService.getFiberVarderingsprotokollWithAvtalId(this.projektId, this.avtal.id).subscribe(res => {
        this.fiberVp = res;
      });
    } else {
      this.varderingsprotokollService.getVarderingsprotokollWithAvtalId(this.projektId, this.avtal.id).subscribe(res => {
        this.vp = res;
      });
    }
  }

  private notifySuccessTranslate(messageTranslate: string) {
    this.notificationService.success(this.translation.translate(messageTranslate));
  }

  private onRotnettoChange(rotnetto: number) {
  }
}
