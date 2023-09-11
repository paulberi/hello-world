import { TestBed } from '@angular/core/testing';
import { DataService } from './shared 11-54-25-772/data.service';



describe('DataService', () => {
  let service: DataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
