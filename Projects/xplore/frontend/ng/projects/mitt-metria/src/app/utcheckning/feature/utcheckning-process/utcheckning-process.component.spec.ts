import { ActiveCustomerGQL, AddPaymentGQL, GetActiveOrderStateGQL, GetActiveOrderTotalPriceGQL, GetGembetPaymentStatusGQL, GetPaymentMethodsGQL, InitiateGembetPaymentGQL, TransitionToAddingItemsGQL, UpdateCustomerInvoicingLabelGQL } from './../../data-access/utcheckning-process.shop.generated';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UtcheckningProcessComponent } from './utcheckning-process.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('UtcheckningProcessComponent', () => {
  let component: UtcheckningProcessComponent;
  let fixture: ComponentFixture<UtcheckningProcessComponent>;

  let mockGetPaymentMethodsGQL: any;
  let mockAddPaymentGQL: any;
  let mockActiveCustomerGQL: any;
  let mockGetActiveOrderTotalPriceGQL: any;
  let mockGetGembetPaymentStatusGQL: any;
  let mockInitiateGembetPaymentGQL: any;
  let mockTransitionToAddingItemsGQL: any;
  let mockUpdateCustomerInvoicingLabelGQL: any;
  let mockgetActiveOrderStateGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UtcheckningProcessComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: GetPaymentMethodsGQL, useValue: mockGetPaymentMethodsGQL },
        { provide: AddPaymentGQL, useValue: mockAddPaymentGQL },
        { provide: ActiveCustomerGQL, useValue: mockActiveCustomerGQL },
        { provide: GetActiveOrderTotalPriceGQL, useValue: mockGetActiveOrderTotalPriceGQL },
        { provide: GetGembetPaymentStatusGQL, useValue: mockGetGembetPaymentStatusGQL },
        { provide: InitiateGembetPaymentGQL, useValue: mockInitiateGembetPaymentGQL },
        { provide: TransitionToAddingItemsGQL, useValue: mockTransitionToAddingItemsGQL },
        { provide: UpdateCustomerInvoicingLabelGQL, useValue: mockUpdateCustomerInvoicingLabelGQL },
        { provide: GetActiveOrderStateGQL, useValue: mockgetActiveOrderStateGQL },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(UtcheckningProcessComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
