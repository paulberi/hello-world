import { HttpClient, HttpRequest } from "@angular/common/http";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { TestBed } from "@angular/core/testing";
import { XpTranslateHttpLoader } from "./translateHttpLoader.translate";

describe("translateHttpLoader", () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  it("Ska gå att lägga till en queryparameter som representerar språkfilsversionen", (done) => {
    expect.assertions(1);

    const translation = {
      key: "value"
    };
    const version = { "se": "version_1" };

    let loader = new XpTranslateHttpLoader(httpClient, version, "test1.");
    loader.getTranslation("se").subscribe(translation => {
      let obj = parseJsonUnquotedKeys(translation as string);

      expect(obj).toEqual(translation);
      done();
    });

    const req = httpMock.expectOne("test1.se.json?v="+version["se"]);

    req.flush(translation);
  });

  it("Ska kunna ladda in översättningar från mer än en fil", (done) => {
    expect.assertions(2);

    let loader = new XpTranslateHttpLoader(httpClient, null, "test1.", "test2.");
    let translation_1 = { key1: "value1" };
    let translation_2 = { key2: "value2" };

    loader.getTranslation("se").subscribe(translation => {
      let obj = parseJsonUnquotedKeys(translation as string);

      expect(obj.key1).toEqual(translation_1.key1);
      expect(obj.key2).toEqual(translation_2.key2);
      done();
    });

    const req1 = httpMock.expectOne("test1.se.json");
    req1.flush(translation_1);

    const req2 = httpMock.expectOne("test2.se.json");
    req2.flush(translation_2);
  })

  it("Ska gå att skriva över ett fält i en språkfil", (done) => {
    expect.assertions(1);

    let translation_1 = {
      xp: {
        key1: "value1",
        key2: "value2",
      }
    }

    let translation_2 = {
      test: {
        key: "value"
      },
      xp: {
        key2: "newValue"
      }
    }

    let translation_expect = {
      test: {
        key: "value"
      },
      xp: {
        key1: "value1",
        key2: "newValue"
      }
    }

    let loader = new XpTranslateHttpLoader(httpClient, null, "test1.", "test2.");
    loader.getTranslation("se").subscribe(translation => {
      let obj = parseJsonUnquotedKeys(translation as string);

      expect(obj).toEqual(translation_expect);
      done();
    });

    const req1 = httpMock.expectOne("test1.se.json");
    req1.flush(translation_1);

    const req2 = httpMock.expectOne("test2.se.json");
    req2.flush(translation_2);
  })
});

function parseJsonUnquotedKeys(translation: string): any {
  // Regex som lägger till cittecken runt keys, t.ex. en_key => "en_key"
  const pattern = /(['"])?([a-z0-9A-Z_]+)(['"])?:/g;

  let quoted = JSON.stringify(translation).replace(pattern, '"$2": ');

  return JSON.parse(quoted);
}
