import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/Order';
import { User } from 'src/app/models/User';
import { OrderService } from 'src/app/Services/order.service';
import { UserService } from 'src/app/Services/user.service';

declare var $: any;

@Component({
  selector: 'app-adm',
  templateUrl: './adm.component.html',
  styleUrls: ['./adm.component.css']
})
export class AdmComponent implements OnInit {

  orders:Order[];
  users:User[];

  constructor(private orderService:OrderService, private userService:UserService) { }

  ngOnInit(): void {
    this.orderService.getOrder1().subscribe((data: Order[])=>{
      console.log(data);
      this.orders=data;
    });
    this.userService.getUser1().subscribe((data:User[])=>{
      console.log(data);
      this.users=data;
    });

   }

}
