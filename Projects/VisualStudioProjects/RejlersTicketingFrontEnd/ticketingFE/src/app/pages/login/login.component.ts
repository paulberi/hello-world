import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/Services/auth.service';
import {Router} from "@angular/router"
import { User } from 'src/app/models/User';
import { stringify } from 'querystring';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email:string;
  password:string;
  errorMessage='Invalid Credentials';
  successMessage:string;
  invalidLogin=false;
  loginSuccess=false;
  user:User[];

  constructor(private authService: AuthService, private router:Router) { }

  ngOnInit(): void {
  }
  login(){
    console.log(this.email+" an pw "+this.password);
    let user:User=new User;
    user.email=this.email;
    user.password=this.password;
    localStorage.clear();
    let response=this.authService.login(this.email, this.password);
    response.subscribe((result)=>{
      console.log(result)
     let user=[result];
      this.invalidLogin=false;
      this.loginSuccess=true;
      this.successMessage='Login Successful';
      localStorage.setItem("userName", this.email);
      localStorage.setItem("password", this.password);
      console.log(localStorage.getItem("userName")+" and "+ localStorage.getItem("password"));
      this.router.navigate(['rejlers/admin']);

    }),()=>{
      this.invalidLogin=true;
      this.loginSuccess=false;
    }
  }


}
