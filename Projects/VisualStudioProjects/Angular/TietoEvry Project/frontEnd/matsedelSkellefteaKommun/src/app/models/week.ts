import { Day } from "./day";


export class Week {

  startDate: Date;
  endDate: Date;
  weekNr: string;

  days: Day[];


  constructor() {
    this.startDate=new Date(Date.now());
    this.endDate=new Date(Date.now());
    this.days = new Array();
  }

}
