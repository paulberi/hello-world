import { BehaviorSubject } from "rxjs";
import { Property } from "../../../../../shared/utils/property-utils";
import { DynamicFormPresenter } from "./dynamic-form.presenter";

describe("DynamicFormPresenter", () => {
  const presenter = new DynamicFormPresenter();

  it("Ska sätta värde till 'false' om type är boolean", async () => {
    //Given
    const booleanProperty: Property<string>[] = [
      {
        "key": "booleanTest",
        "title": "BooleanTest",
        "description": "BooleanTest",
        "type": "boolean",
      }
    ]

    //When  
    await presenter.initializeForm(booleanProperty, new BehaviorSubject([]))

    //Then
    const form = presenter.dynamicForm;
    expect(form.get("booleanTest").value).toBe(false);
  });

  it("Ska sätta ett värde om property har const", async () => {
    //Given
    const constProperty: Property<string>[] = [
      {
        "key": "constTest",
        "title": "ConstTest",
        "description": "ConstTest",
        "const": "Const Test"
      }
    ]

    //When  
    await presenter.initializeForm(constProperty, new BehaviorSubject([]))

    //Then
    const form = presenter.dynamicForm;
    expect(form.get("constTest").value).toBe("Const Test");
  });
});