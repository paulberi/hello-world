import { MenuService } from './../../../../services/menu.service';
import { Observable, Subscription } from 'rxjs';
import { Menu } from './../../../../models/menu';
import { Component, OnInit, Input } from '@angular/core';
import { Alert } from 'src/assets/alert';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-update-menu',
  templateUrl: './update-menu.component.html',
  styleUrls: ['./update-menu.component.scss']
})

export class UpdateMenuComponent implements OnInit {
  @Input() $menus : Observable<any>;
  editMenuTitle : string;
  menuToEdit : Menu = new Menu();
  menuNameToEdit : string;
  chosenMenuStartDate : Date;
  chosenMenuEndDate : Date;
  subscriptions : Subscription[];

  constructor(private menuService: MenuService, private alert : Alert) {
    this.editMenuTitle = "Välj matsedel att redigera: "
    this.menuNameToEdit = "";
    this.subscriptions = [];
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  editMenu(menu : Menu) {
    this.editMenuTitle = menu.menuName;
    this.menuToEdit = menu;
    this.menuNameToEdit = menu.menuName;
    this.chosenMenuStartDate = menu.startDate;
    this.chosenMenuEndDate = menu.endDate;
  }

  saveEditedMenu(menuName : string, startDate : Date, endDate : Date) {
    let datePipe: DatePipe = new DatePipe('en-US');
    if (menuName === '') {
      this.alert.showAlert('', 'Du måste välja en matsedel att uppdatera.', 'error');
    } else if (menuName.length < 1) {
      this.alert.showAlert('', 'Input för menynamnet är för kort. Testa igen!', 'error');
    } else if (menuName.length > 40) {
      this.alert.showAlert('', 'Input för menynamnet är för långt. Testa igen!', 'error');
    } else {
      this.menuToEdit.menuName = menuName;
      this.menuToEdit.startDate = startDate;
      this.menuToEdit.endDate = endDate;
      let sub: Subscription = this.menuService.updateMenu(this.menuToEdit).subscribe(() => {
      },
      (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'),
      () =>  this.alert.showAlertAndUpdatePage('Uppdaterad!', 'Matsedeln har blivit uppdaterad.', 'success')
      );
      this.subscriptions.push(sub);

    }
  }


}
