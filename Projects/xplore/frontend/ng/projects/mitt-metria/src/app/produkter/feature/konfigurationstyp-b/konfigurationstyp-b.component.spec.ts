import { MatSnackBarModule } from '@angular/material/snack-bar';
import { OrderService } from '../../data-access/order/order.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { MMKonfigurationstypBModule } from './konfigurationstyp-b.module';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { KonfigurationstypBComponent } from './konfigurationstyp-b.component';
import { ProductVariantsByProductIdGQL } from '../../data-access/konfigurationstyp-b.shop.generated';
import { CartService } from '../../../../../../lib/vendure/cart/cart.service';

describe('KonfigurationstypBComponent', () => {
  let component: KonfigurationstypBComponent;
  let fixture: ComponentFixture<KonfigurationstypBComponent>;
  let mockProductVariantsByProductIdGQL: any;
  let mockGetJsonSchemaGQL: any;
  let mockOrderService: any;
  let mockCartService: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KonfigurationstypBComponent ],
      imports: [
        TranslocoTestingModule,
        HttpClientTestingModule,
        MMKonfigurationstypBModule,
        MatSnackBarModule
      ],
      providers: [
        { provide: ProductVariantsByProductIdGQL, useValue: mockProductVariantsByProductIdGQL },
        { provide: OrderService, useValue: mockOrderService },
        { provide: CartService, useValue: mockCartService },
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KonfigurationstypBComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
