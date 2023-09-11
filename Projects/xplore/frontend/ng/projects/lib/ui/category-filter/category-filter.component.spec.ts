import { ComponentFixture, TestBed } from '@angular/core/testing';

import { XpCategoryFilterComponent } from './category-filter.component';

describe('FilterComponent', () => {
  let component: XpCategoryFilterComponent;
  let fixture: ComponentFixture<XpCategoryFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XpCategoryFilterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpCategoryFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
