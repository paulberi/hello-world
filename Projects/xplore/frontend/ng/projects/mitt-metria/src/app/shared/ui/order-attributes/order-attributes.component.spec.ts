import { ComponentFixture, TestBed } from "@angular/core/testing";
import { OrderAttributesComponent } from "./order-attributes.component";

describe("OrderAttributesComponent", () => {
  let fixture: ComponentFixture<OrderAttributesComponent>;
  let component: OrderAttributesComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderAttributesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OrderAttributesComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});

