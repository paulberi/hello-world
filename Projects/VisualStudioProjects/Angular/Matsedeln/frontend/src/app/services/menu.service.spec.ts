import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MenuService } from './menu.service';
import { MenuMockup } from '../mockups/menu-mockup';
import { environment } from './../../environments/environment';

describe('MenuService', () => {
  let service: MenuService;
  let httpMock : HttpTestingController;
  let menuMockup : MenuMockup = new MenuMockup();
  let ROOT_URL = environment.ROOT_URL;
  let menuId = menuMockup.getMenu()._id;
  let mealId = menuMockup.getMenu().meals[0]._id;
  let meal = menuMockup.getMenu().meals[0];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MenuService]
    });
    service = TestBed.inject(MenuService);
    httpMock = TestBed.inject(HttpTestingController);
    menuMockup = new MenuMockup();
  });

  afterEach(() => {
    httpMock.verify();
  })

  describe('Create', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });

  describe('API via get', () => {
    it('should retrieve menus from API via GET', ()=> {
      service.getMenus().subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      });
      const request = httpMock.expectOne(`${ROOT_URL}/public/menu`);
      expect(request.request.method).toBe('GET');
      request.flush(menuMockup);
    });

    it('should retrieve one menu from API via GET', ()=> {
      service.getMenu(menuId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/public/menu/${menuId}`);
      expect(request.request.method).toBe('GET');
      request.flush(menuMockup);
    });

    it('should get meal from API via get', ()=> {
      service.getMeal(menuId, mealId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/public/menu/${menuId}/meal/${mealId}`);
      expect(request.request.method).toBe('GET');
      request.flush(menuMockup);
    });

    it('should get menuName from API via get', ()=> {
      service.getMenuName(menuId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/public/menu/name/${menuId}`);
      expect(request.request.method).toBe('GET');
      request.flush(menuMockup);
    });
  });

  describe('API via patch', () => {
    it('should update one menu from API via patch', ()=> {
      service.updateMenu(menuMockup.getMenu()).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu/${menuId}`);
      expect(request.request.method).toBe('PATCH');
      request.flush(menuMockup);
    });

    it('should update meal from API via update', ()=> {
      service.updateMeal(meal, menuId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu/${menuId}/meal/${mealId}`);
      expect(request.request.method).toBe('PATCH');
      request.flush(menuMockup);
    });
  });

  describe('API via delete', () => {
    it('should delete meal from API via delete', ()=> {
      service.deleteMeal(menuId, mealId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu/${menuId}/meal/${mealId}`);
      expect(request.request.method).toBe('DELETE');
      request.flush(menuMockup);
    });

    it('should delete menu from API via delete', ()=> {
      service.deleteMenu(menuId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu/${menuId}`);
      expect(request.request.method).toBe('DELETE');
      request.flush(menuMockup);
    });
  });

  describe('API via post', () => {
    it('should create meal from API via post', ()=> {
      service.postMeal(meal, menuId).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu/${menuId}/meal`);
      expect(request.request.method).toBe('POST');
      request.flush(menuMockup);
    });

    it('should create menu from API via post', ()=> {
      service.postMenu(menuMockup.getMenu()).subscribe(menus => {
        expect(menus).toEqual(menuMockup);
      })
      const request = httpMock.expectOne(`${ROOT_URL}/private/menu`);
      expect(request.request.method).toBe('POST');
      request.flush(menuMockup);
    });
  });
});
