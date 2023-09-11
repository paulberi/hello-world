import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatTableModule } from "@angular/material/table";
import { RouterTestingModule } from "@angular/router/testing";
import { XpTranslocoTestModule } from "../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../lib/ui/dropzone/dropzone.module";
import { CartItemsComponent } from "../cart-items/cart-items.component";
import { DropzoneComponent } from "./forms/dynamic-form/complex-datatypes/dropzone/dropzone.component";
import { ObjectComponent } from "./forms/dynamic-form/complex-datatypes/object/object.component";
import { CreateOrderComponent } from "./create-order.component";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { OAuthModule } from "angular-oauth2-oidc";
import { PolygonComponent } from "./forms/dynamic-form/complex-datatypes/object/polygon/polygon.component";
import { DynamicFormComponent } from "./forms/dynamic-form/dynamic-form.component";
import { CustomerFormComponent } from "./forms/customer-form/customer-form.component";
import { ProductFormComponent } from "./forms/product-form/product-form.component";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { RectangleComponent } from "./forms/dynamic-form/complex-datatypes/object/rectangle/rectangle.component";
import { testEmptyParentForm, testParentForm } from "../../../../test/data";
import { AdminGetCustomerByEmailGQL, AdminSetOrderCustomFieldsGQL, AdminGetOrCreateActiveOrderForCustomerGQL, AdminAddItemToOrderGQL, AdminOngoingOrderGQL, AdminPlaceOrderGQL, AdminCancelOrderGQL } from "./create-order.admin.generated";
import { MatDialogModule } from "@angular/material/dialog";
import { AdminAdjustOrderLineGQL } from "../../data-access/cart-items.admin.generated";
import { AdminCreateCustomerGQL, AdminFilterCustomerByEmailGQL } from "../../data-access/customer-form.admin.generated";
import { ProductsGQL, ProductVariantsByProductIdGQL } from "../../data-access/product-form.shop.generated";
import { ProductVariantsSchemasByProductIdGQL } from "./create.order.shop.generated";

describe("CreateOrderComponent", () => {
  let fixture: ComponentFixture<CreateOrderComponent>;
  let component: CreateOrderComponent;

  let mockProductsGQL: any;
  let mockProductVariantsByProductIdGQL: any;
  let mockProductVariantsSchemasByProductIdGQL: any;
  let mockAdminSetOrderCustomFieldsGQL: any;
  let mockAdminGetOrCreateActiveOrderForCustomerGQL: any;
  let mockAdminAddItemToOrderGQL: any;
  let mockAdminOngoingOrderGQL: any;
  let mockAdminPlaceOrderGQL: any;
  let mockAdminCancelOrderGQL: any;
  let mockAdminCreateCustomerGQL: any;
  let mockAdminGetCustomerByEmailGQL: any;
  let mockAdminAdjustOrderLineGQL: any;
  let mockAdminFilterCustomerByEmailGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        CreateOrderComponent,
        DynamicFormComponent,
        CustomerFormComponent,
        ProductFormComponent,
        DropzoneComponent,
        ObjectComponent,
        PolygonComponent,
        CartItemsComponent,
        RectangleComponent
      ],
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        RouterTestingModule,
        OAuthModule.forRoot(),
        MatCheckboxModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatRadioModule,
        MatSelectModule,
        MatTableModule,
        MatProgressSpinnerModule,
        XpDropzoneModule,
        XpTranslocoTestModule
      ],
      providers: [
        { provide: ProductsGQL, useValue: mockProductsGQL },
        { provide: ProductVariantsByProductIdGQL, useValue: mockProductVariantsByProductIdGQL },
        { provide: ProductVariantsSchemasByProductIdGQL, useValue: mockProductVariantsSchemasByProductIdGQL },
        { provide: AdminCreateCustomerGQL, useValue: mockAdminCreateCustomerGQL },
        { provide: AdminGetCustomerByEmailGQL, useValue: mockAdminGetCustomerByEmailGQL },
        { provide: AdminSetOrderCustomFieldsGQL, useValue: mockAdminSetOrderCustomFieldsGQL },
        { provide: AdminGetOrCreateActiveOrderForCustomerGQL, useValue: mockAdminGetOrCreateActiveOrderForCustomerGQL },
        { provide: AdminAddItemToOrderGQL, useValue: mockAdminAddItemToOrderGQL },
        { provide: AdminOngoingOrderGQL, useValue: mockAdminOngoingOrderGQL },
        { provide: AdminPlaceOrderGQL, useValue: mockAdminPlaceOrderGQL },
        { provide: AdminCancelOrderGQL, useValue: mockAdminCancelOrderGQL },
        { provide: AdminCreateCustomerGQL, useValue: mockAdminCreateCustomerGQL },
        { provide: AdminGetCustomerByEmailGQL, useValue: mockAdminGetCustomerByEmailGQL },
        { provide: AdminAdjustOrderLineGQL, useValue: mockAdminAdjustOrderLineGQL },
        { provide: AdminFilterCustomerByEmailGQL, useValue: mockAdminFilterCustomerByEmailGQL },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateOrderComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska rensa productForm och dynamicForm när 'cancelOrderForm' körs", () => {
    // Given
    component.initializeParentForm();

    component.parentForm.setValue(testParentForm);
    const spy = jest.spyOn(component, "cancelOrderForm");

    // When
    component.cancelOrderForm();

    // Then
    expect(spy).toHaveBeenCalled();
    expect(component.parentForm.value).toEqual(testEmptyParentForm);
  });
})
