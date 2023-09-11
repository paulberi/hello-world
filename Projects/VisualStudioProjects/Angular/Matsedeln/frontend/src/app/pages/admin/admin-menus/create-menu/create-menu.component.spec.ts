import { Alert } from 'src/assets/alert';
import { MenuService } from './../../../../services/menu.service';
import { MenuMockup } from './../../../../mockups/menu-mockup';
import { Menu } from './../../../../models/menu';
import { Observable, of } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { CreateMenuComponent } from './create-menu.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from '@auth0/auth0-angular';

describe('CreateMenuComponent', () => {
  let component: CreateMenuComponent;
  let fixture: ComponentFixture<CreateMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateMenuComponent ],
      providers: [
        {provide: MenuService, useClass: MenuServiceStub},
        {provide: Alert, useClass: AlertStub},
        {provide: AuthService, useClass: AuthServiceStub}
      ],
      imports: [HttpClientTestingModule]
    })
    .compileComponents();
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(CreateMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should contain a button with text "Skapa matsedel"', () => {
      const buttonElement = fixture.debugElement.query(By.css('button'));
      expect(buttonElement.nativeElement.outerText).toBe("Skapa matsedel");
    });
  });

  describe('Check methods', () => {
    it('calls the createMenu method', ()=>{
      const buttonElement = fixture.debugElement.query(By.css('.manage-menu-button'));
      spyOn(component, 'createMenu')
      buttonElement.triggerEventHandler('click', null);
      expect(component.createMenu).toHaveBeenCalledTimes(1);
    }) ;
  });

});

class AuthServiceStub{
  user$ : Observable<any>;
}

class AlertStub {
  showAdvancedAlert() {
    //Mockup på Alert. Skickar tillbacka ett object med isConfirmed = true. isConfirmed används
    //för att kolla om en måltid ska tas bort. Med denna mockup tas den alltid bort.
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
  postMenu(menu : Menu) : Observable<Menu> {
    return of(new MenuMockup().getMenu())
  }
}
function Done() {
  throw new Error('Function not implemented.');
}

