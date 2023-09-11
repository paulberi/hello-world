import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteUserComponent } from './delete-user.component';
import { AuthService } from '@auth0/auth0-angular';
import { Observable, of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Alert } from 'src/assets/alert';
import { DOMHelper } from 'src/app/mockups/DOM-helper';
import { By } from '@angular/platform-browser';
import { User } from 'src/app/models/user';

describe('DeleteUserComponent', () => {
  let component: DeleteUserComponent;
  let fixture: ComponentFixture<DeleteUserComponent>;
  let dh: DOMHelper<DeleteUserComponent>;
  let userServiceMock : any;

  beforeEach(async () => {
    userServiceMock = jasmine.createSpyObj('UserService', ['getUsers', 'deleteUser']);
    userServiceMock.getUsers.and.returnValue(of([]));
    userServiceMock.deleteUser.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      declarations: [ DeleteUserComponent ],
      providers: [
        {provide: AuthService, useClass: AuthServiceStub},
        {provide: Alert, useClass: AlertStub},
        {provide: UserService, useValue: userServiceMock}
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dh = new DOMHelper<DeleteUserComponent>(fixture);
    component.$users = of([{
      "firstName": "Jakob",
      "lastName": "Öhlén",
      "email": "kungen@hubbahubba.com",
      "schoolIds": [],
      "permissions": ["admin"],
      "menuId": []
    }]);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('Check methods when buttons are clicked', () => {
    it('should call userToDelete() when dropdown has been clicked', (done) => {
      spyOn(component, 'UserToDelete');
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        fixture.debugElement.query(By.css("a.navbar-item")).nativeElement.click();
        expect(component.UserToDelete).toHaveBeenCalledTimes(1);
      });
      done();
    });

    it('should call deleteUser() when "Ta bort användare" button has been clicked', () => {
      spyOn(component, 'deleteUser');
      dh.clickButton("Ta bort användare");
      expect(component.deleteUser).toHaveBeenCalledTimes(1);
    });

  });

  describe('Check ngOnInit', () => {
    it('should call getUsers() from userService on NgOnInit()', () => {
      expect(component.$users).toBeDefined();
      expect(component.$users).not.toBeNull();
      expect(userServiceMock.getUsers).toHaveBeenCalledTimes(1);
    });
  });

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
  showAlertAndUpdatePage() {

  }
}

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

class UserServiceStub {
  getUsers() {
    return of([]);
  }

  deleteUser() {

   }
}
