import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {MatobjektMapinfo} from "../../services/matobjekt.service";
import {UserService} from "../../services/user.service";
import {getStyleImageUrl} from "../../common/components/styles";

@Component({
  selector: "mdb-matobjekt-mapinfo",
  template: `
    <div *ngIf="mapinfo" class="description">
      <a target="_blank" routerLink="/matobjekt/{{mapinfo.id}}">
        <h3>
          <img class="icon" [src]="getStyleImageUrl(mapinfo)">
          {{mapinfo.namn}} - {{mapinfo.typNamn}}
        </h3>
      </a>

      <div class="matobjekt-image">
        <picture *ngIf="mapinfo.bifogadBildThumbnail">
          <img class="img" [src]="mapinfo.bifogadBildThumbnail | secureLoadImage | async" alt="Bild på mätobjekt">
        </picture>
      </div>

      <div><span class="label">Fastighet: </span><span>{{mapinfo.fastighet}}</span></div>
      <div style="clear: left"><span class="label">Läge: </span>{{mapinfo.lage}}</div>

      <ng-container *ngIf="rapporteraLink && userService.userDetails.isMatrapportor()">
        <br/>
        <a type="button" mat-stroked-button
           target="_blank" [routerLink]="['/rapportera-matdata',{matobjekt: mapinfo.id}]">Rapportera mätdata</a>
      </ng-container>

      <ng-container *ngIf="selection">
        <br/>
        <mat-checkbox (change)="selectedChanged.emit($event.checked)" [indeterminate]="selected === null"
                      [checked]="selected"
        >Vald
        </mat-checkbox>
      </ng-container>
    </div>
  `,
  styles: [`
    .matobjekt-image .img {
      height: 64px;
      width: 96px;
      border-radius: 10px;
      object-fit: contain;
      float: right;
    }

    .matobjekt-image .missing {
      border: 1px dashed grey;
      opacity: 0.33;
      align-items: center;
      justify-items: center;
    }

    .icon {
      max-height: 12px;
    }
  `]
})
export class MatobjektMapinfoComponent implements OnInit {
  @Input() mapinfo: MatobjektMapinfo;
  @Input() selection: boolean;
  @Input() selected: boolean;
  @Input() rapporteraLink: boolean;
  @Output() selectedChanged = new EventEmitter<boolean>();

  constructor(public userService: UserService) { }

  ngOnInit() {
  }

  getStyleImageUrl(mapinfo: MatobjektMapinfo) {
    return getStyleImageUrl(mapinfo);
  }
}
