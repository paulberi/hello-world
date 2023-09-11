import { By } from '@angular/platform-browser';
import { DOMHelper } from 'src/app/mockups/DOM-helper';
import { User } from 'src/app/models/user';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';

import { CreateUserComponent } from './create-user.component';
import { AuthService } from '@auth0/auth0-angular';
import { Observable, of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Alert } from 'src/assets/alert';
import { MunicipalityService } from 'src/app/services/municipality.service';

describe('CreateUserComponent', () => {
  let component: CreateUserComponent;
  let fixture: ComponentFixture<CreateUserComponent>;
  let dh: DOMHelper<CreateUserComponent>;
  let mockservice = jasmine.createSpyObj('UserService', ['postUser']);
  mockservice.postUser.and.returnValue(of({}))
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUserComponent ],
      providers: [
        {provide: AuthService, useClass: AuthServiceStub},
        {provide: UserService, useValue: mockservice},
        {provide: Alert, useClass: AlertStub},
        {provide: MunicipalityService, useClass: MunicipalityServiceStub}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dh = new DOMHelper(fixture);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('Method' , () => {
    describe('clickAdmin', ()=> {
      beforeEach(() => {
        component.schoolsToChoose = [{_id: '123', _menuId: '123', schoolName:'Skolan'},{_id: '1234', _menuId: '1234', schoolName:'Andra skolan'}];
      });

      it('should select all schools if admin is checked', () => {
        component.clickAdmin(true);
        expect(component.selectedSchools.length).toBe(2);
      })

      it('should deselect all schools if admin is not checked', () => {
        component.clickAdmin(true);
        component.clickAdmin(false);
        expect(component.selectedSchools.length).toBe(0);
      })
    })

    describe('createUser', () => {
      beforeEach(() => {
        mockservice.postUser.calls.reset();
      })
      it('should call userService.postUser when all required fields is filled', () => {
        component.createUser('Erik', 'Eriksson', 'erik@mail.com', 'password', true, []);
        expect(mockservice.postUser).toHaveBeenCalledTimes(1);
      })

      it('should not call userService.postUser when user password is to short', () => {
        component.createUser('Erik', 'Eriksson', 'erik@mail.com', 'pas', true, []);
        expect(mockservice.postUser).toHaveBeenCalledTimes(0);
      })
      it('should not call userService.postUser when user got no mail', () => {
        component.createUser('Erik', 'Eriksson', '', 'password', true, []);
        expect(mockservice.postUser).toHaveBeenCalledTimes(0);
      })
    })
  })

  describe('html', () => {
      it('should hava a "Spara användare" button that should call createUser', () => {
        spyOn(component, 'createUser');
        dh.clickButton('Spara användare')
        expect(component.createUser).toHaveBeenCalledTimes(1);
      })
  })

});

class AlertStub {
  showAdvancedAlert() {
    const promise = new Promise((res, rej) => {
      const result = {isConfirmed : true};
      res(result);
    });
    return promise;
  }
  showAlert(){
  }

  showAlertAndUpdatePage(){

  }
}

class AuthServiceStub {
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


class MunicipalityServiceStub {
  getSchools() {
    return of([]);
  }
}
