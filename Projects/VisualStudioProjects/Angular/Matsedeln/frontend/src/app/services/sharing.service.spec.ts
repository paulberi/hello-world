import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { SharingService } from './sharing.service';

describe('SharingService', () => {
  let service: SharingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SharingService]
    });
    service = TestBed.inject(SharingService);
  });

  describe('Create', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });
});
