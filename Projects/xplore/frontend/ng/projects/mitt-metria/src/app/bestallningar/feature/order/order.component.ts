import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface TabLink {
  label: string;
  link: string;
  index: number;
}

@Component({
  selector: 'mm-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  tabLinks: TabLink[] = [];
  activeLinkIndex = 0;

  constructor(private router: Router) {
    this.tabLinks = [
      {
        label: "Lägg beställning",
        link: "/bestallningar/lagg-bestallning",
        index: 0
      },
      {
        label: "Alla beställningar",
        link: "/bestallningar/alla-bestallningar",
        index: 1
      },
    ];
  }

  ngOnInit(): void {
    this.router.events.subscribe(() => {
      this.activeLinkIndex = this.tabLinks.indexOf(this.tabLinks.find(tab => tab.link === '.' + this.router.url));
    });
  }

}
