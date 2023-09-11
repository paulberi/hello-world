import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { XpTranslocoTestModule } from "../../../../../../../../../../../lib/translate/translocoTest.module.translate";
import { RectangleComponent } from "./rectangle.component";
import { RectanglePresenter, RectangleFormValue } from "./rectangle.presenter";

describe("RectangleComponent", () => {
  function testRectangleFormData(nMin: number): RectangleFormValue {
    return {
      nMin,
      nMax: 7200000,
      eMin: 600000,
      eMax: 610000
    }
  };

  let fixture: ComponentFixture<RectangleComponent>;
  let component: RectangleComponent;
  const presenter = new RectanglePresenter();

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RectangleComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        XpTranslocoTestModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RectangleComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska godta ett ifyllt och giltigt formulär", () => {
    // Given
    presenter.initializeForm(component.formControls);

    // When
    presenter.rectangleForm.setValue(testRectangleFormData(7100000));

    // Then
    expect(presenter.rectangleForm.valid).toBeTruthy();
  });

  it("Ska inte godkänna ett formulär med felaktiga inputs", () => {
    //Given
    presenter.initializeForm(component.formControls);

    //When
    presenter.rectangleForm.setValue(testRectangleFormData(7300000));

    //Then
    expect(presenter.rectangleForm.invalid).toBeTruthy();
  });

  it("Ska sätta errors till null om nMin är mindre än nMax", () => {
    // Given
    presenter.initializeForm(component.formControls);

    // When
    presenter.rectangleForm.setValue(testRectangleFormData(7100000));

    // Then
    expect(presenter.rectangleForm.get("nMin").errors).toBeNull();
  });

  it("Ska sätta error minGreaterThanMax till true om nMin är större än nMax", () => {
    // Given
    presenter.initializeForm(component.formControls);

    // When
    presenter.rectangleForm.setValue(testRectangleFormData(7300000));

    // Then
    expect(presenter.rectangleForm.get("nMin").errors.minGreaterThanMax).toBeTruthy();
  });
});
