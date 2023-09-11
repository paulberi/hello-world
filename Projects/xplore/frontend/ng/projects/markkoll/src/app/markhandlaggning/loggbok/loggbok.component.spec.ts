import { DatePipe, registerLocaleData } from "@angular/common";
import localeSv from "@angular/common/locales/sv";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatDividerModule } from "@angular/material/divider";
import { MkLoggbokComponent } from "./loggbok.component";
import { MockModule, MockProvider } from "ng-mocks";
import { MkMockLoggbok } from "./loggbok.mock";
import { clickButtonBySelector, queryAllBySelector, queryBySelector } from "../../../test/jest-util";
import { By } from "@angular/platform-browser";
import { LOCALE_ID } from "@angular/core";
registerLocaleData(localeSv, "sv");

describe("MkLoggbokComponent", () => {
  let component: MkLoggbokComponent;
  let fixture: ComponentFixture<MkLoggbokComponent>;
  let datePipe: DatePipe;

  beforeEach(async () => {
    await TestBed.configureTestingModule({           
      declarations: [MkLoggbokComponent],
      imports: [
          MockModule(MatButtonModule),
          MockModule(MatCardModule),
          MockModule(MatDividerModule),
        ],
      providers: [
        MockProvider(DatePipe),
        { provide: LOCALE_ID, useValue: "sv-SE" },
      ]        
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MkLoggbokComponent);
    component = fixture.componentInstance;
    component.loggbok = MkMockLoggbok;
    component.title = "Titel";
    fixture.detectChanges();
    datePipe = new DatePipe("sv-SE");    
  });

  it("ska skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska visa titel för logghändelser", () => {
    const items = queryAllBySelector(fixture, ".item");
    items.forEach((item, index) => {
      const title = item.query(By.css(".title")).nativeElement.textContent;
      expect(title).toEqual(component.loggbok[index].title);
    });    
  });

  it("Ska visa text för logghändelser", () => {
    const items = queryAllBySelector(fixture, ".item");
    items.forEach((item, index) => {
      const text = item.query(By.css(".text")).nativeElement.textContent;
      expect(text).toEqual(component.loggbok[index].text);
    });    
  });

  it("Ska visa skapat datum i svenskt format för logghändelser", () => {
    const items = queryAllBySelector(fixture, ".item");
    items.forEach((item, index) => {
      const date = item.query(By.css(".created-date")).nativeElement.textContent;
      expect(date).toEqual(datePipe.transform(component.loggbok[index].createdDate, "short"));
    });    
  });

  it("Ska visa alternativ text när det inte finns några logghändelser", () => {
    component.loggbok = null;
    fixture.detectChanges();
    const emptyElement = queryBySelector(fixture, ".empty-text");
  
    expect(emptyElement).toBeTruthy();
  });

  it("Ska visa alternativ text när input för loggbok är null", () => {
    component.loggbok = null;
    fixture.detectChanges();
    const emptyElement = queryBySelector(fixture, ".empty-text");

    expect(emptyElement).toBeTruthy();
  });

  it("Ska skicka event vid klick på Visa fler", () => {
    const spy = jest.spyOn(component.onShowMore, "emit");
    clickButtonBySelector(fixture, ".show-more-button");
    expect(spy).toHaveBeenCalledTimes(1);
  });
});
