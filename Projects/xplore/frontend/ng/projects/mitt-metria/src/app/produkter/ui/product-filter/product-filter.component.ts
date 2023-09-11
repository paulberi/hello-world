import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { FacetValue } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { RouterService } from '../../utils/router.service';

export interface FacetWithValues {
    id: string;
    name: string;
    code: string;
    values: Array<{
        id: string;
        name: string;
        count: number;
    }>;
}

@Component({
    selector: 'mm-product-filter',
    templateUrl: './product-filter.component.html',
    styleUrls: ['./product-filter.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductFilterComponent implements OnChanges {
    @Input() activeFacetValueIds: string[] = [];
    @Input() facetValues: FacetValue[] | null;
    @Input() totalResults = 0;
    facets: FacetWithValues[];

    searchForm = new FormGroup({
        searchTerm: new FormControl('')
    });

    constructor(private routerService: RouterService, private route: ActivatedRoute) {
    }

    ngOnChanges(changes: SimpleChanges) {
        if ('facetValues' in changes) {
            this.facets = this.groupFacetValues(this.facetValues);
        }
    }

    toggleFacetValueIdInRoute(id: string) {
        this.routerService.updateArrayParam("filter", this.toggleFacetValueId(id), this.route)
    }

    toggleFacetValueId(id: string): string[] {
        const existing = this.activeFacetValueIds;
        return existing.includes(id) ? existing.filter(x => x !== id) : existing.concat(id);
    } 

    get searchFormValue() {
        return this.searchForm?.get("searchTerm")?.value || "";
    }

    doSearch(term: string) {
        this.routerService.updateParam("search", term, this.route)
        this.searchForm.get("searchTerm").setValue('', { emitEvent: false });
    }

    private groupFacetValues(facetValues: FacetValue[] | null): FacetWithValues[] {
        if (!facetValues) {
            return [];
        }
        const activeFacetValueIds = this.activeFacetValueIds;
        const facetMap = new Map<string, FacetWithValues>();
        for (const { count, facetValue: { id, name, facet } } of facetValues as any) {
            if (count === this.totalResults && !activeFacetValueIds.includes(id)) {
                // skip FacetValues that do not have any effect on the
                // result set and are not active
                continue;
            }
            const facetFromMap = facetMap.get(facet.id);
            if (facetFromMap) {
                facetFromMap.values.push({ id, name, count });
            } else {
                facetMap.set(facet.id, { id: facet.id, name: facet.name, code: facet.code, values: [{ id, name, count }] });
            }
        }
        let facets = Array.from(facetMap.values());
        facets = facets.filter(facet => facet.code !== "konfigurationstyp");
        return facets;
    }
}
