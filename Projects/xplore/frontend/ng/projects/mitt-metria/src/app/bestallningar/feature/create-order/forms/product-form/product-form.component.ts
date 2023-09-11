import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Product, ProductVariant } from '../../../../../../../../../generated/graphql/shop/shop-api-types';
import { ProductsGQL, ProductVariantsByProductIdGQL } from '../../../../data-access/product-form.shop.generated';
import { ProductFormPresenter } from './product-form.presenter';

@Component({
  selector: 'mm-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ProductFormPresenter),
      multi: true
    },
    ProductFormPresenter
  ]
})
export class ProductFormComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  productId: string;
  productVariants: ProductVariant[] = [];
  valueChangesSubscription: Subscription;

  get productForm(): UntypedFormGroup {
    return this.productFormPresenter.productForm;
  }

  constructor(
    private productFormPresenter: ProductFormPresenter,
    private productsGQL: ProductsGQL,
    private productVariantsByProductIdGQL: ProductVariantsByProductIdGQL) { }

  ngOnInit(): void {
    this.productFormPresenter.initializeForm();

    this.getAllProducts();
    this.onFormChanges();
  }

  getAllProducts() {
    this.productsGQL.watch().valueChanges.subscribe((res: any) => {
      this.products = res.data?.search?.items;
    });
  }

  onFormChanges() {
    this.valueChangesSubscription = this.productFormPresenter.productIdChange.subscribe((productId: string) => {
      this.productForm.get("productVariantId").reset();
      this.productVariants = [];
      if (productId) {
        this.productId = productId;
        this.getProductVariants(this.productId);
      }
    });
  }

  getProductVariants(id: string) {
    this.productVariantsByProductIdGQL.watch({
      id
    }).valueChanges.subscribe((result: any) => {
      this.productVariants = result.data?.product?.productVariants || [];
    })
  }

  removeProductNameFromProductVariant(productVariantName: string, productName: string): string {
    if (productVariantName) {
      const replacedName = productVariantName.replace(productName, "");
      return replacedName ? replacedName : productVariantName;
    } else {
      return "Okänt utsnitt"
    }
  }

  ngOnDestroy(): void {
    if (this.valueChangesSubscription) {
      this.valueChangesSubscription.unsubscribe();
    }
  }
}
