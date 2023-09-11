import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ProductFilterChipsComponent } from './product-filter-chips.component';
import { MMProductFilterChipsModule } from "./product-filter-chips.module";

describe('ProductFilterChipsComponent', () => {
  let component: ProductFilterChipsComponent;
  let fixture: ComponentFixture<ProductFilterChipsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductFilterChipsComponent],
      imports: [
        RouterTestingModule,
        MMProductFilterChipsModule
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProductFilterChipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
