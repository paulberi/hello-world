import { Injectable, LOCALE_ID } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Week } from '../models/week';
import { Menu } from '../models/menu';
import { Day } from '../models/day';

@Injectable({
  providedIn: 'root'
})
export class DateHandlerService {

  constructor() { }

  getWeeks(menu : Menu) : Week[] {
    let datePipe: DatePipe = new DatePipe('en-US');
    let week : Week = new Week();
    let weeks : any[];
    weeks  = [];
    var newEndDate = new Date(menu.endDate);
    let day : Day;
    week.startDate = menu.startDate;
    week.weekNr = datePipe.transform(menu.startDate, 'w');
    var date = new Date(menu.startDate);
    date.setHours(0);

    for (date; date <= newEndDate; date.setDate(date.getDate() + 1)) {
      if(datePipe.transform(date, 'EEEE') === 'Monday') {
        week = new Week();
        week.startDate = new Date(date);
        week.weekNr = datePipe.transform(date, 'w');
      } else if (datePipe.transform(date, 'EEEE') === 'Sunday' || datePipe.transform(date, 'mediumDate') === datePipe.transform(newEndDate, 'mediumDate')) {
        week.endDate = new Date(date);
        weeks.push(week);
      }
      if (datePipe.transform(date, 'EEEE') === 'Monday' || datePipe.transform(date, 'EEEE') === 'Tuesday' || datePipe.transform(date, 'EEEE') === 'Wednesday'
      || datePipe.transform(date, 'EEEE') === 'Thursday' || datePipe.transform(date, 'EEEE') === 'Friday') {
        day = new Day();
        day.date = new Date(date);
        menu.meals.forEach(meal => {
          if(datePipe.transform(meal.mealDate, 'mediumDate') === datePipe.transform(date, 'mediumDate')) {
            day.meals.push(meal);
          }
        });
        week.days.push(day);
      }
    }
    return weeks;
  }

  getCurrentWeek() : string {
    let dateNow : Date = new Date(Date.now());
    let datePipe: DatePipe = new DatePipe('en-US');
    return datePipe.transform(dateNow, 'w');
  }

  getPreviousWeek(weeks : Week [], week : Week) : Week{
    let previousWeekNr = parseInt(week.weekNr) - 1;
    let returnWeek : Week;
    weeks.forEach(week => {
      if(parseInt(week.weekNr) === previousWeekNr) {
        returnWeek = week;
      }
    });
    return returnWeek;
  }

  getNextWeek(weeks : Week[], week : Week) : Week {
    let nextWeek = parseInt(week.weekNr) + 1;
    let returnWeek : Week;
    weeks.forEach(week => {
      if(parseInt(week.weekNr) === nextWeek) {
        returnWeek = week;
      }
    });
    return returnWeek;
  }

}
