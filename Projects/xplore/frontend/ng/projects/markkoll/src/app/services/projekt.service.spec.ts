import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { TestBed } from "@angular/core/testing";
import { ProjektService } from "./projekt.service";

let projektService: ProjektService;
let httpMock: HttpTestingController;

describe("ProjektService", () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProjektService]
    });

    projektService = TestBed.inject(ProjektService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it("should be created", () => {
    expect(projektService).toBeTruthy();
  });
});
