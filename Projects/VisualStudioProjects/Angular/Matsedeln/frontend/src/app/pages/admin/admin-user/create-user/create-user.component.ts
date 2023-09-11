import { Component, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { School } from 'src/app/models/school';
import { MunicipalityService } from 'src/app/services/municipality.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { Alert } from 'src/assets/alert';
import { AuthService } from '@auth0/auth0-angular';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})

export class CreateUserComponent implements OnInit {
  schoolsTitle : string;
  schoolsToChoose : School[];
  selectedSchools : School[];
  chosenSchools : [];
  dropdownSettings: IDropdownSettings = {};
  subscriptions : Subscription[];
  currentUser : User;

  constructor(private municipalityService : MunicipalityService, private userService : UserService, private alert : Alert, private auth: AuthService) {
    this.schoolsTitle = 'Välj skolor till användare';
    this.subscriptions = [];
  }

  ngOnInit(): void {
    this.subscriptions.push(this.auth.user$.subscribe((user) => {
      this.currentUser = new User();
      this.currentUser.setUserFromAuthPic(user.picture);
    }));

    this.subscriptions.push(this.municipalityService.getSchools().subscribe((schools : School[]) => {
      this.schoolsToChoose = schools;
    },
    (err) => this.alert.showAlert('Error','Skolor kunde inte hämtas från databasen', 'error')
    ));

    this.dropdownSettings = {
      singleSelection: false,
      textField: 'schoolName',
      selectAllText: 'Markera alla',
      unSelectAllText: 'Avmarkera alla',
      searchPlaceholderText: 'Sök',
      itemsShowLimit: 1,
      idField: '_id',
      allowSearchFilter: true
    };
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  createUser(firstName : string, lastName : string, email : string, password : string, admin : boolean, schools) {
    if(!this.currentUser.permissions.some((permission) => permission === 'admin')) {
      this.alert.showAlert('', 'Du måste ha behörighet för att administrera användare!', 'error');
    } else {
      if(lastName.length < 1) {
        this.alert.showAlert('', 'Användare måste ha ett efternamn. Testa igen!', 'error');
      } else if(email.length < 5) {
        this.alert.showAlert('', 'Användare måste ha en email på minst 5 tecken. Testa igen!', 'error');
      } else if(password.length < 5) {
        this.alert.showAlert('', 'Användare måste ha ett lösenord på minst 5 tecken. Testa igen!', 'error');
      } else {
        let newUser = new User();
        newUser.setUserFromAuthPic({'firstName' : firstName, 'lastName' : lastName, 'email' : email, 'permissions' : [], 'schoolIds' : [], 'menuIds' : []});
        let schoolIds = [];
        newUser.password = password;
        if(admin) {
          newUser.permissions.push('admin');
        } else if(schools) {
          schools.forEach(school => {
            schoolIds.push(school.id);
          });
        }
        newUser.schoolIds = schoolIds;
        let sub: Subscription = this.userService.postUser(newUser).subscribe(() => {
        },
        (err) => this.alert.showAlert('Nånting gick fel.', 'Användaren sparades inte', 'error'),
        () => this.alert.showAlertAndUpdatePage('Sparad!', 'Användaren har blivit sparad.', 'success')
        )
        this.subscriptions.push(sub);
      };
    }
  };

  clickAdmin(adminChecked: boolean) {
    if (adminChecked) {
      this.selectedSchools = this.schoolsToChoose;
    } else {
      this.selectedSchools = [];
    }
  }

}
