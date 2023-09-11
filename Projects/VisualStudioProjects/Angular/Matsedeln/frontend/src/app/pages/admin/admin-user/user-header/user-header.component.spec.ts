import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '@auth0/auth0-angular';
import { Observable, of } from 'rxjs';
import { DOMHelper } from 'src/app/mockups/DOM-helper';

import { UserHeaderComponent } from './user-header.component';

describe('UserHeaderComponent', () => {
  let component: UserHeaderComponent;
  let helper: DOMHelper<UserHeaderComponent>;
  let fixture: ComponentFixture<UserHeaderComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserHeaderComponent ],
      imports: [
        RouterTestingModule
      ],
      providers:  [
        {provide: AuthService, useClass: AuthServiceStub}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    helper=new DOMHelper(fixture);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should contain 3 buttons', () => {
      expect(helper.countFromTagName("button")).toEqual(3);
    });

    it('first button should have text: "lägg till användare"', () => {
      expect(helper.singleTextFromTagName("button").trim()).toBe("Lägg till användare");
    });

    it('second button should have text: "Uppdatera användare"', () => {
      expect(helper.getSpecificElement("button", 1).textContent.trim()).toBe("Uppdatera användare");
    });

    it('third button should have text: "Ta bort användare"', () => {
      expect(helper.getSpecificElement("button", 2).textContent.trim()).toBe("Ta bort användare");
    });
  });

  describe('Navigation', () => {
    it('"Lägg till användare" button should navigate to /createUser', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl')
      helper.clickButton("Lägg till användare");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/createUser']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });

    it('"Uppdatera användare" button should navigate to /updateUser', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl')
      helper.clickButton("Uppdatera användare");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/updateUser']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });

    it('"Ta bort användare" button should navigate to /deleteUser', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl')
      helper.clickButton("Ta bort användare");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/deleteUser']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });
  });

});
class AuthServiceStub {
  isAuthenticated$ : Observable<boolean>;
  constructor () {
    this.isAuthenticated$ = of(true);
  }
}
