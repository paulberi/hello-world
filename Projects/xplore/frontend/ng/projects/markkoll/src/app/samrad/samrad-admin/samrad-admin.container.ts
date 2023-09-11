import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { ProjektRsp as Projekt } from '../../../../../../generated/samrad-api';
import { SamradStatus } from '../../model/samrad/samradStatus';
import { uuid } from '../../model/uuid';
import { ProjektService } from "../../services/projekt.service";
import { SamradService } from '../../services/samrad/samrad.service';
import { MkConfirmUnsavedChanges } from '../../unsaved-changes.guard';
import { SamradAdminComponent } from './samrad-admin.component';
import { KundRsp } from '../../../../../../generated/samrad-api';
import { ProjektTyp } from '../../../../../../generated/markkoll-api';


@Component({
  selector: 'mk-samrad-admin',
  templateUrl: './samrad-admin.container.html',
})
export class SamradAdminContainerComponent implements OnInit, MkConfirmUnsavedChanges{
  projektId: uuid = this.route.snapshot.params.projektId;
  projektTyp: ProjektTyp


  @ViewChild(SamradAdminComponent) private samradAdminComponent: SamradAdminComponent ;

  projektnamn$: Observable<string>;
  samradStatus: typeof SamradStatus;
  samradProjekt: Projekt;
  markkollProjekt: any;
  samradId: string;
  loadingDone = false;
  kundId: string;
  kundInformation: KundRsp;


  constructor(private route:ActivatedRoute, private projektService: ProjektService, private samradService: SamradService) { }

  ngOnInit(): void {
      this.projektnamn$ = this.projektService.getProjektName(this.projektId);
      this.route.params
      .pipe(
        mergeMap((params) =>
          this.projektService.getProjektInfo(params.projektId)
        ),
        mergeMap((projekt) => {
          this.projektTyp = projekt.projektTyp
          this.markkollProjekt = projekt;
            return this.samradService.getByMarkkollId$(
            this.markkollProjekt.kundId,
            projekt.id
          )
          }),
        mergeMap((Projekt) => {
          this.samradId = Projekt.samradId
          return this.samradService.getKund$(this.markkollProjekt.kundId)
        }),
        mergeMap((samrad: any) => {
          this.kundInformation = samrad
          this.kundId = samrad.id
          return this.samradService.getSamrad$(
            samrad.id,
            this.samradId
          );
        }),
        catchError(err => {
          return of(null);
        })
      )
      .subscribe(samrad => {
        this.samradProjekt = samrad
        this.loadingDone = true;
      });
   }

  hasUnsavedChanges(): boolean {
    return this.samradAdminComponent.hasUnsavedChanges();
  }

  updateSamrad(samrad: Projekt) {
    this.samradProjekt = samrad;
  }

}
