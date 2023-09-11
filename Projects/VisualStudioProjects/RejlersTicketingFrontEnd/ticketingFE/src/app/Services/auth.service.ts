import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private email1:string;
  private password1:string;
  private user:User;

  constructor(private httpClient:HttpClient) { }

  login(email:string, password:string) {
    console.log(email, password);
    const formData=new FormData();
    formData.append('username',email);
    formData.append('password',password);
    return this.httpClient.post(`${environment.hostUrl}/rejlers/login`,formData,{withCredentials: true, observe: 'response' });
  }
}
