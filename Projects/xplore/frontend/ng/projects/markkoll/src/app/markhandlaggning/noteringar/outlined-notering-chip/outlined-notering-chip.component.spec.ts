import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MkOutlinedNoteringChipComponent } from './outlined-notering-chip.component';

describe('OutlinedNotisComponent', () => {
  let component: MkOutlinedNoteringChipComponent;
  let fixture: ComponentFixture<MkOutlinedNoteringChipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkOutlinedNoteringChipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkOutlinedNoteringChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
