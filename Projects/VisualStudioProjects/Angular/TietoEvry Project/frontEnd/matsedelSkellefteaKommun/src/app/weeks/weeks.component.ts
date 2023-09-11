import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Day } from '../models/day';
import { Week } from '../models/week';


@Component({
  selector: 'app-weeks',
  templateUrl: './weeks.component.html',
  styleUrls: ['./weeks.component.scss']
})
export class WeeksComponent implements OnInit {
  day: Day;
  weeks:Week[];
  myDate=new Date();
  newDate: string;
  week:Week;

  constructor(private datePipe: DatePipe) {
    this.newDate= this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
   }


  ngOnInit(): void {
  }
  getWeek(days:Day): Week[]{

    let datePipe:   DatePipe =new DatePipe('en-US');
    this.week =new Week();
    let endDates= new Date(this.week.endDate);
    let startDates= new Date(this.week.startDate);
    this.week.weekNr=datePipe.transform(startDates, 'w');

    for(startDates; startDates<=endDates; startDates.setDate(startDates.getDate())+1){
      if(datePipe.transform(startDates, 'EEEE')==='Monday'){
        this.week=new Week();
        this.week.weekNr=datePipe.transform(startDates,'w');
      }
      else if(datePipe.transform(startDates, 'EEEE')==='Sunday'){
        this.week.endDate = new Date(endDates);;
        this.weeks.push(this.week);
      }
      if (datePipe.transform(startDates, 'EEEE') === 'Monday' || datePipe.transform(startDates, 'EEEE') === 'Tuesday' || datePipe.transform(startDates, 'EEEE') === 'Wednesday'
      || datePipe.transform(startDates, 'EEEE') === 'Thursday' || datePipe.transform(startDates, 'EEEE') === 'Friday')
      this.day=new Day();
      this.day.date=new Date(startDates);
      this.week.days.push(this.day);


    }



    return this.weeks;

  }

}
