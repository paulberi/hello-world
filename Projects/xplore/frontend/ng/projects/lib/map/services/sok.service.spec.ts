import {fakeAsync, TestBed, tick} from '@angular/core/testing'
import {SokService} from "./sok.service";
import {ErrorHandlingHttpClient} from "../../http/http-client.service";
import {ConfigService} from "../../config/config.service";
import {MockConfigService} from "../test-context.util";
import {of} from "rxjs";
import {transform} from "ol/proj";


jest.mock("../../http/http-client.service");
jest.mock("../../config/config.service");
jest.mock("ol/proj");

describe('Service: SokService', () => {
    let service: SokService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [
                SokService,
                {provide: ConfigService, useClass: MockConfigService},
                ErrorHandlingHttpClient
            ],
        });

        service = TestBed.get(SokService);

        //Static "mock"
        ConfigService.urlConfig = {
            sokServiceUrl: "/api/sok",
            configurationUrl: ""
        };
    });

    it('should create an instance successfully', () => {
        expect(service).toBeDefined()
    });

    it("findFastighet should return a UNIQUE list of non-empty fastigheter", fakeAsync(() => {
        let response = {
            features: [
                {
                    properties: {
                        bbox: [6585232.640000001, 676277.1199999999, 6586379.5200000005, 677423.9999999999],
                        kommunnamn: "KOMMUNNAMN123",
                        trakt: "TRAKT123",
                        blockenhet: "BLOCKENHET123",
                        objekt_id: "OBJEKT_ID123"
                    }
                },
                {
                    properties: {
                        bbox: [6585232.640000001, 676277.1199999999, 6586379.5200000005, 677423.9999999999],
                        kommunnamn: "KOMMUNNAMN123",
                        trakt: "TRAKT123",
                        blockenhet: "BLOCKENHET123",
                        objekt_id: "OBJEKT_ID123"
                    }
                },
                {
                    properties: {
                        bbox: [6585232.640000001, 676277.1199999999, 6586379.5200000005, 677423.9999999999],
                        kommunnamn: "KOMMUNNAMN123",
                        trakt: "",
                        blockenhet: "",
                        objekt_id: ""
                    }
                }
            ]
        };
        (<any>ErrorHandlingHttpClient.prototype.get).mockImplementation(() => of(response));
        //För att undvika att behöva sätta upp proj4defs så mockar vi bort denna funktion, dessutom så testar vi inte ol/proj
        (<any>transform).mockImplementation(() => [0, 0]);

        service.findFastighet("MOCKBY TESTTUNA 1:23").subscribe(fastigheter => {
            expect(fastigheter.length).toBe(1);
            expect(fastigheter[0].name).toBe("KOMMUNNAMN123 TRAKT123 BLOCKENHET123");
            expect(fastigheter[0].type).toBe("0000");
        });
        tick();
    }));

});
