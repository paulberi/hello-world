import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  ViewChild,
} from "@angular/core";
import { mergeMap } from "rxjs/operators";
import { Samrad } from "../../../../../../../generated/markkoll-api";
import { ProjektRsp as Projekt } from "../../../../../../../generated/samrad-api";
import { SamradStatus } from "../../../model/samrad/samradStatus";
import { DialogService } from "../../../services/dialog.service";
import { SamradService } from "../../../services/samrad/samrad.service";
import { SamradProjektinformationPresenter } from "../samrad-edit-projekt/samrad-edit.presenter";
import { environment } from "../../../../environments/environment";
import { uuid } from "../../../model/uuid";
import { HttpClient } from "@angular/common/http";
import { KundRsp } from "../../../../../../../generated/samrad-api";

@Component({
  selector: "mk-samrad-projekt",
  providers: [SamradProjektinformationPresenter],
  templateUrl: "./samrad-projekt.component.html",
  styleUrls: ["./samrad-projekt.component.scss"],
})
export class SamradProjektComponent implements OnChanges {
  constructor(
    private samradService: SamradService,
    private dialogService: DialogService,
    private http: HttpClient
  ) {}

  @ViewChild(SamradProjektinformationPresenter) private samradProjektInformationPresenter: SamradProjektinformationPresenter;

  isDisabled = false;

  projekt: Projekt;

  status: string;

  samradIMarkkoll: Samrad;

  canSave = false;

  @Input() markkollProjekt: any;
  @Input() samradProjekt: Projekt;
  @Input() kundId: string;
  @Input() kundInformation: KundRsp;

  @Output() updateSamrad = new EventEmitter<Projekt>();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.samradProjekt) {
      this.status = this.samradProjekt?.publiceringStatus;
    }
  }

  onSubmit(): void {
    if(!this.samradProjekt) {

      this.samradService.getKund$(this.markkollProjekt.kundId)
      .pipe(
        mergeMap((samradKund) => {
          this.projekt = {
            ...this.projekt,
            ingress: "",
            publiceringStatus: "UTKAST",
          }
          return this.samradService.saveSamrad$(samradKund.id, this.projekt)
        }),
        mergeMap((samrad: Samrad) => {
          this.updateSamrad.emit(samrad);
          const samradId = {
            samradId: samrad.id,
          };
          return this.samradService.saveSamradId$(
            this.markkollProjekt.kundId,
            this.markkollProjekt.id,
            samradId
          );
        })
      ).subscribe();
    } else {
      const updatedSamrad: Projekt = {
        ...this.samradProjekt,
        ...this.projekt
      }
      this.samradService.getKund$(this.markkollProjekt.kundId).pipe(
        mergeMap((samradKund) => {
          return this.samradService.updateSamradProjekt$(samradKund.id, this.samradProjekt.id, updatedSamrad)
        })
      ).subscribe();
      this.updateSamrad.emit(updatedSamrad);
    }
    this.samradProjektInformationPresenter.onSubmit();
  }
  onDelete(): void {
    this.samradService.deleteSamradProjekt(
      this.kundInformation.id,
      this.samradProjekt.id
    ).subscribe();
  }
  onPublicera(): void {
    const updatedSamrad: Projekt = {
      ...this.samradProjekt,
      ...this.projekt,
      publiceringStatus: SamradStatus.PUBLICERAD
    }
    this.samradService.getKund$(this.markkollProjekt.kundId).pipe(
      mergeMap((samradKund) => {
        const kundId = samradKund.id
        this.saveGeometriForProjekt(kundId)
        return this.samradService.updateSamradProjekt$(samradKund.id, this.samradProjekt.id, updatedSamrad)
      })
    ).subscribe();
    this.updateSamrad.emit(updatedSamrad);
    this.samradProjektInformationPresenter.onSubmit();
  }

  onArkivera(): void {
    this.dialogService.arkiveraSamradDialog().subscribe(() => {
      const updatedSamrad: Projekt = {
        ...this.samradProjekt,
        ...this.projekt,
        publiceringStatus: SamradStatus.ARKIVERAD
      }
      this.samradService.getKund$(this.markkollProjekt.kundId).pipe(
        mergeMap((samradKund) => {
          return this.samradService.updateSamradProjekt$(samradKund.id, this.samradProjekt.id, updatedSamrad)
        })
      ).subscribe();
      this.updateSamrad.emit(updatedSamrad);
    })
  }

  saveGeometriForProjekt(kundId: any) {
    this.getIntrangFeatures(environment.markkollWfsUrl, this.markkollProjekt.id).pipe(
      mergeMap((geometri) => {
        const geoJSON =  {
          geom: JSON.stringify(geometri)
        }
         return this.samradService.saveGeometriToSamrad$(kundId, this.samradProjekt.id, geoJSON)
      })
     ).subscribe();
  }

  onUpdateProjekt(projekt: Projekt): Projekt {
    this.projekt = projekt;
    return projekt;
  }

  setHasUnsavedChanges(canSave: boolean): boolean {
    this.canSave = canSave;
    return canSave;
  }

  hasUnsavedChanges(): boolean {
    return this.canSave;
  }

  isPubliceraButtonDisabled(): boolean {
    if(this.samradProjekt.publiceringStatus === SamradStatus.ARKIVERAD) {
      return true;
    }
    if(this.samradProjekt.publiceringStatus === SamradStatus.UTKAST) {
      return false;
    } else {
      return !this.canSave;
    }
  }

  private getIntrangFeatures(wfsUrl: string, projektId: uuid): any {
    const cqlFilter = `projekt_id='${projektId}'`;
    return this.getFeaturesAsGeoJson(wfsUrl, "markkoll:indata_view", cqlFilter)
  }

  private getFeaturesAsGeoJson(wfsUrl: string, typeName: string, cqlFilter: string, maxFeatures: number = 10000) {
    const params = {
        service: "wfs",
        version: "2.0.0",
        request: "GetFeature",
        typeName,
        outputFormat: "application/json",
        cql_filter: cqlFilter,
        count: maxFeatures.toString()
    };

    return this.http.get(wfsUrl, { params })
  }
}
