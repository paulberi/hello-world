import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.scss']
})
export class ImagesComponent implements OnInit {

    kommunLogo: string="assets/documentation/Skellefteåkommun_färg.png";
    kommunlogo1: string="assets/documentation/Skellefteåkommun_färg 2.png"

  constructor() { }

  ngOnInit(): void {
  }

}
