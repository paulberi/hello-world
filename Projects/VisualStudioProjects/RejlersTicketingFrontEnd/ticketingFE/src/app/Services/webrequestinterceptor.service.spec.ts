import { TestBed } from '@angular/core/testing';

import { WebrequestinterceptorService } from './webrequestinterceptor.service';

describe('WebrequestinterceptorService', () => {
  let service: WebrequestinterceptorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebrequestinterceptorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
