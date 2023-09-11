import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { queryBySelector, setInputValueBySelector, wait } from "../../../test/jest-util";

import { MkSearchInputComponent } from "./search-input.component";

describe("MkSearchInputComponent", () => {
  let component: MkSearchInputComponent;
  let fixture: ComponentFixture<MkSearchInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MkSearchInputComponent
      ],
      imports: [
        MatFormFieldModule,
        FormsModule,
        ReactiveFormsModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MkSearchInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("ska gå att sätta en placeholdertext", async () => {
    component.placeholder = "placeholder";

    fixture.detectChanges();
    await fixture.whenStable();

    expect(queryBySelector(fixture, "label span").nativeElement.textContent).toEqual("placeholder");
  });

  it("Så ska värdet i sökfältet emittas efter en viss tid", async () => {
    // Given
    const spy = jest.spyOn(component.searchChange, "emit");
    const inputValue = "hej!";

    // When
    setInputValueBySelector(fixture, inputValue, "input");

    fixture.detectChanges();
    expect(spy).toHaveBeenCalledTimes(0);

    await wait(component.debounceTime);

    fixture.detectChanges();

    // Then
    expect(spy).toHaveBeenCalledTimes(1);
    expect(spy).toHaveBeenCalledWith(inputValue);
  });
});
