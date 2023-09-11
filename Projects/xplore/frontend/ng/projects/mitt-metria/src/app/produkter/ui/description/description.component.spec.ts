import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DescriptionComponent } from './description.component';

describe('DescriptionComponent', () => {
  let component: DescriptionComponent;
  let fixture: ComponentFixture<DescriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DescriptionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DescriptionComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
