import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SamradAdminComponent } from './samrad-admin.component';
import { SamradAdminModule } from './samrad-admin.module';

describe('SamradAdminComponent', () => {
  let component: SamradAdminComponent;
  let fixture: ComponentFixture<SamradAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SamradAdminComponent ],
      imports: [SamradAdminModule, HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SamradAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
