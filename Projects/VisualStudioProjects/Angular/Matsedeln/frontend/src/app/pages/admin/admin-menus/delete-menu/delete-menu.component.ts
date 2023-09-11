import { Menu } from './../../../../models/menu';
import { Observable, Subscription } from 'rxjs';
import { MenuService } from './../../../../services/menu.service';
import { Component, OnInit, Input } from '@angular/core';
import { Alert } from 'src/assets/alert';
import { MunicipalityService } from 'src/app/services/municipality.service';
import { School } from 'src/app/models/school';
import { Municipality } from 'src/app/models/municipality';

@Component({
  selector: 'app-delete-menu',
  templateUrl: './delete-menu.component.html',
  styleUrls: ['./delete-menu.component.scss']
})
export class DeleteMenuComponent implements OnInit {
  @Input() $menus : Observable<any>;
  deleteMenuTitle : string;
  menuToDeleteId : string = '';
  subscriptions : Subscription[];
  schools : School[];
  municipalities : Municipality[];

  constructor(private menuService : MenuService, private alert : Alert, private municipalityService : MunicipalityService) {
    this.subscriptions = [];
  }

  ngOnInit(): void {
    this.deleteMenuTitle = "Välj matsedel att ta bort: ";

    this.subscriptions.push(this.municipalityService.getSchools().subscribe((schools : School[]) => {
      this.schools = schools;
    },
    (err) => this.alert.showAlert('Error','Skolor kunde inte hämtas från databasen', 'error')
    ));

    this.subscriptions.push(this.municipalityService.getMunicipalities().subscribe((municipalities : Municipality[]) => {
      this.municipalities = municipalities;
    },
    (err) => this.alert.showAlert('Error','Kommuner kunde inte hämtas från databasen', 'error')
    ));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  updateDeleteMenuTitle(menu : Menu) {
    this.deleteMenuTitle = menu.menuName;
    this.menuToDeleteId = menu._id;
  }

  deleteMenu() {
    if(this.menuToDeleteId === '') {
      this.alert.showAlert('', 'Du måste välja en matsedel att ta bort.', 'error');
    } else {
      this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna matsedel?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
        if (result.isConfirmed) {
          this.schools.forEach(school => {
            if(school._menuId === this.menuToDeleteId) {
              school._menuId = '';
              let municipalityToUpdate = this.municipalities
              .filter((municipality) => municipality.schools
              .some((schoolInMunicipality) => schoolInMunicipality._id === school._id))[0];

              this.subscriptions.push(this.municipalityService.updateSchool(municipalityToUpdate._id, school).subscribe(() => {
              }));
            }
          });

          this.subscriptions.push(this.menuService.deleteMenu(this.menuToDeleteId).subscribe(() => {
          },
          (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'),
          () => this.alert.showAlertAndUpdatePage('Borttagen!', 'Matsedeln har blivit borttagen.', 'success')
          ));
        }
      });
    }
  }
}
