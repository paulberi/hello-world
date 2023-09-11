import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { AuthService } from '@auth0/auth0-angular';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';
import { FooterComponent } from './footer.component';
import { Component } from '@angular/core';
import { Observable, of } from 'rxjs';
import { DOMHelper } from 'src/app/mockups/DOM-helper';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;
  let dh: DOMHelper<FooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[RouterTestingModule.withRoutes(
        [
          { path:'authLogin', component: TempRouter },
          { path: 'home', component: TempRouter },
          { path: '', component: TempRouter }
        ]
      )],
      providers:[
        {provide: AuthService, useClass: AuthServiceStub}
      ],
      declarations: [
        FooterComponent
       ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dh = new DOMHelper(fixture);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should contain a button with text "Logga in"', () => {
      const loginButton = fixture.debugElement.queryAll(By.css('button'));
      expect(loginButton[1].nativeNode.outerText).toBe("Logga in");
    });
  });

  describe('Navigation', () => {
    it('should navigate to / before first button click', fakeAsync(() => {
      const location =TestBed.inject(Location);
      expect(location.path()).toBe('');
    }));
  });

});

@Component({template: ''})

class TempRouter{}

class AuthServiceStub{
  user$ : Observable<any>;
  constructor () {
    this.user$ = of({"picture": {
      "firstName": "Jakob",
      "lastName": "Öhlén",
      "email": "kungen@hubbahubba.com",
      "schoolIds": [],
      "permissions": ["admin"],
      "menuId": []}});
  }
}
