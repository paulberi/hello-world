import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';

import { LoginFailedComponent } from './login-failed.component';
import { DOMHelper } from 'src/app/mockups/DOM-helper';

class TempRouter{}

describe('LoginFailedComponent', () => {
  let component: LoginFailedComponent;
  let fixture: ComponentFixture<LoginFailedComponent>;
  let dh: DOMHelper<LoginFailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[RouterTestingModule.withRoutes(
        [
          { path:'loginFailed', component: TempRouter},
          { path: '', component: TempRouter}
        ]
      )],
      declarations: [ LoginFailedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginFailedComponent);
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
    it('should contain a label with text "Inloggning misslyckades!"', () => {
      expect(dh.singleTextFromTagName("p").trim()).toBe("Inloggning misslyckades!");
    });

    it('should contain only one button', () => {
      expect(dh.countFromTagName("button")).toEqual(1);
    });

    it('should contain button with text "Till startsidan"', () => {
      expect(dh.singleTextFromTagName("button").trim()).toBe("Till startsidan");
    });
  });

  describe('Navigation', () => {
    it('should be in route / (/loginFailed) before first button click', () => {
      const location = TestBed.inject(Location);
      expect(location.path()).toBe('');
    });
  });
});
