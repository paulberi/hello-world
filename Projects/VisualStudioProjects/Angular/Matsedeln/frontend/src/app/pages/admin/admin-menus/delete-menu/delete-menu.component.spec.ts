import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { MenuService } from './../../../../services/menu.service';
import { Alert } from 'src/assets/alert';
import { AdminHeaderComponent } from './../../admin-header/admin-header.component';
import { DOMHelper } from '../../../../mockups/DOM-helper';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { DeleteMenuComponent } from './delete-menu.component';
import { doesNotReject } from 'assert';
import { MunicipalityService } from 'src/app/services/municipality.service';

describe('DeleteMenuComponent', () => {
  let component: DeleteMenuComponent;
  let fixture: ComponentFixture<DeleteMenuComponent>;
  let dh: DOMHelper<DeleteMenuComponent>;
  let mockService : MenuService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteMenuComponent ],
      providers: [
        {provide: MenuService, useClass: MenuServiceStub},
        {provide: Alert, useClass: AlertStub},
        {provide: MunicipalityService, useClass: MunicipalityServiceStub}
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    dh = new DOMHelper<DeleteMenuComponent>(fixture);
    mockService = TestBed.inject(MenuService);
  });

  describe('Create', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('HTML', () => {
    it('should contain a button with text "Ta bort matsedel"', ()=> {
      expect(dh.singleTextFromTagName("button")).toBe("Ta bort matsedel")
    });

    it('should contain a dropdown with 3 menus', (done) => {
      component.$menus = of([
        {_id: '123', menuName:'menu1', startDate: new Date("2021-05-20"), endDate: new Date("2021-06-20")},
        {_id: '345', menuName:'menu2', startDate: new Date("2021-05-20"), endDate: new Date("2021-06-20")},
        {_id: '678', menuName:'menu3', startDate: new Date("2021-05-20"), endDate: new Date("2021-06-20")}
      ]);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(dh.countFromTagName("a.navbar-item")).toBe(3);
      })
      done();
    });
  });

  describe('Check methods when buttons are clicked', () => {
    it('should call deleteMenu() when "Ta bort matsedel" button has been clicked', () => {
      spyOn(component, 'deleteMenu');
      dh.clickButton("Ta bort matsedel");
      expect(component.deleteMenu).toHaveBeenCalledTimes(1);
    });

    it('menuService.deleteMenu should not be called when no menu is selected and "Ta bort matsedel" button clicked', () => {
        let mockSpy = spyOn(mockService,"deleteMenu");
        dh.clickButton("Ta bort matsedel");
        expect(mockSpy).toHaveBeenCalledTimes(0);
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
}

class MenuServiceStub {
  deleteMeal(){
    return of({})
  }
  deleteMenu() {
  }
}

class MunicipalityServiceStub {
  getSchools() {
    return of([]);
  }
  getMunicipalities() {
    return of([]);
  }
  updateSchool() {
  }
}
