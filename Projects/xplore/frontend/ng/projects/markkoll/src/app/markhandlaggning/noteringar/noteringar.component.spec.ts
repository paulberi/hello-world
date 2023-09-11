import { ComponentFixture, TestBed } from '@angular/core/testing';
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";

import { MkNoteringarComponent } from './noteringar.component';

describe('NotiserComponent', () => {
  let component: MkNoteringarComponent;
  let fixture: ComponentFixture<MkNoteringarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkNoteringarComponent ],
      imports: [ XpTranslocoTestModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkNoteringarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
