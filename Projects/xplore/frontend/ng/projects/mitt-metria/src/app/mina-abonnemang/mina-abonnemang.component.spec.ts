import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { XpTranslocoTestModule } from '../../../../lib/translate/translocoTest.module.translate';
import { CustomerChannelsGQL } from './data-access/mina-abonnemang.shop.generated';
import { MinaAbonnemangComponent } from './mina-abonnemang.component';


describe('MinaAbonnemangComponent', () => {
  let component: MinaAbonnemangComponent;
  let fixture: ComponentFixture<MinaAbonnemangComponent>;
  let mockCustomerChannelsGQL: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinaAbonnemangComponent ],
      imports: [MatProgressSpinnerModule, MatExpansionModule, NoopAnimationsModule, XpTranslocoTestModule, MatButtonModule, MatIconModule],
      providers: [
        { provide: CustomerChannelsGQL, useValue: mockCustomerChannelsGQL },
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinaAbonnemangComponent);
    component = fixture.componentInstance;
  });

  it('Ska kunna skapa komponent', () => {
    expect(component).toBeTruthy();
  });
});


