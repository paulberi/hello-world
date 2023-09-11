import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { QueryRef } from 'apollo-angular';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { LogicalOperator, SearchResultAsset } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { environment } from '../../../../environments/environment';
import { CollectionGQL, SearchProductsGQL } from '../../data-access/products.shop.generated';
import { Chip } from '../../ui/chip-list/chip-list.component';
import { Product } from '../../ui/product-list/product-list.component';
import { RouterService } from '../../utils/router.service';

const FETCH_MORE = 15;
const COLLECTION_SLUG = environment.vendureCollectionSlug;

@Component({
  selector: 'mm-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  isLoading: boolean = false;
  feedQuery: QueryRef<any>
  skip: number = 0;
  totalItems: number;
  bannerImageSource: string;
  bannerImageName: string;
  facetValues: any;
  activeFacets: Chip[] = [];
  activeFacetValueIds$: Observable<string[]>;
  activeFacetValueIds: string[];
  searchTerm$: Observable<string>;
  searchTerm: string = "";

  constructor(
    private searchProductsGQL: SearchProductsGQL,
    private notificationService: XpNotificationService,
    private breakpointObserver: BreakpointObserver,
    private route: ActivatedRoute,
    private collectionGQL: CollectionGQL,
    private routerService: RouterService,
    private router: Router) { }

  ngOnInit(): void {
    this.getRouteParams();
    this.getBanner();
    this.initFeedQuery();
    this.getFacets();
    this.getProducts();
    this.subscribeOnFacetValueIds();
    this.subscribeOnSearchTerm()
  }

  isMobile$: Observable<boolean> = this.breakpointObserver.observe('(max-width: 991px)')
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  getRouteParams() {
    this.activeFacetValueIds$ = this.routerService.getArrayParam("filter", this.route);
    this.searchTerm$ = this.routerService.getQueryParam("search", this.route)
  }

  getBanner() {
    this.collectionGQL.watch({ collectionSlug: COLLECTION_SLUG }).valueChanges.subscribe(({ data }) => {
      this.bannerImageSource = data.collection?.assets[0]?.source;
      this.bannerImageName = data.collection?.assets[0]?.name;
    })
  }

  initFeedQuery() {
    this.feedQuery = this.searchProductsGQL.watch({
      input: {
        "collectionSlug": COLLECTION_SLUG,
        "groupByProduct": true,
        "take": FETCH_MORE,
        "skip": this.skip,
        "facetValueOperator": LogicalOperator.And
      }
    });
  }

  getFacets() {
    this.searchProductsGQL.fetch({
      input: {
        "collectionSlug": COLLECTION_SLUG,
        "term": this.searchTerm || "",
        "groupByProduct": true,
        "take": FETCH_MORE,
        "skip": this.skip,
      }
    }).subscribe(({ data, loading }) => {
      this.facetValues = data.search?.facetValues || [];
      this.getActiveFacets(this.activeFacetValueIds || [])
    })
  }


  getProducts() {
    this.feedQuery.valueChanges.subscribe(({ data, loading }) => {
      this.isLoading = loading;
      this.products = data.search?.items.map(product => ({ id: product.productId, image: this.checkIfImage(product?.productAsset), name: product?.productName, description: product?.description })) || [];
      this.totalItems = +data?.search?.totalItems;
    }, error => {
      this.isLoading = false;
      this.notificationService.error("Kunde inte hämta produkter")
      console.error("Could not get products. Message:", error?.message)
    })
  }

  subscribeOnFacetValueIds() {
    this.activeFacetValueIds$.subscribe((activeFacetValueIds) => {
      this.getActiveFacets(activeFacetValueIds)
      this.activeFacetValueIds = activeFacetValueIds;
      this.feedQuery.variables.input.facetValueIds = activeFacetValueIds;
      this.feedQuery.refetch().then(({ data, loading }) => {
        this.totalItems = data.search?.totalItems || this.totalItems;
        this.skip = data.search?.items?.length || this.skip;
      });
    })
  }

  subscribeOnSearchTerm() {
    this.searchTerm$.subscribe((term) => {
      this.searchTerm = term;
      this.feedQuery.variables.input.term = term;
      this.feedQuery.refetch().then(({ data, loading }) => {
        this.totalItems = data.search?.totalItems || this.totalItems;
        this.skip = data.search?.items?.length || this.skip;
      });
    })
  }

  getActiveFacets(activeFacetValueIds: any) {
    if (this.facetValues) {
      this.activeFacets = [];
      for (let id of activeFacetValueIds) {
        const activeFacet = this.facetValues?.find(facet => facet.facetValue.id === id);
        this.activeFacets.push({ id: activeFacet.facetValue.id || "", title: activeFacet.facetValue.facet.name || "", text: activeFacet.facetValue.name || "" })
      }
    }
  }

  fetchMore() {
    this.feedQuery.fetchMore({
      variables: {
        input: {
          "collectionSlug": COLLECTION_SLUG,
          "term": "",
          "groupByProduct": true,
          "take": FETCH_MORE,
          "skip": this.skip,
          "facetValueIds": [],
          "facetValueOperator": LogicalOperator.And
        }
      }
    }).then(({ data, loading }) => {
      this.isLoading = loading;
      const currentProducts = this.products;
      const newProducts = data.search?.items.map(product => ({ id: product.productId, image: this.checkIfImage(product?.productAsset), name: product?.productName, description: product?.description })) || [];
      this.products = [...currentProducts, ...newProducts]
      this.skip = this.skip + newProducts?.length;
    }, error => {
      this.notificationService.error("Kunde inte hämta fler produkter")
      console.error("Could not fetch more products. Message:", error?.message)
    })
  }

  checkIfImage(asset: SearchResultAsset): string {
    if (!asset.preview.includes(".pdf")) {
      return asset.preview + "?preset=small";
    }
  }

  navigateToProduct(id: string) {
    this.router.navigate(["/produkter", id]);
  }
}
