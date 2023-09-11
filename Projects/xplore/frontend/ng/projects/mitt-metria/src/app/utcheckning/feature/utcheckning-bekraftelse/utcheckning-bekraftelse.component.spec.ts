import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UtcheckningBekraftelseComponent } from './utcheckning-bekraftelse.component';

describe('UtcheckningBekraftelseComponent', () => {
  let component: UtcheckningBekraftelseComponent;
  let fixture: ComponentFixture<UtcheckningBekraftelseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UtcheckningBekraftelseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UtcheckningBekraftelseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
