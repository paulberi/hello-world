import { Component,ChangeDetectionStrategy, ViewChild, TemplateRef } from '@angular/core';
//import {startOfDay, endOfDay, subDays, addDays, endOfMonth, IsSameDay, IsSameMonth, addHours} from 'date-fns';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Calendar';
}
