
import { AfterContentInit, AfterViewInit, Component, ContentChildren, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren } from "@angular/core";
import { XpVerticalTabComponent } from "./vertical-tab.component";

  @Component({
    selector: "xp-vertical-tabs",
    templateUrl: "./vertical-tabs.component.html",
    styleUrls: ["./vertical-tabs.component.scss"]
  })
  export class XpVerticalTabsComponent implements OnInit, AfterContentInit {
    @Output() activeTabChange = new EventEmitter<string>();

    constructor() {}

    private _activeTab: string;
    defaultContent: XpVerticalTabComponent;

    get activeTab(): string {
      return this._activeTab;
    }
    @Input() set activeTab(value: string) {
      if (this._activeTab === value || value == undefined) {
        return;
      } 

      this._activeTab = value;
      this.showActiveTabContent();
      this.activeTabChange.emit(this._activeTab);
  }
    @ContentChildren(XpVerticalTabComponent) private tabChildren: QueryList<XpVerticalTabComponent>;
    tabs: XpVerticalTabComponent[] = [];

    ngOnInit() {
    }
  
    ngAfterContentInit(): void {      
      this.tabChildren.forEach(tabElement => {
        this.addTab(tabElement);
      });
      this.showActiveTabContent();
    }

    private addTab(tab: XpVerticalTabComponent) {
      tab.hidden = true;
      if(!tab.tabId) {
        this.defaultContent = tab;
      } else {
        this.tabs.push(tab);
      }
    }

    private showActiveTabContent() {
      this.tabs.forEach(tab => {
        tab.hidden = !(tab.tabId === this._activeTab);
      });

      if(this.defaultContent) {
        this.defaultContent.hidden = !!this._activeTab;
      }
    } 
  }
