import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../models/Order';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  email:string;
  password:string;

  constructor(private httpClient:HttpClient) { }

  getOrders():Observable<Order[]>{
    this.email=localStorage.getItem("userName");
    this.password=localStorage.getItem("password");
    console.log(this.email +" "+this.password);
    const headerDict = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
    const headers={
      headers:new HttpHeaders(headerDict).append('Authorization', 'Basic'+window.btoa(this.email+":"+this.password))
    };
    //return this.httpClient.get<Order[]>((environment.hostUrl+"/rejlers/allorders"),httpOptions);
    return this.httpClient.get<Order[]>(`${environment.hostUrl}/rejlers/allorders`,headers);
  }
  getOrder1(){
    this.email=localStorage.getItem("userName");
    this.password=localStorage.getItem("password");
    const headers=new HttpHeaders({Authorization: 'Basic '+btoa(this.email+":"+this.password)});
    return this.httpClient.get(environment.hostUrl+'/rejlers/allorders',{headers})
  }

}
