import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { ProductFormComponent } from "./product-form.component";
import { ProductFormPresenter } from './product-form.presenter';
import { XpTranslocoTestModule } from "../../../../../../../../lib/translate/translocoTest.module.translate";
import { ProductsGQL, ProductVariantsByProductIdGQL } from "../../../../data-access/product-form.shop.generated";

describe("DynamicFormComponent", () => {
  let fixture: ComponentFixture<ProductFormComponent>;
  let component: ProductFormComponent;

  let mockProductsGQL: any;
  let mockProductVariantsByProductIdGQL: any;

  const presenter = new ProductFormPresenter();

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductFormComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        XpTranslocoTestModule,
      ],
      providers: [
        { provide: ProductsGQL, useValue: mockProductsGQL },
        { provide: ProductVariantsByProductIdGQL, useValue: mockProductVariantsByProductIdGQL },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska emitta event när man ändrar produkt", () => {
    // Given
    presenter.initializeForm();
    const spy = jest.spyOn(presenter.productIdChange, "emit");
    const testValue = "1";

    // When
    presenter.productForm.get("productId").setValue(testValue);

    // Then
    expect(spy).toHaveBeenCalledWith(testValue);
  });
});
