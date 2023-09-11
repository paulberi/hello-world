import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDividerModule } from '@angular/material/divider';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { MMBannerModule } from '../../ui/banner/banner.module';
import { MMBreadcrumbsModule } from '../../ui/breadcrumbs/breadcrumbs.module';
import { MMKonfigurationstypBModule } from '../konfigurationstyp-b/konfigurationstyp-b.module';
import { MMProductInformationModule } from '../../ui/product-information/product-information.module';
import { ProductPageComponent } from './product-page.component';
import { ProductByIdGQL } from '../../data-access/product-page.shop.generated';

describe('ProductPageComponent', () => {
  let component: ProductPageComponent;
  let fixture: ComponentFixture<ProductPageComponent>;
  let mockProductByIdGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        ProductPageComponent
      ],
      imports: [
        RouterTestingModule,
        TranslocoTestingModule,
        MatDividerModule,
        MMBannerModule,
        MMBreadcrumbsModule,
        MMProductInformationModule,
        MMKonfigurationstypBModule,
      ],
      providers: [
        { provide: ProductByIdGQL, useValue: mockProductByIdGQL },
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductPageComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
