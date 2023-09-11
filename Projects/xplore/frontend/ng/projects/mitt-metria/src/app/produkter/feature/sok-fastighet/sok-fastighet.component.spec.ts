import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SokFastighetComponent } from './sok-fastighet.component';
import { MMSokFastighetModule } from './sok-fastighet.module';

describe('SokFastighetComponent', () => {
  let component: SokFastighetComponent;
  let fixture: ComponentFixture<SokFastighetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SokFastighetComponent ],
      imports: [HttpClientTestingModule, MatSnackBarModule, MMSokFastighetModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(SokFastighetComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
