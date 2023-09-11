import { MunicipalityService } from './../../../../services/municipality.service';
import { Alert } from 'src/assets/alert';
import { MenuService } from './../../../../services/menu.service';
import { MenuMockup } from './../../../../mockups/menu-mockup';
import { Menu } from './../../../../models/menu';
import { Observable, of } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RemoveMenuComponent } from './remove-menu.component';
import { Municipality } from 'src/app/models/municipality';
import { By } from '@angular/platform-browser';
import { DOMHelper } from '../../../../mockups/DOM-helper';

describe('RemoveMenuComponent', () => {
  let component: RemoveMenuComponent;
  let fixture: ComponentFixture<RemoveMenuComponent>;
  let municipalitiesMockup = new Municipality();
  municipalitiesMockup = {_id: "123", municipalityName: "Umeå", schools: [{_id: "1", schoolName: "Dragonskolan", _menuId: "2"}]};
  let dh: DOMHelper<RemoveMenuComponent>;
  let menuServiceMock : any;
  let municipalityServiceMock : any;

  beforeEach(async () => {
    menuServiceMock = jasmine.createSpyObj('MenuService', ['getMenuName']);
    menuServiceMock.getMenuName.and.returnValue(of([]));

    municipalityServiceMock = jasmine.createSpyObj('MunicipalityService', ['updateSchool']);
    municipalityServiceMock.updateSchool.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      declarations: [ RemoveMenuComponent ],
      providers: [
        {provide: MenuService, useClass: MenuServiceStub},
        {provide: Alert, useClass: AlertStub}  ,
        {provide: MenuService, useValue: menuServiceMock},
        {provide: MunicipalityService, useValue: municipalityServiceMock},
      ],

    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dh = new DOMHelper(fixture);
    component.municipalities = [municipalitiesMockup];
    component.schoolToDelete = municipalitiesMockup.schools[0];
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('Check methods when buttons are clicked', () => {
    it('should call chooseMunicipalityToDelete() when dropdown is clicked', (done) => {
      let mockSpy = spyOn(component, 'chooseMunicipalityToDelete');
      fixture.detectChanges();
       fixture.whenStable().then(() => {
        const dropDown = fixture.debugElement.queryAll(By.css('div.navbar-dropdown'))[0];
        dropDown.children[0].nativeElement.click();
        expect(mockSpy).toHaveBeenCalledTimes(1);
       })
       done();
    });

    it('should call chooseSchoolToDelete()', (done) => {
      let mockSpy = spyOn(component, 'chooseSchoolToDelete');
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const dropDown = fixture.debugElement.queryAll(By.css('div.navbar-dropdown'))[0];
        dropDown.children[0].nativeElement.click();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
       const dropDown2 = fixture.debugElement.queryAll(By.css('div.navbar-dropdown'))[1];
       dropDown2.children[0].nativeElement.click();
       expect(mockSpy).toHaveBeenCalledTimes(1);
      })
    })
      done();
    });

    it('should call getMenuName() from menuService when chooseSchoolToDelete() is run', () => {
      component.chooseSchoolToDelete({_id: "1", schoolName: "Dragonskolan", _menuId: "2"});
      expect(menuServiceMock.getMenuName).toHaveBeenCalledTimes(1);
    });

    it('should click deleteMenuFromSchool button and call deleteMenuFromSchool()', () => {
      let mockSpy = spyOn(component, 'deleteMenuFromSchool');
      dh.clickButton("Ta bort matsedel från skolan");
      expect(mockSpy).toHaveBeenCalledTimes(1);
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

class MenuServiceStub {
  getMenus() : Observable<Menu> {
    return of(new MenuMockup().getMenu())
  }
  getMenuName(menuId : string) : Observable<Menu> {
    return of(new MenuMockup().getMenu())
  }
}
