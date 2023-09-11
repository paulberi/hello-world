import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable, Subscription } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { Alert } from 'src/assets/alert';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.scss']
})
export class DeleteUserComponent implements OnInit {

  NameOfUserToDelete: string;
  $users : Observable<any>;
  userToDeleteId: string = '';
  sub:Subscription[];
  currentUser : User;


  constructor( private userService: UserService, private alert: Alert, private auth: AuthService) {
    this.sub=[];

  }

  ngOnInit(): void {
    this.NameOfUserToDelete= "Välja användare att ta bort";
    this.$users= this.userService.getUsers();

    this.sub.push(this.auth.user$.subscribe((user) => {
      this.currentUser = new User();
      this.currentUser.setUserFromAuthPic(user.picture);
    }));
  }

  ngOnDestroy(): void {
    this.sub.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  UserToDelete(user: User) {
      this.NameOfUserToDelete=user.email;
      this.userToDeleteId=user._id;
  }

  deleteUser() {
    if(!this.currentUser.permissions.some((permission) => permission === 'admin')) {
      this.alert.showAlert('', 'Du måste ha behörighet för att administrera användare!', 'error');
    } else {
      if(this.userToDeleteId===''){
        this.alert.showAlert('', 'Du måste välja en användare att ta bort!', 'error');
      }else{
        this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna användare?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
          if (result.isConfirmed) {
            this.sub.push(this.userService.deleteUser(this.userToDeleteId).subscribe(()=>{
            },
            (err) => this.alert.showAlert('Nånting gick fel.', 'Användaren sparades inte', 'error'),
            () => this.alert.showAlertAndUpdatePage('Borttagen!', 'Användare har blivit borttagen.', 'success')
            ));
          }
        });
      }
    }
  }

}
