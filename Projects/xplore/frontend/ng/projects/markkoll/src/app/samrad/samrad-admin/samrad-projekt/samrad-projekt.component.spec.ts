import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SamradProjektComponent } from './samrad-projekt.component';
import { SamradProjektModule } from './samrad-projekt.module';

describe('SamradProjektComponent', () => {
  let component: SamradProjektComponent;
  let fixture: ComponentFixture<SamradProjektComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SamradProjektComponent ],
      imports: [SamradProjektModule, HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SamradProjektComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
