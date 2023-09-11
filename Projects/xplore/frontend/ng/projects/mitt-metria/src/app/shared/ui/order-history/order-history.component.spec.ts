import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatButtonModule } from "@angular/material/button";
import { MatChipsModule } from "@angular/material/chips";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatIconModule } from "@angular/material/icon";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { XpTranslocoTestModule } from "../../../../../../lib/translate/translocoTest.module.translate";
import { OrderAttributesComponent } from "../order-attributes/order-attributes.component";
import { OrderHistoryComponent } from "./order-history.component";

describe("OrderHistoryComponent", () => {
  let component: OrderHistoryComponent;
  let fixture: ComponentFixture<OrderHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderHistoryComponent, OrderAttributesComponent],
      imports: [
        MatProgressSpinnerModule,
        MatPaginatorModule,
        MatExpansionModule,
        NoopAnimationsModule,
        XpTranslocoTestModule,
        MatButtonModule,
        MatIconModule,
        MatChipsModule
      ],
    })
      .compileComponents();
  },
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderHistoryComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
