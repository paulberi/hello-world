import { By } from '@angular/platform-browser';
import { AuthService } from '@auth0/auth0-angular';
import { Day } from './../../models/day';
import { Week } from './../../models/week';
import { Subject } from 'rxjs';
import { SharingService } from './../../services/sharing.service';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuComponent } from './menu.component';

describe('MenuComponent', () => {
  let component: MenuComponent;
  let fixture: ComponentFixture<MenuComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MenuComponent ],
      providers: [
        {provide: SharingService, useClass: SharingServiceStub}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const week : Week = new Week();
    week.startDate = new Date("2021-05-17");
    week.endDate = new Date("2021-05-19");
    let day1 = new Day();
    day1.date = new Date("2021-05-17");
    let day2 = new Day();
    day2.date = new Date("2021-05-18");
    let day3 = new Day();
    day3.date = new Date("2021-05-19");
    week.days.push(day1, day2, day3)
    component.week = week;
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should create 1 div for each day', (done) => {
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const dayElement = fixture.debugElement.queryAll(By.css('.day-div'));
        expect(dayElement.length).toBe(3);
      })
      done();
    });
  });

});

class SharingServiceStub {
  sub :Subject<Week>;

  getObservableWeek() {
    this.sub  = new Subject<Week>();
    const week : Week = new Week();
    week.startDate = new Date("2021-05-17");
    week.endDate = new Date("2021-05-19");
    let day1 = new Day();
    day1.date = new Date("2021-05-17");
    let day2 = new Day();
    day2.date = new Date("2021-05-18");
    let day3 = new Day();
    day3.date = new Date("2021-05-19");
    week.days.push(day1, day2, day3)
    this.sub.next(week);
    return this.sub;
  }

  setWeek() {
    const week : Week = new Week();
    week.startDate = new Date("2021-05-17");
    week.endDate = new Date("2021-05-19");
    let day1 = new Day();
    day1.date = new Date("2021-05-17");
    let day2 = new Day();
    day2.date = new Date("2021-05-18");
    let day3 = new Day();
    day3.date = new Date("2021-05-19");
    week.days.push(day1, day2, day3)
    this.sub.next(week);
  }
}
