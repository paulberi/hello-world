import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/Order';
import { User } from 'src/app/models/User';
import { OrderService } from 'src/app/Services/order.service';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-listorders',
  templateUrl: './listorders.component.html',
  styleUrls: ['./listorders.component.css']
})
export class ListordersComponent implements OnInit {

  orders:Order[];
  users:User[];
  constructor(private orderService: OrderService, private userService:UserService) { }

  ngOnInit(): void {

    this.orderService.getOrders().subscribe((data: Order[])=>{
      console.log(data);
      this.orders=data;
    });

    this.userService.getUsers().subscribe((data:User[])=>{
      console.log(data);
      this.users=data;
    });

  }

}
