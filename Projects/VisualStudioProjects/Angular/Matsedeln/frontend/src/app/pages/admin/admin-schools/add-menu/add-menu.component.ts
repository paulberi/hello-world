import { Subscription } from 'rxjs';
import { MenuService } from './../../../../services/menu.service';
import { MunicipalityService } from './../../../../services/municipality.service';
import { School } from './../../../../models/school';
import { Menu } from './../../../../models/menu';
import { Municipality } from './../../../../models/municipality';
import { Component, OnInit, Input } from '@angular/core';
import { Alert } from 'src/assets/alert';

@Component({
  selector: 'app-add-menu',
  templateUrl: './add-menu.component.html',
  styleUrls: ['./add-menu.component.scss']
})
export class AddMenuComponent implements OnInit {
  @Input() municipalities : Municipality[];
  @Input() menus : Menu[];
  municipalityToAdd : Municipality = new Municipality();
  schoolToAdd : School = new School();
  menuToAdd : Menu = new Menu();
  chosenMunicipalityTitleToAdd : string;
  chosenSchoolTitleToAdd : string;
  chosenMenuTitle : string;
  subscriptions: Subscription[];


  constructor(private municipalityService: MunicipalityService, private alert : Alert) {
    this.chosenMunicipalityTitleToAdd = "Välj kommun: ";
    this.chosenSchoolTitleToAdd = "Välj skola: ";
    this.chosenMenuTitle = "Välj matsedel: ";
    this.subscriptions = [];
  }

  ngOnInit(): void {

  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  chooseMunicipalityToAdd(municipality : Municipality) {
    this.municipalityToAdd = municipality;
    this.chosenMunicipalityTitleToAdd = municipality.municipalityName;
  }

  chooseSchoolToAdd(school : School) {
    this.schoolToAdd = school;
    this.chosenSchoolTitleToAdd = school.schoolName;
  }

  chooseMenuToAdd(menu : Menu) {
    this.menuToAdd = menu;
    this.chosenMenuTitle = menu.menuName;
  }

  addMenuToSchool(menu : Menu, school : School) {
    if(this.chosenSchoolTitleToAdd === "Välj skola: " || this.chosenMenuTitle === "Välj matsedel: ") {
      this.alert.showAlert('', 'Du måste välja både en skola och en matsedel att lägga till!', 'error');
    } else {
      school._menuId = menu._id;
      let sub: Subscription = this.municipalityService.updateSchool(this.municipalityToAdd._id, school).subscribe(() => {
      },
      (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'),
      () => this.alert.showAlertAndUpdatePage('Tillagd!', 'Matsedeln har blivit sparad i vald skola.', 'success')
      );
      this.subscriptions.push(sub);

    }
  }

}
