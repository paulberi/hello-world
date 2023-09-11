import { Meal } from './../../../models/meal';
import { DomSanitizer } from '@angular/platform-browser';
import { Day } from './../../../models/day';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-day',
  templateUrl: './day.component.html',
  styleUrls: ['./day.component.scss']
})
export class DayComponent implements OnInit {

  dateToday: Date;

  constructor(private sanitizer: DomSanitizer) {
    this.dateToday = new Date(Date.now());
  }

  @Input() day : Day;

  ngOnInit(): void {
  }

  getFoodSpecs(meal : Meal) : string {
    let returnString : string = '';
    returnString += '&nbsp&nbsp';
    meal.foodSpecs.forEach(foodSpec => {
      if(foodSpec === 'veg') {
        returnString += `<i class="fas fa-seedling" style='color:darkGreen' title="Vegetarisk"></i>`;
      } else if(foodSpec === 'hot') {
        returnString += `<i class="fas fa-pepper-hot" style='color:red' title="Stark"></i>`;
      } else if(foodSpec === 'pig') {
        returnString += `<i class="fas fa-bacon" style='color:pink' title="Fläsk"></i>`;
      }
      returnString += '&nbsp&nbsp';
    });
    return returnString;
  }

}
