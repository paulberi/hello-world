import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '@auth0/auth0-angular';
import { Observable, of } from 'rxjs';
import { DOMHelper } from 'src/app/mockups/DOM-helper';

import { LogoutComponent } from './logout.component';

describe('LogoutComponent', () => {
  let component: LogoutComponent;
  let fixture: ComponentFixture<LogoutComponent>;
  let helpers: DOMHelper<LogoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LogoutComponent ],
      imports: [
        RouterTestingModule
      ],
      providers:  [
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    helpers= new DOMHelper(fixture);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should contain a button with text "Till startsidan"', () => {
      const buttonElement = fixture.debugElement.query(By.css('button'));
      expect(buttonElement.nativeElement.outerText).toBe("Till startsidan");
    });
  });
});
