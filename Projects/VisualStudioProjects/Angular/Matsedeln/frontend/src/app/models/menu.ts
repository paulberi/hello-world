import { Meal } from "./meal";

export class Menu {
  _id: string;
  startDate: Date;
  endDate: Date;
  meals: Meal[];
  menuName : string;
}
