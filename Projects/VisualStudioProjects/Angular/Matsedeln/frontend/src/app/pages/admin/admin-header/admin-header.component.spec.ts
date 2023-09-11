import { DOMHelper } from '../../../mockups/DOM-helper';
import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminHeaderComponent } from './admin-header.component';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

describe('AdminHeaderComponent', () => {
  let component: AdminHeaderComponent;
  let fixture: ComponentFixture<AdminHeaderComponent>;
  let dh: DOMHelper<AdminHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminHeaderComponent ],
      imports: [
        RouterTestingModule
      ],
      providers:  [

      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminHeaderComponent);
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
    it('should contain 3 buttons', () => {
      expect(dh.countFromTagName("button")).toEqual(3);
    });

    it('first button should have text: "Administrera matsedel"', () => {
      expect(dh.singleTextFromTagName("button").trim()).toBe("Administrera matsedel");
    });

    it('second button should have text: "Administrera skola"', () => {
      expect(dh.getSpecificElement("button", 1).textContent.trim()).toBe("Administrera skola");
    });

    it('third button should have text: "Administrera maträtter"', () => {
      expect(dh.getSpecificElement("button", 2).textContent.trim()).toBe("Administrera maträtter");
    });
  });

  describe('Navigation', () => {
    it('should be in root before buttonclick', () => {
      const location = TestBed.inject(Location);
      expect(location.path()).toBe('');
    });

    it('"Administrera matsedel" button should navigate to /adminMenus', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl')
      dh.clickButton("Administrera matsedel");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/adminMenus']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });

    it('"Administrera skola" button should navigate to /adminSchools', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl');
      dh.clickButton("Administrera skola");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/adminSchools']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });

    it('"Administrera maträtter" button should navigate to /adminMeals', (done) => {
      const router = TestBed.get(Router);
      spyOn(router, 'navigateByUrl');
      dh.clickButton("Administrera maträtter");
      expect(router.navigateByUrl).
      toHaveBeenCalledWith(router.createUrlTree(['/adminMeals']),{ skipLocationChange: false, replaceUrl: false, state: undefined});
      done();
    });
  });


});
