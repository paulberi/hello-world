import { TestBed } from '@angular/core/testing';

import { CommentToOrdersService } from './comment-to-orders.service';

describe('CommentToOrdersService', () => {
  let service: CommentToOrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentToOrdersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
