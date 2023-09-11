
import { AfterViewInit, Component, Input, OnInit } from "@angular/core";

// https://xd.adobe.com/view/3af559e3-5e2c-4c96-b3b3-8af6112fd7d2-6fa6/

  @Component({
    selector: "xp-vertical-tab",
    templateUrl: "./vertical-tab.component.html",
    styleUrls: ["./vertical-tab.component.scss"]
  })
  export class XpVerticalTabComponent implements OnInit, AfterViewInit {

    hidden: boolean;

    @Input() tabId: string;
    @Input() tabTitle: string;
    @Input() tabIcon: string;
    // Se till att ikonen är 24x24px
    @Input() customSvgIcon = false;

    constructor() {}

    ngOnInit() {
    }
  
    ngAfterViewInit() {
    }
  }
  