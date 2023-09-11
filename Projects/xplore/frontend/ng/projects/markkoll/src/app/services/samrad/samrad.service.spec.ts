import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { SamradService } from './samrad.service';

describe('SamradService', () => {
  let service: SamradService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(SamradService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
