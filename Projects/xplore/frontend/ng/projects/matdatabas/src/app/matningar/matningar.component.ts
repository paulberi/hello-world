import { Component, OnInit } from "@angular/core";
import {Matning, Matningstyp, MatobjektService} from "../services/matobjekt.service";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../services/user.service";

@Component({
  selector: "mdb-matningar",
  template: `
    <div class="main-content">
      <h2>{{matningstypNamn}}</h2>
      <mdb-search-matning (selected)="onMatningarUpdated($event)" [matningstypId]="matningstypId"
                            [matobjektId]="matobjektId" [updatedMatning]="updatedMatning">
      </mdb-search-matning>
      <div *ngIf="!readonly" class="granska-content">
        <h3>Granska</h3>
        <mdb-granska-matning (updated)="onMatningUpdated($event)" [matningar]="matningar" [matningstypId]="matningstypId"
                              [matobjektId]="matobjektId"></mdb-granska-matning>
      </div>
    </div>
  `,
  styles: [
    `
    .main-content {
      display: grid;
      grid-gap: 2rem;
    }

    .granska-content {
      margin-top: 1rem;
    }
    `
  ]
})
export class MatningarComponent implements OnInit {

  matningstypNamn: string;
  matningstypId: number;
  matobjektId: number;
  matningstyp: Matningstyp;
  matningar: number[];
  updatedMatning: Matning;
  readonly = true;

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService,
              private userService: UserService) { }

  ngOnInit() {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.matningstypId = +this.route.snapshot.paramMap.get("id");
    this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.matobjektService.getMatningstyp(this.matobjektId, this.matningstypId).subscribe(res => {
      this.matningstyp = res;
      this.matningstypNamn = res.typ;
    });
  }

  onMatningarUpdated(ids: number[]) {
    this.matningar = ids;
  }

  onMatningUpdated(matning: Matning) {
    this.updatedMatning = matning;
  }

}
