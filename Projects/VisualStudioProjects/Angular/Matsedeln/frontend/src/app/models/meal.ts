export class Meal {
  _id: string;
  mealName: string;
  mealDate: Date;
  foodSpecs: string[];

  constructor(){
    this.foodSpecs = new Array();
  }

}
