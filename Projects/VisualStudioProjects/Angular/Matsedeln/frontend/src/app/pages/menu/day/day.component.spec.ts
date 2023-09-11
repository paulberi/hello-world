import { Day } from './../../../models/day';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MenuMockup } from 'src/app/mockups/menu-mockup';
import { DayComponent } from './day.component';

describe('DayComponent', () => {
  let component: DayComponent;
  let fixture: ComponentFixture<DayComponent>;
  let dayCreator : DayCreator;
  let menuMockup : MenuMockup;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dayCreator = new DayCreator();
    menuMockup = new MenuMockup();
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('Check foodspecs', () => {
    it('should return foodspec', () => {
      component.day = dayCreator.getDay(1);
      expect(component.getFoodSpecs(menuMockup.getMenu().meals[0])).toContain('Fläsk');
    });
  });

});

class DayCreator {
  getDay(mealsPerDay : number) : Day {
      let day : Day = new Day;
      day.date = new Date("2021-05-20");
      for(let i = 0; i < mealsPerDay; i++) {
        day.meals.push({_id:"abc" + mealsPerDay, mealName: "fläskpannkaka", mealDate: new Date(day.date), foodSpecs: ["pig"]});
      }
    return day;
  }
}
