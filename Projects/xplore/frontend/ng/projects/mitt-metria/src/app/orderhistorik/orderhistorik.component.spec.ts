import { ComponentFixture, TestBed } from '@angular/core/testing';
import { XpTranslocoTestModule } from '../../../../lib/translate/translocoTest.module.translate';
import { ShopOrdersGQL } from './data-access/orderhistorik.shop.generated';
import { OrderhistorikComponent } from './orderhistorik.component';

describe("OrderhistorikComponent", () => {
  let component: OrderhistorikComponent;
  let fixture: ComponentFixture<OrderhistorikComponent>;

  let mockShopOrdersGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderhistorikComponent],
      imports: [
        XpTranslocoTestModule
      ],
      providers: [
        { provide: ShopOrdersGQL, useValue: mockShopOrdersGQL }
      ]
    })
      .compileComponents();
  },
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderhistorikComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
