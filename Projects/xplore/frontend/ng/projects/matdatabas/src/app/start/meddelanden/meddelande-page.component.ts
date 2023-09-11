import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {Meddelande, MeddelandeService} from "../../services/meddelande.service";
import {Observable} from "rxjs";
import {switchMap, tap} from "rxjs/operators";
import {Page} from "../../common/page";

@Component({
  selector: "mdb-messages",
  template: `
    <ng-container *ngIf="meddelandePage | async as page">
      <div class="message" *ngFor="let message of page.content">
        <h2>{{message.rubrik}}</h2>
        <div class="date subheader">{{message.datum}}</div>
        <div class="text" [innerHTML]="message.meddelande"></div>
        <a *ngIf="message.url" class="external-link" href="{{message.url}}" target="_blank">
          <span>Mer information</span>
          <mat-icon aria-hidden="false" aria-label="Example home icon">launch</mat-icon>
        </a>
      </div>
      <div class="navigation">
        <mdb-page-navigation [currentPage]="currentPage" [totalPages]="page.totalPages"></mdb-page-navigation>
      </div>
    </ng-container>
  `,
  styles: [`
    .message {
      margin-bottom: 1rem;
    }

    h2 {
      text-transform: uppercase;
      margin-bottom: 0 !important;
    }

    .date {
      font-size: smaller;
      margin-bottom: 0.5rem;
    }

    .external-link {
      display: inline-grid;
      grid-template-columns: auto auto;
      align-items: center;
      grid-gap: 0.5rem;
      margin-top: 0.5rem;
    }

    .external-link .mat-icon {
      font-size: 1em;
      height: 1em;
    }

    .navigation {
      text-align: center;
      margin-top: 0.5rem;
    }
  `]
})
export class MeddelandePageComponent implements OnInit {
  meddelandePage: Observable<Page<Meddelande>>;
  currentPage: number;

  constructor(private route: ActivatedRoute, private meddelandeService: MeddelandeService) {
  }

  ngOnInit() {
    this.meddelandePage = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
          const page = +params.get("page");
          return this.meddelandeService.getPage(page).pipe(tap(() => this.currentPage = page));
        }
      ));
  }

  showNext(page: Page<Meddelande>) {
    return this.currentPage < page.totalPages - 1;
  }

  showPrev() {
    return this.currentPage > 0;
  }
}
