import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpTranslocoTestModule } from "../../../../../../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../../../../../../lib/ui/dropzone/dropzone.module";
import { createMockFileList } from "../../../../../../../../../test/data";
import { Property } from "../../../../../../../../shared/utils/property-utils";
import { DropzoneComponent } from "../../dropzone/dropzone.component";
import { ErrorMessage, KartbladComponent } from "./kartblad.component";

describe("KartbladComponent", () => {
  const testForm: FormGroup = new FormGroup({
    dropzone: new FormControl("")
  });

  const testProperty: Property<string> = {
    key: "dropzone",
    title: "dropzone",
    type: "object"
  };

  let fixture: ComponentFixture<KartbladComponent>;
  let component: KartbladComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KartbladComponent, DropzoneComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatIconModule,
        MatProgressSpinnerModule,
        XpDropzoneModule,
        XpTranslocoTestModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(KartbladComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska returnera true vid validering av giltiga kartblad", () => {
    //Given
    const validKartblad = ["1330_6733_195 ", "", "1330_6733142_195504"]

    //When
    const result = component.isValid(validKartblad);

    //Then
    expect(result).toEqual(true);
  });

  it("Ska returnera false vid validering av ogiltiga kartblad", () => {
    //Given
    const unValidKartblad = ["1330_6733_195 1330_6733_195", "", "1330_6733142_195504"]

    //When
    const result = component.isValid(unValidKartblad);

    //Then
    expect(result).toEqual(false);
  });


  it("Ska visa felmeddelande för ogiltiga kartblad", () => {
    //Given
    component.dynamicForm = testForm;
    component.property = testProperty;

    //When
    component.readKartblad("1330_6733_195\n1330_6733142_195504 1330_6733142_195504\n1330_6733142_195504")

    //Then
    expect(component.conversionErrorMsg).toEqual(ErrorMessage.UNVALID_FILE);
  });

  it("Ska visa felmeddelande för tom fil", () => {
    //Given
    component.dynamicForm = testForm;
    component.property = testProperty;
    const mockedFileList = createMockFileList([
      {
        body: '',
        mimeType: 'text/plain',
        name: 'test.txt'
      }
    ]);

    //When
    component.onChanges(mockedFileList)

    //Then
    expect(component.conversionErrorMsg).toEqual(ErrorMessage.EMPTY_FILE);
  });

  it("Ska visa felmeddelande för felaktig fil", () => {
    //Given
    component.dynamicForm = testForm;
    component.property = testProperty;

    const unValidFile = ["123_132", "123_123_123"]

    //When
    component.readFile(unValidFile as unknown as File)

    //Then
    expect(component.conversionErrorMsg).toEqual(ErrorMessage.NOT_READABLE_FILE);
  });
});
