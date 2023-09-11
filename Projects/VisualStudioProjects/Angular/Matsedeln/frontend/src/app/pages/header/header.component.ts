import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Menu } from 'src/app/models/menu';
import { Municipality } from 'src/app/models/municipality';
import { School } from 'src/app/models/school';
import { Week } from 'src/app/models/week';
import { DateHandlerService } from 'src/app/services/date-handler.service';
import { MenuService } from 'src/app/services/menu.service';
import { MunicipalityService } from 'src/app/services/municipality.service';
import { SharingService } from 'src/app/services/sharing.service';
import { Subscription, Observable } from 'rxjs';
import { Alert } from 'src/assets/alert';
import { User } from 'src/app/models/user';
import { AuthService } from '@auth0/auth0-angular';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit {
  $municipalities: Observable<any>;
  chosenMunicipality: Municipality;
  chosenSchool: School;
  weeks : Week[];
  weekTitle : string;
  chosenWeek : Week;
  previousWeek : Week;
  nextWeek : Week;
  previousWeekTitle : string;
  nextWeekTitle : string;
  currentWeek : string;
  subscriptions : Subscription[] = [];

  constructor(private municipalityService: MunicipalityService, private router: Router,
    private dateHandlerService : DateHandlerService, private menuService: MenuService, private sharingService : SharingService, private alert : Alert, private auth: AuthService) {
    this.weekTitle = "Vecka"
    this.currentWeek = this.dateHandlerService.getCurrentWeek();
  }

  ngOnInit(): void {
    this.auth.isAuthenticated$.subscribe((loggedIn) => {
      if(loggedIn) {
        this.subscriptions.push(this.auth.user$.subscribe((user) => {
          let currentUser = new User();
          currentUser.setUserFromAuthPic(user.picture);
          if(!currentUser.permissions.some((perm) => perm === 'admin')){
            this.$municipalities = this.filterMunicipalitiesFromUserPerm(currentUser);
          } else {
            this.$municipalities = this.municipalityService.getMunicipalities();
          }
        }))
      } else {
        this.$municipalities = this.municipalityService.getMunicipalities();
      }
    })
  }

  filterMunicipalitiesFromUserPerm(currentUser : User) : Observable<object> {
     return this.municipalityService.getMunicipalities().pipe(map((municipalities : Municipality[]) => {
      let mun = municipalities.filter((mun) => {
        return mun.schools.some((school) => {
          return currentUser.schoolIds.some((schoolId) => schoolId === school._id)
        })
      })
     mun.forEach((municipality) => {
        municipality.schools = municipality.schools.filter((school) => {
          return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
        })
      })
      return mun;
    }))
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => {
      sub.unsubscribe();
    })
  }

  chooseMunicipality() {
    this.sharingService.setWeek(null);
    this.weeks = [];
    this.nextWeek = null;
    this.previousWeek = null;
    this.previousWeekTitle = '';
    this.nextWeekTitle = '';
    this.weekTitle = "Vecka";
  }

  chooseSchool() {
    this.sharingService.setWeek(null);
    this.weeks = [];
    this.nextWeek = null;
    this.previousWeek = null;
    this.previousWeekTitle = '';
    this.nextWeekTitle = '';
    let school : School = this.chosenSchool;
    if(school._menuId === '' || school._menuId === undefined) {
      this.alert.showAlert('', 'Vald skola har ingen matsedel!', 'error');
    } else {
      let menu : Menu;

      let sub: Subscription = this.menuService.getMenu(school._menuId).subscribe((menuu: Menu) => {
        menu = menuu;
      },
      (err) => {

      },
      () => {
        this.sharingService.setMenu(menu);
        this.weeks = this.dateHandlerService.getWeeks(menu);
        this.weeks.forEach(week => {
          if(week.weekNr === this.currentWeek) {
            this.chosenWeek = week;
            this.chooseWeek(this.chosenWeek);
          }
        });
      });
      this.subscriptions.push(sub);
    }
  }

  setWeek(){
    this.chooseWeek(this.chosenWeek);
  }

  chooseWeek(week : Week) {
    this.chosenWeek = week;
    this.weekTitle = "Vecka " + this.chosenWeek.weekNr;
    this.previousWeek = this.dateHandlerService.getPreviousWeek(this.weeks, week);
    if(this.previousWeek){
      this.previousWeekTitle = "V." + this.previousWeek.weekNr;
    } else {
      this.previousWeekTitle = "";
    }

    this.nextWeek = this.dateHandlerService.getNextWeek(this.weeks, week);

    if(this.nextWeek){
      this.nextWeekTitle = "V." + this.nextWeek.weekNr;
    } else {
      this.nextWeekTitle = "";
    }

    this.sharingService.setWeek(this.chosenWeek);
  }

  previousWeekClick(): void {
    if(this.previousWeek) {
      this.chooseWeek(this.previousWeek);
    }

  }

  nextWeekClick(): void {
    if(this.nextWeek){
      this.chooseWeek(this.nextWeek);
    }
  }

}
