import { Component, OnInit } from '@angular/core';
import { XpUserService } from '../../../../../lib/user/user.service';


@Component({
  selector: 'sr-mina-sidor',
  templateUrl: './mina-sidor.component.html',
  styleUrls: ['./mina-sidor.component.scss']
})
export class MinaSidorComponent implements OnInit {

  fornamn: string
  personnummer: string

  constructor(private xpUserService:XpUserService){
  }

  ngOnInit(){
    this.personnummer = this.xpUserService.getUser()?.claims.preferred_username
    this.fornamn = this.xpUserService.getUser()?.claims.given_name
  }
}
