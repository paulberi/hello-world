import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterService } from '../../utils/router.service';
import { Chip } from '../chip-list/chip-list.component';
@Component({
  selector: 'mm-product-filter-chips',
  templateUrl: './product-filter-chips.component.html',
  styleUrls: ['./product-filter-chips.component.scss']
})
export class ProductFilterChipsComponent {
  @Input() activeFacets: Chip[];
  @Input() searchTerm: Chip;

  constructor(private routerService: RouterService, private route: ActivatedRoute) { }

  removeActiveFacet(id: string) {
    this.routerService.updateArrayParam("filter", this.toggleFacetValueId(id), this.route)
  }

  toggleFacetValueId(id: string): string[] {
    const existing = this.activeFacets.map(facet => facet.id);
    return existing.includes(id) ? existing.filter(x => x !== id) : existing.concat(id);
  }

  removeSearch() {
    this.routerService.updateParam("search", null, this.route)
  }
}
