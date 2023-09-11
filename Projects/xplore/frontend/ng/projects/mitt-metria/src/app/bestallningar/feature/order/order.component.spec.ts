import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { RouterTestingModule } from "@angular/router/testing";
import { OAuthModule } from "angular-oauth2-oidc";
import { XpTranslocoTestModule } from "../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../lib/ui/dropzone/dropzone.module";
import { CartItemsComponent } from "../cart-items/cart-items.component";
import { CreateOrderComponent } from "../create-order/create-order.component";
import { CustomerFormComponent } from "../create-order/forms/customer-form/customer-form.component";
import { DropzoneComponent } from "../create-order/forms/dynamic-form/complex-datatypes/dropzone/dropzone.component";
import { ObjectComponent } from "../create-order/forms/dynamic-form/complex-datatypes/object/object.component";
import { PolygonComponent } from "../create-order/forms/dynamic-form/complex-datatypes/object/polygon/polygon.component";
import { RectangleComponent } from "../create-order/forms/dynamic-form/complex-datatypes/object/rectangle/rectangle.component";
import { DynamicFormComponent } from "../create-order/forms/dynamic-form/dynamic-form.component";
import { ProductFormComponent } from "../create-order/forms/product-form/product-form.component";
import { OrderComponent } from "./order.component";

describe("OrderComponent", () => {
  let fixture: ComponentFixture<OrderComponent>;
  let component: OrderComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        OrderComponent,
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
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatRadioModule,
        MatSelectModule,
        MatTabsModule,
        MatTableModule,
        MatProgressSpinnerModule,
        XpDropzoneModule,
        XpTranslocoTestModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrderComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
