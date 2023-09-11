import {fakeAsync, TestBed, tick} from '@angular/core/testing'
import {ErrorHandlingHttpClient} from "./http-client.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {MatSnackBar} from "@angular/material/snack-bar";


describe('Service: ErrorHandlingHttpClient', () => {
    let service: ErrorHandlingHttpClient;
    let backend: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [
                ErrorHandlingHttpClient,
              {provide: MatSnackBar, useValue: {open: jest.fn()}}
            ],
        });
        backend = TestBed.inject(HttpTestingController);
        service = TestBed.inject(ErrorHandlingHttpClient);
    });

    afterEach(() => {
        backend.verify();
    });

    it('should create ErrorHandlingHttpClient successfully', () => {
        expect(service).toBeDefined();
    });

    it("should make a successful http GET request", fakeAsync(() => {
        service.get("/get/request?q=123").subscribe((response: any) => {
            expect(response.value).toBe("XYZ");
        });

        const req = backend.expectOne("/get/request?q=123");
        expect(req.request.method).toEqual("GET");

        req.flush({value: "XYZ"});
        tick();
    }));

    it("should make a successful http POST request", fakeAsync(() => {
        service.post("/request/post", {parameter: "ABC"}).subscribe((response: any) => {
            expect(response.value).toBe("ABCXYZ");
        });
        const req = backend.expectOne("/request/post");
        expect(req.request.method).toEqual("POST");
        expect(req.request.body.parameter).toEqual("ABC");

        req.flush({value: "ABCXYZ"});
        tick();
    }));

    it("should make an unauthorized http GET request", fakeAsync(() => {
        service.get("/get/request?q=123").subscribe((response: any) => {

        }, error => {
            expect(error.status).toBe(401);
        });
        backend.expectOne("/get/request?q=123").flush(null, {status: 401, statusText: 'Unauthorized'});
        tick();
    }));

    it("should make an unauthorized http POST request", fakeAsync(() => {
        service.post("/request/post", {}).subscribe((response: any) => {
            expect(response).toBeFalsy();
        }, error => {
            expect(error.status).toBe(401);
        });
        backend.expectOne("/request/post").flush(null, {status: 401, statusText: 'Unauthorized'});
        tick();
    }));

});
