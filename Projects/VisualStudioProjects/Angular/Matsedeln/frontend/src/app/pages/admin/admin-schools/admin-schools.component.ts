import { User } from 'src/app/models/user';
import { AuthService } from '@auth0/auth0-angular';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Menu } from 'src/app/models/menu';
import { Municipality } from 'src/app/models/municipality';
import { School } from 'src/app/models/school';
import { MenuService } from 'src/app/services/menu.service';
import { MunicipalityService } from 'src/app/services/municipality.service';

@Component({
  selector: 'app-admin-schools',
  templateUrl: './admin-schools.component.html',
  styleUrls: ['./admin-schools.component.scss']
})
export class AdminSchoolsComponent implements OnInit {
  menus : Menu[];
  municipalities : Municipality[];
  subscriptions : Subscription[] = [];



  constructor(private municipalityService : MunicipalityService, private menuService: MenuService, private auth: AuthService) {}

  ngOnInit(): void {
    this.subscriptions.push(this.municipalityService.getMunicipalities().subscribe((municipalities : Municipality[]) => {
      this.subscriptions.push(this.auth.user$.subscribe((user) => {
        let currentUser = new User();
        currentUser.setUserFromAuthPic(user.picture);
        if(!currentUser.permissions.some((perm) => perm === 'admin')){
          //Filter out municipalities that user have access to change
          municipalities = municipalities.filter((mun) => {
            return mun.schools.some((school) => {
              return currentUser.schoolIds.some((schoolId) => schoolId === school._id)
            });
          });
          //filter out schools for each municipality that user have access to change
          municipalities.forEach((municipality) => {
            municipality.schools = municipality.schools.filter((school) => {
              return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
            });
          });
        }

        this.municipalities = municipalities;
      }));
      this.municipalities = municipalities;
    }));
    this.subscriptions.push(this.menuService.getMenus().subscribe((menus : Menu[]) => {
      this.menus = menus;
    }))
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  filterMunicipalities(){

  }


}
