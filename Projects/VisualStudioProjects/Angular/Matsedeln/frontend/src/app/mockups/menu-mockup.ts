import { Menu } from "../models/menu";
import { Meal } from "../models/meal";

export class MenuMockup {
  menu : Menu;

  constructor() {
    this.menu = new Menu();

    this.menu._id = '123';
    this.menu.startDate = new Date("2021-05-03");
    this.menu.endDate = new Date("2021-06-20");
    this.menu.menuName = 'Matsedel test';

    this.menu.meals = new Array();

    let meal : Meal = new Meal();
    meal._id = "111";
    meal.mealName = "Meal 1";
    meal.mealDate = new Date("2021-05-13");
    meal.foodSpecs.push("pig");
    this.menu.meals.push(meal);

    let meal2 : Meal = new Meal();
    meal2._id = "222";
    meal2.mealName = "Meal 2";
    meal2.mealDate = new Date("2021-05-17");
    meal2.foodSpecs.push("veg");
    this.menu.meals.push(meal2);

    let meal3 : Meal = new Meal();
    meal3._id = "333";
    meal3.mealName = "Meal 3";
    meal3.mealDate = new Date("2021-05-19");
    meal3.foodSpecs.push("hot");
    this.menu.meals.push(meal3);
  }

  getMenu() : Menu {
    return this.menu;
  }
}
