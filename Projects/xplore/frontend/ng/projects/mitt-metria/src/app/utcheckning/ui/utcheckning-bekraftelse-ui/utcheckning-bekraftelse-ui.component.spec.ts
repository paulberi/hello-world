/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { UtcheckningBekraftelseUiComponent } from './utcheckning-bekraftelse-ui.component';
import { TranslocoTestingModule } from '@ngneat/transloco';

describe('UtcheckningBekraftelseUiComponent', () => {
  let component: UtcheckningBekraftelseUiComponent;
  let fixture: ComponentFixture<UtcheckningBekraftelseUiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UtcheckningBekraftelseUiComponent ],
      imports: [
        TranslocoTestingModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UtcheckningBekraftelseUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
