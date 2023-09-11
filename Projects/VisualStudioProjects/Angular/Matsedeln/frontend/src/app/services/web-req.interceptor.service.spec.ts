import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { WebReqInterceptorService } from './web-req.interceptor.service';

describe('WebReq.InterceptorService', () => {
  let service: WebReqInterceptorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [WebReqInterceptorService]
    });
    service = TestBed.inject(WebReqInterceptorService);
  });

  describe('Create', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });
});
