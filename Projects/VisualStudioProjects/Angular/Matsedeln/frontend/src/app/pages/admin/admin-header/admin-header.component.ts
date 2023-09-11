import { Subscription } from 'rxjs';
import { Location } from '@angular/common';
import { Router} from '@angular/router';
import { AuthService } from '@auth0/auth0-angular';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  constructor(private router: Router, private location: Location) {

   }

  ngOnInit(): void {
  }



}
