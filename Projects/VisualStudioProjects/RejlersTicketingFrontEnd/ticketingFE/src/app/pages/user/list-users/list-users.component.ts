import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/User';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.css']
})
export class ListUsersComponent implements OnInit {
  users:User[];
  constructor(private userService:UserService) { }

  ngOnInit(): void {

   this.userService.getUsers().subscribe((data:User[])=>{
     console.log(data);
     this.users=data;
   });

  }

}
