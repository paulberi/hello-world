import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { WebReqService } from './web-req.service';

describe('WebReqService', () => {
  let service: WebReqService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [WebReqService]
    });
    service = TestBed.inject(WebReqService);
  });

  describe('Create', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });
});
