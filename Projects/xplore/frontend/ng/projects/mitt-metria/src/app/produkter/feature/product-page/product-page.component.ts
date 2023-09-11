import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { Breadcrumb } from '../../ui/breadcrumbs/breadcrumbs.component';
import { Asset, AssetType } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { ProductByIdGQL } from '../../data-access/product-page.shop.generated';

@Component({
  selector: 'mm-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.scss']
})
export class ProductPageComponent implements OnInit {
  productId: string;
  productBannerImage: string;
  productBannerName: string;
  breadcrumbs: Breadcrumb[] = [];
  productName: string;
  productDescription: string;
  productPreamble: string;
  productImages: Asset[] = [];
  productDocuments: Asset[] = [];
  orderStarted: boolean = false;
  configurationType: "a" | "b" | "c" | undefined = undefined;
  orderAttributes: string;
  orderLabel: string;
  productVariantId: string;

  constructor(
    private route: ActivatedRoute,
    private productByIdGQL: ProductByIdGQL,
    private notificationService: XpNotificationService,
  ) { }

  ngOnInit(): void {
    //För att komma längst upp på sidan när man kommer in

    document.body.scrollTop = 0;
    window.scrollTo(0, 0);

    const id = this.route.snapshot.params["id"];
    if (id) {
      this.productId = id;
    }

    this.getProductById();
  }

  getProductById() {
    this.productByIdGQL.watch({ productId: this.productId }).valueChanges.subscribe(({ data }) => {
      this.productName = data.product?.name;
      this.productPreamble = data.product?.description;
      this.productDescription = data.product?.customFields?.fullDescription;

      this.setBannerImage(data.product?.featuredAsset as Asset);
      this.setBreadcrumbs(this.productName, this.productId);

      const assets = data.product?.assets;
      const filterImages = assets.filter(asset => asset.type === AssetType.Image);
      this.productImages = this.lowerImageQuality(filterImages);
      this.productDocuments = assets.filter(asset => asset.mimeType === "application/pdf") as Asset[];

      this.configurationType = this.determineConfigurationType(data.product.facetValues || [])
    })
  }

  setBannerImage(asset: Asset) {
    if (asset.type === AssetType.Image) {
      this.productBannerImage = asset.source;
      this.productBannerName = asset.name;
    }
  }

  lowerImageQuality(images): Asset[] {
    return images.map(image => { return { ...image, source: image.source + "?preset=medium" } });
  }

  determineConfigurationType(facetValues: any) {
    if (facetValues.length) {
      for (const facetValue of facetValues as any) {
        if (facetValue?.facet?.code === "konfigurationstyp") {
          return facetValue?.name;
        }
      }
      return undefined;
    } else {
      return undefined;
    }
  }

  setBreadcrumbs(productName: string, productId: string) {
    this.breadcrumbs = [
      {
        name: "Produkter",
        path: "/produkter"
      },
      {
        name: productName,
        path: `/produkter/${productId}`
      }
    ]
  }

  startOrder() {
    if (this.configurationType) {
      this.orderStarted = true;
    } else {
      this.notificationService.error("Produkten går tyvärr inte att beställa just nu, prova igen om en stund eller kontakta support.");
      console.error(`${this.productName} is missing configuration type`)
    }
  }

  cancelOrder() {
    this.orderStarted = false;
  }

  downloadDocument(source: string) {
    if (source) {
      window.open(source);
    } else {
      this.notificationService.error("Kunde inte hämta dokument");
    }
  }

}
