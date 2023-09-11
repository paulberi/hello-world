import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {MatobjektService, TYP_VATTENKEMI} from "../services/matobjekt.service";
import {UserService} from "../services/user.service";

@Component({
  selector: "mdb-matobjekt-container",
  template: `
    <div>
      <h2>Mätobjekt {{namn}}</h2>
      <nav mat-tab-nav-bar>
        <a mat-tab-link routerLink="grunduppgifter" routerLinkActive #a="routerLinkActive" [active]="a.isActive">
          Grunduppgifter
        </a>
        <div [matTooltip]="getTooltip(namn)">
          <a mat-tab-link routerLink="matningstyper" routerLinkActive #b="routerLinkActive" [active]="b.isActive"
             [disabled]="!namn">
            Mätningstyper och mätdata
          </a>
        </div>
        <div [matTooltip]="getTooltip(namn)">
          <a mat-tab-link routerLink="matrundor" routerLinkActive #c="routerLinkActive" [active]="c.isActive"
             [disabled]="!namn">
            Mätrundor
          </a>
        </div>
        <div *ngIf="typ === ${TYP_VATTENKEMI}" [matTooltip]="getTooltip(namn)">
          <a mat-tab-link routerLink="analyser" routerLinkActive #d="routerLinkActive" [active]="d.isActive"
             [disabled]="!namn">
            Analyser (vattenkemi)
          </a>
        </div>
        <div [matTooltip]="getTooltip(namn)" *ngIf="userService.userDetails.isObservator()">
          <a mat-tab-link routerLink="handelser" routerLinkActive #e="routerLinkActive" [active]="e.isActive"
             [disabled]="!namn">
            Händelselogg
          </a>
        </div>
        <div [matTooltip]="getTooltip(namn)" *ngIf="userService.userDetails.isMatrapportor()">
          <a mat-tab-link routerLink="larmhistorik" routerLinkActive #f="routerLinkActive" [active]="f.isActive"
             [disabled]="!namn">
            Larmhistorik
          </a>
        </div>
      </nav>
    </div>

    <router-outlet></router-outlet>
  `,
  styles: [`
    nav {
      margin-bottom: 1rem;
    }

    a:disabled {
      pointer-events: none;
    }
  `]
})
export class MatobjektContainerComponent implements OnInit {
  namn: string;
  typ: number;

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService,
              public userService: UserService) {
  }

  ngOnInit() {

    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const matobjektId = +params.get("id");
        if (matobjektId) {
          this.matobjektService.get(matobjektId).subscribe(matobjekt => {
            this.namn = matobjekt.namn;
            this.typ = matobjekt.typ;
          });
        }
      });
  }

  getTooltip(namn) {
    return namn ? null : "Du måste först spara grunduppgifterna";
  }
}
