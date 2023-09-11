import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { MkCountrySelectComponent } from './country-select.component';
import { MkCountrySelectModule } from "./country-select.module";

describe('MkCountrySelectComponent', () => {
  let component: MkCountrySelectComponent;
  let fixture: ComponentFixture<MkCountrySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MkCountrySelectModule,
        BrowserAnimationsModule,
        HttpClientModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkCountrySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
