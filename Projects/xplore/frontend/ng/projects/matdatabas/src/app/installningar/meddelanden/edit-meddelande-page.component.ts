import {Component, OnInit} from "@angular/core";
import {switchMap, tap} from "rxjs/operators";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {Observable} from "rxjs";
import {Page} from "../../common/page";
import {Meddelande, MeddelandeService} from "../../services/meddelande.service";

@Component({
  selector: "mdb-edit-meddelande-page",
  template: `
      <div class="main-content">
        <h2>Meddelanden</h2>
        <div class="actions">
          <button routerLink="../../meddelande/new" mat-raised-button color="primary">Lägg till meddelande</button>
        </div>
        <ng-container *ngIf="meddelandePage | async as page">
              <h3>Redigera befintliga meddelanden</h3>
              <div class="messages" >
                  <ng-container *ngFor="let message of page.content">
                  <div>{{message.datum}}</div>
                  <a routerLink="../../meddelande/{{message.id}}">{{message.rubrik}}</a>
                  </ng-container>
              </div>
              <mdb-page-navigation [currentPage]="currentPage" [totalPages]="page.totalPages"></mdb-page-navigation>
          </ng-container>
      </div>
  `,
  styles: [`
      .main-content {
          padding: 0.5rem;
          display: grid;
          grid-gap: 1rem;
      }

      .messages {
          display: grid;
          grid-template-columns: auto auto;
          justify-content: left;
          grid-gap: 0.5rem;
      }
  `]
})
export class EditMeddelandePageComponent implements OnInit {
  meddelandePage: Observable<Page<Meddelande>>;
  currentPage: number;

  constructor(private route: ActivatedRoute, private meddelandeService: MeddelandeService) {
  }

  ngOnInit() {
    this.meddelandePage = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
          const page = +params.get("page");
          return this.meddelandeService.getPage(page, 10).pipe(tap(() => this.currentPage = page));
        }
      ));
  }
}
