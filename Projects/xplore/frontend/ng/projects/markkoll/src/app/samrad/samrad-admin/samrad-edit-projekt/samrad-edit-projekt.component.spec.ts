import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogModule } from '@angular/material/dialog';

import { SamradEditProjektComponent } from './samrad-edit-projekt.component';
import { SamradEditProjektModule } from './samrad-edit-projekt.module';

describe('SamradEditProjektComponent', () => {
  let component: SamradEditProjektComponent;
  let fixture: ComponentFixture<SamradEditProjektComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SamradEditProjektComponent ],
      imports: [SamradEditProjektModule, HttpClientTestingModule, MatDialogModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SamradEditProjektComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
