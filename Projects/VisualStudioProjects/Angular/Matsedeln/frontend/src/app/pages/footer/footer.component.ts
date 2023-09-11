import { Component, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Subscription } from 'rxjs';
import { User } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})

export class FooterComponent implements OnInit {

  admin : boolean = false;
  loggedIn : boolean = false;
  sub : Subscription;
  readonly ROOT_URL;
  loggedInUser : string;

  constructor(public auth: AuthService) {
    this.ROOT_URL = environment.ROOT_URL;
  }

  ngOnInit(): void {

    if(localStorage.getItem('admin')=="admin"){
     this.admin=true;
    }
    if(localStorage.getItem('currentUser')){
      console.log(localStorage.getItem('currentUser'));
      this.loggedIn=true;
    }

    // this.sub = this.auth.user$.subscribe((user) => {
    //   let currentUser = new User();
    //   currentUser.setUserFromAuthPic(user.picture);
    //   this.admin = currentUser.permissions.some((perm)=> perm === 'admin');
    //   this.loggedInUser = currentUser.firstName + ' ' + currentUser.lastName;
    // })
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
