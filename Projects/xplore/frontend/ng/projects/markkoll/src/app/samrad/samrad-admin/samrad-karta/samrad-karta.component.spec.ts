import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SamradKartaComponent } from './samrad-karta.component';

describe('SamradKartaComponent', () => {
  let component: SamradKartaComponent;
  let fixture: ComponentFixture<SamradKartaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SamradKartaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SamradKartaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
