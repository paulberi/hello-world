import { Component, OnInit } from '@angular/core';
import { BoundDirectivePropertyAst } from '@angular/compiler';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  sub:Subscription[];
  userList:User[];
  currentUser:User;


  constructor(private userService:UserService, private router: Router) { }

  ngOnInit(): void {
    this.userList=[];
    this.sub=[];
    this.userService.getUsers().subscribe((users:User[]) => {
      this.userList = users;
      console.log(this.userList);
    })
  }

  login(email:any, password:any){

    localStorage.clear();
    console.log(email, password,this.userList);
    let user=new User();
    user.email=email;
    user.password=password;
    console.log(user);
    this.userService.loginUser(user).subscribe((data:User)=>{
      console.log(data);
      localStorage.setItem('currentUser',email);
      this.currentUser=data;
      this.currentUser.permissions.forEach(item=>{
        if(item.toString()=="admin"){
          localStorage.setItem('admin','admin');
        }
      })

      if(email==this.currentUser.email){
        console.log(email+" and "+this.currentUser.email)
        this.router.navigate(['/']);
      }
    })
  }

}

