import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { DinKartproduktComponent } from './din-kartprodukt.component';

describe('DinKartproduktComponent', () => {
  let component: DinKartproduktComponent;
  let fixture: ComponentFixture<DinKartproduktComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DinKartproduktComponent ],
      imports: [
        TranslocoTestingModule,
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(DinKartproduktComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
