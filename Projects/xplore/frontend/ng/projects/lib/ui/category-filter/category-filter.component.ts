import { Component, EventEmitter, Input, Output, ViewEncapsulation } from '@angular/core';

export interface Item {
  id: string;
  name: string;
  values: Array<{
    id: string;
    name: string;
  }>;
}

@Component({
  selector: 'xp-category-filter',
  templateUrl: './category-filter.component.html',
  styleUrls: ['./category-filter.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class XpCategoryFilterComponent {
  @Input() items: Item[];
  @Input() activeItemIds: string[];
  @Input() titleCaseOnCategory = true;
  @Output() filterUpdated = new EventEmitter<string>();

  constructor() { }

  isActive(id: string): boolean {
    return this.activeItemIds.includes(id);
  }
}
