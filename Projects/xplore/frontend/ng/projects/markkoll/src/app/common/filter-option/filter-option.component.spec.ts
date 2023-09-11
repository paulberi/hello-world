import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatOption, MatOptionModule } from "@angular/material/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelect, MatSelectModule } from "@angular/material/select";
import { By } from "@angular/platform-browser";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { MkFilterOptionComponent, OptionItem } from "./filter-option.component";
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";
import { clickButtonBySelector, queryBySelector, setMatSelectValue } from "../../../test/jest-util";

describe("MkFilterOptionComponent", () => {
  let component: MkFilterOptionComponent;
  let fixture: ComponentFixture<MkFilterOptionComponent>;
  let mockOptionItems: OptionItem[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkFilterOptionComponent ],
      imports: [
        MatFormFieldModule,
        ReactiveFormsModule,
        MatOptionModule,
        MatSelectModule,
        NoopAnimationsModule,
        XpTranslocoTestModule,
      ]
    })
    .compileComponents();
  });

  beforeEach(async () => {
    fixture = TestBed.createComponent(MkFilterOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    mockOptionItems = [
      { value: "value1", label: "label1"},
      { value: "value2", label: "label2"},
      { value: "value3", label: "label3"}];

    // Måste trigga MatSelect för att komma åt options
    await clickButtonBySelector(fixture, ".mat-select-trigger");

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it ("ska kunna innehålla en array med options", async () => {
    // When
    component.options = mockOptionItems;

    fixture.detectChanges();
    await fixture.whenStable();

    const options = fixture.debugElement.queryAll(By.directive(MatOption));

    // Then
    expect(options.length).toBe(4);
    mockOptionItems.forEach(
      v => expect(options.map(o => o.attributes["ng-reflect-value"]).includes(v.value)).toBeTruthy());
  });

  it("ska ha ett tomt val", () => {
    const options = fixture.debugElement.queryAll(By.directive(MatOption));

    expect(options.length).toBe(1);
    expect(options.map(o => o.attributes["ng-reflect-value"]).includes("")).toBeTruthy();
  });

  it("ska emitta ett valt värde", async () => {
    const matSelect = queryBySelector(fixture, "mat-select");

    component.options = mockOptionItems;

    fixture.detectChanges();
    await fixture.whenStable();

    await setMatSelectValue(fixture, matSelect.nativeElement, "label2");

    expect((matSelect.context as MatSelect).value).toEqual("value2");
  });
});
