import { ComponentFixture, TestBed } from "@angular/core/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { clickButtonBySelector, setInputValueBySelector } from "../../../markkoll/src/test/jest-util";
import { XpUiSearchFieldComponent } from "./search-field.component";
import { XpUiSearchFieldModule } from "./search-field.module";

const SEARCH_FIELD_SELECTOR = "#search";
const RESET_BUTTON_SELECTOR = "#reset-button";

describe("SearchFieldComponent", () => {
  let fixture: ComponentFixture<XpUiSearchFieldComponent>;
  let component: XpUiSearchFieldComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ XpUiSearchFieldModule, NoopAnimationsModule ]
    }).compileComponents();

    fixture = TestBed.createComponent(XpUiSearchFieldComponent);
    component = fixture.componentInstance;
    component.debounceTime = 0;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("Ska emitta event när man skriver in ett värde i sökfältet", async () => {
    // Given
    const value = "test";

    const onChange = { fn: jest.fn() };
    const spy = jest.spyOn(onChange, "fn");
    component.registerOnChange(onChange.fn);
    fixture.detectChanges();

    // When
    setInputValueBySelector(fixture, value, SEARCH_FIELD_SELECTOR);
    await fixture.whenStable();

    // Then
    expect(spy).toHaveBeenCalledWith(value);
  });

  it("Så ska tomt värde emittas när man trycker på Reset", async () => {
    // Given
    setInputValueBySelector(fixture, "något värde", SEARCH_FIELD_SELECTOR);
    await fixture.whenStable();
    expect(component.isEmpty()).toBeFalsy();

    const onChange = { fn: jest.fn() };
    const spy = jest.spyOn(onChange, "fn");
    component.registerOnChange(onChange.fn);
    fixture.detectChanges();

    // When
    clickButtonBySelector(fixture, RESET_BUTTON_SELECTOR);
    await fixture.whenStable();

    // Then
    expect(spy).toHaveBeenCalledWith("");
  });
});
