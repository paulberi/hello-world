import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {UserService} from "../services/user.service";
import { ConfigService } from "../../../../lib/config/config.service";

@Component({
  selector: "mdb-start",
  template: `
    <div class="banner">
      <h2> {{headerText}} </h2>
      <div>Inloggad: {{userService.userDetails.fullname}}</div>
    </div>
    <div class="main-content">
      <div class="messages">
        <mdb-messages></mdb-messages>
      </div>
      <div class="quick-menu-container mat-elevation-z2">
        <h2>Gå direkt till</h2>
        <mat-divider></mat-divider>
        <div class="quick-menu">
          <div *ngIf="userService.userDetails.isMatrapportor()">
            <a routerLink="/rapportera-matdata">
              Rapportera in mätdata</a>
          </div>
          <div>
            <a routerLink="/matobjekt">
              Hantera mätobjekt</a>
          </div>
        </div>
        <div class="stats" *ngIf="userService.userDetails.isMatrapportor()">
          <div><a routerLink="/granskning">Mätvärden att granska</a></div>
          <div>{{this.antalOgranskadeMatvarden | async}} st</div>
          <div><a routerLink="/paminnelser">Påminnelser</a></div>
          <div>{{this.antalPaminnelser | async}} st</div>
          <div><a routerLink="/larm">Larm</a></div>
          <div>{{this.antalLarm | async}} st</div>
        </div>
      </div>
      <div class="information-container mat-elevation-z2">
        <h2>Information</h2>
        <mat-divider></mat-divider>
        <div class="information">
          <div>
            <h5 class="subheader">Koordinatsystem</h5>
            <div> {{projectionDescriptionText}}</div>
          </div>
          <div>
            <h5 class="subheader">Dokument</h5>
            <div *ngFor="let doc of documents">
              <div><a href={{doc.url}} target="_blank">{{doc.name}}</a></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ["./start.component.scss"]
})
export class StartComponent implements OnInit {

  antalOgranskadeMatvarden: Observable<number>;
  antalPaminnelser: Observable<number>;
  antalLarm: Observable<number>;

  headerText: string;
  projectionDescriptionText: string;
  documents: any[];

  documentsDefault: any[] = [
    { name: "Manual - Mätrapportör", url: "/files/manual_metria_miljokoll_matrapportor.pdf"},
    { name: "Manual - Tillståndshandläggare", url: "/files/manual_metria_miljokoll_tillstandshandlaggare.pdf"},
  ];

  constructor(public userService: UserService, private configService: ConfigService) {
  }

  ngOnInit() {
    if (this.userService.userDetails.isMatrapportor()) {
      this.antalOgranskadeMatvarden = this.userService.getOgranskadeMatvardenCount();
      this.antalPaminnelser = this.userService.getPaminnelserCount();
      this.antalLarm = this.userService.getLarmCount();
    }
    this.headerText = this.configService.config.app.startPageHeaderText;
    this.projectionDescriptionText = this.configService.config.app.projectionDescription;
    this.documents = this.configService.config.app.documents || this.documentsDefault;
  }
}
