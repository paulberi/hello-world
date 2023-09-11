import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MkAdditionalNoteringarChipComponent } from './additional-noteringar-chip.component';

describe('AdditionalNoteringarChipComponent', () => {
  let component: MkAdditionalNoteringarChipComponent;
  let fixture: ComponentFixture<MkAdditionalNoteringarChipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkAdditionalNoteringarChipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkAdditionalNoteringarChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
