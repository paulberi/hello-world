import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { AvtalsinstallningarComponent } from './avtalsinstallningar.component';

describe('AvtalsinformationComponent', () => {
  let component: AvtalsinstallningarComponent;
  let fixture: ComponentFixture<AvtalsinstallningarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AvtalsinstallningarComponent,
        BrowserAnimationsModule
    ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvtalsinstallningarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
