import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { MkMatSelectSearchComponent } from './mat-select-search.component';

describe('MatSelectSearchComponent', () => {
  let component: MkMatSelectSearchComponent;
  let fixture: ComponentFixture<MkMatSelectSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MkMatSelectSearchComponent,
        BrowserAnimationsModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MkMatSelectSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
