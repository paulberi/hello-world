import { Meal } from "./meal";

export class Day {
  date: Date;
  meals: Meal[];

  constructor() {
    this.meals = new Array();
  }
}
