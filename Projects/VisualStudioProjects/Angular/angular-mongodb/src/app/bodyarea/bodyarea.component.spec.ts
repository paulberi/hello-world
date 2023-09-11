import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BodyareaComponent } from './bodyarea.component';

describe('BodyareaComponent', () => {
  let component: BodyareaComponent;
  let fixture: ComponentFixture<BodyareaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BodyareaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BodyareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
