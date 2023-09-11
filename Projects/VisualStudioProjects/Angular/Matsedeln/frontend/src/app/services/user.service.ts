import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { WebReqService } from './web-req.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private webReqService: WebReqService) {

  }

  getUsers() {
    return this.webReqService.get('private/user');
  }

  postUser(user : User) {
    return this.webReqService.post(`private/user`, user);
  }

  updateUser(user : User) {
    return this.webReqService.patch(`private/user/${user._id}`, user);
  }

  deleteUser(userId: string){
    return this.webReqService.delete(`private/user/${userId}`);
  }
  loginUser(user:User){
    console.log(user);
    return this.webReqService.post(`public/login`,user);
  }


}
