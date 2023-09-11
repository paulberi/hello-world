import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MkFilledNoteringChipComponent } from './filled-notering-chip.component';

describe('FilledNotisComponent', () => {
  let component: MkFilledNoteringChipComponent;
  let fixture: ComponentFixture<MkFilledNoteringChipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkFilledNoteringChipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkFilledNoteringChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
