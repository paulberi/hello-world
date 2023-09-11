import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { RouterTestingModule } from "@angular/router/testing";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpTranslocoTestModule } from "../../../../../../../../lib/translate/translocoTest.module.translate";
import { CustomerFormComponent } from "./customer-form.component";
import { AdminCreateCustomerGQL, AdminFilterCustomerByEmailGQL } from "../../../../data-access/customer-form.admin.generated";

describe("CustomerFormComponent", () => {
  let fixture: ComponentFixture<CustomerFormComponent>;
  let component: CustomerFormComponent;
  let mockAdminCreateCustomerGQL: any;
  let mockAdminFilterCustomerByEmailGQL: any;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerFormComponent],
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatIconModule,
        MatProgressSpinnerModule,
        XpTranslocoTestModule
      ],
      providers: [
        { provide: AdminCreateCustomerGQL, useValue: mockAdminCreateCustomerGQL },
        { provide: AdminFilterCustomerByEmailGQL, useValue: mockAdminFilterCustomerByEmailGQL }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CustomerFormComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
