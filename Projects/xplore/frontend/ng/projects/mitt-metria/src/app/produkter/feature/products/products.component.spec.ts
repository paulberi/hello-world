import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CollectionGQL, SearchProductsGQL } from '../../data-access/products.shop.generated';
import { BannerComponent } from '../../ui/banner/banner.component';
import { ProductsComponent } from './products.component';

describe('ProductsComponent', () => {
  let component: ProductsComponent;
  let fixture: ComponentFixture<ProductsComponent>;
  let mockSearchProductsGQL: any;
  let mockCollectionGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductsComponent, BannerComponent ], 
      imports: [
        RouterTestingModule
      ],
      providers: [
        { provide: SearchProductsGQL, useValue: mockSearchProductsGQL },
        { provide: CollectionGQL, useValue: mockCollectionGQL }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
