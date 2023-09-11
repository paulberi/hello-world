import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AuthService } from '@auth0/auth0-angular';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';
import { FooterComponent } from './footer.component';
import { Component } from '@angular/core';
import { Observable, of } from 'rxjs';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[RouterTestingModule.withRoutes(
        [
          { path:'admin', component: TempRouter},
          {path: 'logout', component: TempRouter}
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
  });

  it('should contain button with Logga ut', () => {
  const location=TestBed.inject(Location);
    const loginButton = fixture.debugElement.queryAll(By.css('button'));
    expect(loginButton[2].nativeNode.outerText).toBe("Logga ut");
  });

  it('should contain button with Administrera matsedel', () => {
    const location=TestBed.inject(Location);
    const administrera = fixture.debugElement.queryAll(By.css('button'));
    expect(administrera[1].nativeNode.outerText).toBe("Administrera matsedel");
  });
});

@Component({template: ''})

class TempRouter{}

class AuthServiceStub {
  user$ : Observable<any>;
  isAuthenticated$ : Observable<boolean>;
  constructor () {
    this.isAuthenticated$ = of(true);
    this.user$ = of({"picture": {
      "firstName": "Jakob",
      "lastName": "Öhlén",
      "email": "kungen@hubbahubba.com",
      "schoolIds": [],
      "permissions": ["admin"],
      "menuId": []}});
  }
}
