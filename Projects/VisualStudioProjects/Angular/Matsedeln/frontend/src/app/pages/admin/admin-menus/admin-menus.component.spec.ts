import { Menu } from './../../../models/menu';
import { MenuMockup } from './../../../mockups/menu-mockup';
import { Observable, of } from 'rxjs';
import { MenuService } from './../../../services/menu.service';
import { Alert } from 'src/assets/alert';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMenusComponent } from './admin-menus.component';
import { AuthService } from '@auth0/auth0-angular';

describe('AdminMenusComponent', () => {
  let component: AdminMenusComponent;
  let fixture: ComponentFixture<AdminMenusComponent>;
  let mockService : MenuService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminMenusComponent ],
      providers: [
        {provide: MenuService, useClass: MenuServiceStub},
        {provide: Alert, useClass: AlertStub},
        {provide: AuthService, useClass: AuthServiceStub}
      ],
       } )
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminMenusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    mockService = TestBed.inject(MenuService);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('Check ngOnInit', () => {
    it('should call NgOnInit() & getMenus()', () => {
      const component = fixture.debugElement.componentInstance;
      let mockSpy = spyOn(mockService,"getMenus");
      component.ngOnInit();
      expect(mockSpy).toHaveBeenCalled();
    });
  });
});

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
}

class MenuServiceStub {
  getMenus() : Observable<Menu> {
    return of(new MenuMockup().getMenu())
  }

  deleteMenu() {

  }
}
