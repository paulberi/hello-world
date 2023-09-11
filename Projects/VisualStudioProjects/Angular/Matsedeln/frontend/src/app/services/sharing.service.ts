import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Menu } from '../models/menu';
import { Week } from '../models/week';

@Injectable({
  providedIn: 'root'
})
export class SharingService {

weekObserver: Subject<Week>;
menuObserver: Subject<Menu>;

  constructor() {
    this.weekObserver = new Subject<Week>();
    this.menuObserver = new Subject<Menu>();
  }

  setWeek(week : Week) {
    this.weekObserver.next(week);
  }

  getObservableWeek() : Subject<Week> {
    return this.weekObserver;
  }

  setMenu(menu : Menu) {
    this.menuObserver.next(menu);
  }

  getObservableMenu() : Subject<Menu> {
    return this.menuObserver;
  }
}
