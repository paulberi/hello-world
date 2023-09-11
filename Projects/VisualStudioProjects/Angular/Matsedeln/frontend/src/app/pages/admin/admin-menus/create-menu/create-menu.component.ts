import { Subscription } from 'rxjs';
import { Menu } from './../../../../models/menu';
import { MenuService } from './../../../../services/menu.service';
import { Component, OnInit } from '@angular/core';
import  Swal  from 'sweetalert2';
import { Alert } from 'src/assets/alert';
import { DatePipe } from '@angular/common';
import { AuthService } from '@auth0/auth0-angular';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-create-menu',
  templateUrl: './create-menu.component.html',
  styleUrls: ['./create-menu.component.scss']
})
export class CreateMenuComponent implements OnInit {
  currentDate = new Date();
  subscriptions : Subscription[];
  constructor(private menuService : MenuService, private alert : Alert, private auth: AuthService, private userService : UserService) {
    this.subscriptions = [];
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => {
      sub.unsubscribe();
    })
  }

  createMenu(menuName : string, startDate : Date, endDate : Date) {
    let datePipe: DatePipe = new DatePipe('en-US');
    if(menuName.length < 1) {
      this.alert.showAlert('', 'Input för menynamnet är för kort. Testa igen!', 'error');
    } else if (menuName.length > 40) {
      this.alert.showAlert('', 'Input för menynamnet är för långt. Testa igen!', 'error');
    } else {
      let menu : Menu = new Menu();
      menu.menuName = menuName;
      menu.startDate = startDate;
      menu.endDate = endDate;
      this.subscriptions.push(this.menuService.postMenu(menu).subscribe((createdMenu : Menu) => {
        this.subscriptions.push(this.auth.user$.subscribe((user) => {
          let currentUser = new User();
          currentUser.setUserFromAuthPic(user.picture);
          currentUser.menuIds.push(createdMenu._id);
          this.subscriptions.push(this.userService.updateUser(currentUser).subscribe(() => {
          },
          (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error')
          ));
        }));
      },
      (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas', 'error'),
      () => this.alert.showAlertAndUpdatePage('Sparad!', 'Matsedeln har blivit sparad.', 'success')
      ));
    }
  }
}
