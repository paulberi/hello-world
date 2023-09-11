import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  email:string;
  password:string;

  constructor(private httpClient: HttpClient) {
   }

  getUsers():Observable<User[]>{
    this.email=localStorage.getItem("userName");
    this.password=localStorage.getItem("password");
    console.log(this.email +" "+this.password);

    const headerDict = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Headers': 'Content-Type',
    }
    const headers={
      headers:new HttpHeaders(headerDict)
    };
   // return this.httpClient.get<User[]>(environment.hostUrl+"/rejlers/allUsers",httpOptions);
   return this.httpClient.get<User[]>(`${environment.hostUrl}/rejlers/allUsers`,headers);
  }
  getUser1(){
    this.email=localStorage.getItem("userName");
    this.password=localStorage.getItem("password");
    const headers=new HttpHeaders({Authorization: 'Basic '+btoa(this.email+":"+this.password)});
    return this.httpClient.get(environment.hostUrl+'/rejlers/allUsers',{headers})
  }

}
