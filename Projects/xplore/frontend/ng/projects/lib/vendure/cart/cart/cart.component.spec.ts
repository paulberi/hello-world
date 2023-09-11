import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { XpCartComponent } from './cart.component';
import { XpCartModule } from './cart.module';

describe('XpCartComponent', () => {
  let component: XpCartComponent;
  let fixture: ComponentFixture<XpCartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XpCartComponent ],
      imports: [
        XpCartModule, 
        TranslocoTestingModule,
        NoopAnimationsModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Ska kunna skapa komponent', () => {
    expect(component).toBeTruthy();
  });
});
