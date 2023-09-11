import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { GetAreaPriceForVariantGeojsonGQL, ProductVariantsByProductIdGQL } from '../../data-access/konfigurationstyp-a.shop.generated';
import { OrderService } from '../../data-access/order/order.service';
import { KonfigurationstypAComponent } from './konfigurationstyp-a.component';
import { MMKonfigurationstypAModule } from './konfigurationstyp-a.module';

describe('KonfigurationstypAComponent', () => {
  let component: KonfigurationstypAComponent;
  let fixture: ComponentFixture<KonfigurationstypAComponent>;

  let mockProductVariantsByProductIdGQL: any;
  let mockGetAreaPriceForVariantGQL: any;
  let mockOrderService: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KonfigurationstypAComponent],
      imports: [MMKonfigurationstypAModule, HttpClientTestingModule, MatSnackBarModule],
      providers: [
        { provide: ProductVariantsByProductIdGQL, useValue: mockProductVariantsByProductIdGQL },
        { provide: GetAreaPriceForVariantGeojsonGQL, useValue: mockGetAreaPriceForVariantGQL },
        { provide: OrderService, useValue: mockOrderService },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(KonfigurationstypAComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
