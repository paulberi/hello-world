import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { BannerComponent } from './banner.component';
import { MMBannerModule } from './banner.module';

describe('BannerComponent', () => {
  let component: BannerComponent;
  let fixture: ComponentFixture<BannerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BannerComponent ],
      imports: [MMBannerModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska visa en bannerbild", () => {
    const image = fixture.debugElement.query(By.css("#banner-image"));

    expect(image).toBeTruthy();
  });
});
