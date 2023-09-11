import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoTestingModule } from '@ngneat/transloco';
import { MMDescriptionModule } from '../description/description.module';
import { MMHeaderModule } from '../header/header.module';

import { ProductInformationComponent } from './product-information.component';

describe('ProductInformationComponent', () => {
  let component: ProductInformationComponent;
  let fixture: ComponentFixture<ProductInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductInformationComponent ],
      imports: [
        MMHeaderModule, 
        MMDescriptionModule, 
        MatIconModule, 
        MatButtonModule, 
        TranslocoTestingModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
