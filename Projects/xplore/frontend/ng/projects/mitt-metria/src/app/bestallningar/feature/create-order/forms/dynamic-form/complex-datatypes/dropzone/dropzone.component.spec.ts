import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { XpTranslocoTestModule } from "../../../../../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../../../../../lib/ui/dropzone/dropzone.module";
import { Property } from "../../../../../../../shared/utils/property-utils";
import { DropzoneComponent } from "./dropzone.component";

describe("DropzoneComponent", () => {
  const testForm: FormGroup = new FormGroup({
    dropzone: new FormControl("")
  });

  const testProperty: Property<string> = {
    key: "dropzone",
    title: "dropzone",
    type: "object"
  };

  const testFile = [{
    name: "test.zip",
    size: 2566,
    type: "application/zip"
  }];

  let component: DropzoneComponent;
  let fixture: ComponentFixture<DropzoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DropzoneComponent],
      imports: [
        ReactiveFormsModule,
        XpDropzoneModule,
        XpTranslocoTestModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DropzoneComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska emitta event vid ändringar i dropzone", () => {
    // Given
    component.dynamicForm = testForm;
    component.property = testProperty;
    const spy = jest.spyOn(component.onChanges, "emit");

    // When
    component.ngOnInit();
    component.dynamicForm.get("dropzone").setValue(testFile);

    // Then
    expect(spy).toHaveBeenCalledWith(testFile);
  });
});
