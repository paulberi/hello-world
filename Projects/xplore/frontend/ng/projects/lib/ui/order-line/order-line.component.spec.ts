import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { XpOrderLineComponent } from './order-line.component';
import { XpOrderLineModule } from './order-line.module';

describe('XpOrderLineComponent', () => {
  let component: XpOrderLineComponent;
  let fixture: ComponentFixture<XpOrderLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XpOrderLineComponent ],
      imports: [XpOrderLineModule, TranslocoTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpOrderLineComponent);
    component = fixture.componentInstance;
  });

  it('Ska kunna skapa komponent', () => {
    expect(component).toBeTruthy();
  });
});
