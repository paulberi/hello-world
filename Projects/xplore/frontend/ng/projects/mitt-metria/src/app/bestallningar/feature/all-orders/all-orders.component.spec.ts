import { ComponentFixture, TestBed } from '@angular/core/testing';
import { XpTranslocoTestModule } from '../../../../../../lib/translate/translocoTest.module.translate';
import { AdminOrdersGQL, GetDeliveryOrderGQL, GetDeliveryOrderLineGQL } from '../../data-access/all-orders.admin.generated';
import { AllOrdersComponent } from './all-orders.component';
import { MMAllOrdersModule } from './all-orders.module';

describe("AllOrdersComponent", () => {
  let component: AllOrdersComponent;
  let fixture: ComponentFixture<AllOrdersComponent>;

  let mockAdminOrdersGQL: any;
  let mockGetDeliveryOrderGQL: any;
  let mockGetDeliveryOrderLineGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AllOrdersComponent],
      imports: [
        MMAllOrdersModule,
        XpTranslocoTestModule
      ],
      providers: [
        { provide: AdminOrdersGQL, useValue: mockAdminOrdersGQL },
        { provide: GetDeliveryOrderGQL, useValue: mockGetDeliveryOrderGQL },
        { provide: GetDeliveryOrderLineGQL, useValue: mockGetDeliveryOrderLineGQL },
      ]
    })
      .compileComponents();
  },
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(AllOrdersComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
