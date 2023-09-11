import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { ProjektRsp as Projekt } from '../../../../../../generated/samrad-api';
import { uuid } from '../../model/uuid';
import { SamradProjektComponent } from './samrad-projekt/samrad-projekt.component';
import { KundRsp } from '../../../../../../generated/samrad-api';
import { ProjektInfo } from '../../../../../../generated/markkoll-api';
import { ProjektTyp } from '../../../../../../generated/markkoll-api';


@Component({
  selector: 'mk-samrad-admin-ui',
  templateUrl: './samrad-admin.component.html',
  styleUrls: ['./samrad-admin.component.scss']
})
export class SamradAdminComponent implements OnChanges, OnInit {

  @Input() projektnamn: string;
  @Input() projektId: uuid;
  @Input() projektTyp: ProjektTyp;
  @Input() samradProjekt: Projekt;
  @Input() markkollProjekt: ProjektInfo ;
  @Input() loadingDone = false;
  @Input() kundId: string;
  @Input() kundInformation: KundRsp;

  @Output() updateSamradx = new EventEmitter<Projekt>();

  status: string;

  goBackRouterLink: any;

  @ViewChild(SamradProjektComponent) private samradProjektComponent: SamradProjektComponent;

  ngOnInit() {
    this.goBackRouterLink = ['/projekt/' + this.projektTyp + '/' + this.projektId + '/avtal/'];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes.samradProjekt) {
      this.status = this.samradProjekt?.publiceringStatus;
    }
  }

  hasUnsavedChanges(): boolean {
    return this.samradProjektComponent.hasUnsavedChanges();
  }

  updateSamrad(samrad: Projekt) {
    this.updateSamradx.emit(samrad);
  }


}
