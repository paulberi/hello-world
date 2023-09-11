import { TestBed } from "@angular/core/testing";
import { PingService } from "./ping.service";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";

describe("PingService", () => {

  let service: PingService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PingService]
    });
    service = TestBed.inject(PingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Se till att det inte är några anrop kvar
  afterEach(() => {
    httpMock.verify();
  });


  it("Ska returnera 'pong' vid ping", () => {
    const mockResponse = "pong!";

    service.ping().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne("/api/ping", );
    expect(req.request.method).toBe("GET");
    req.flush(mockResponse) ;
  });
});
