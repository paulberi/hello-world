import { Subscription } from 'rxjs';
import { Component, OnInit, LOCALE_ID } from '@angular/core';
import { Week } from 'src/app/models/week';
import { SharingService } from 'src/app/services/sharing.service';


@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  week : Week;
  subscriptions : Subscription[] = [];

  constructor( private sharingService : SharingService) {
  }

  ngOnInit(): void {
     let sub : Subscription = this.sharingService.getObservableWeek().subscribe((week : Week) => {
      this.week = week;
     });
     this.subscriptions.push(sub);
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => {
      sub.unsubscribe();
    })
  }
}
