import { Meal } from 'src/app/models/meal';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Menu } from '../models/menu';
import { WebReqService } from './web-req.service';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private webReqService: WebReqService) { }

  getMenus() {
    return this.webReqService.get('public/menu');
  }

  getMenu(_menuId: string) {
    return this.webReqService.get(`public/menu/${_menuId}`);
  }

  updateMenu(menu : Menu) {
    return this.webReqService.patch(`private/menu/${menu._id}`, menu);
  }

  deleteMeal(_menuId: string, _mealId:string){
    return this.webReqService.delete(`private/menu/${_menuId}/meal/${_mealId}`);
  }

  updateMeal(meal : Meal, _menuId : string) {
    return this.webReqService.patch(`private/menu/${_menuId}/meal/${meal._id}`, meal);
  }

  postMeal(meal: Meal,  _menuId : string ) {
    return this.webReqService.post(`private/menu/${_menuId}/meal`, meal);
  }

  getMeal(_menuId : string, _mealId : string) {
    return this.webReqService.get(`public/menu/${_menuId}/meal/${_mealId}`);
  }

  getMenuName(_menuId : string) {
    return this.webReqService.get(`public/menu/name/${_menuId}`);
  }

  postMenu(menu : Menu) {
    return this.webReqService.post(`private/menu`, menu);
  }

  deleteMenu(_menuId : string) {
    return this.webReqService.delete(`private/menu/${_menuId}`);
  }

}
