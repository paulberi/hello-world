import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { XpTranslocoTestModule } from "../../../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../../../lib/ui/dropzone/dropzone.module";
import { Property } from "../../../../../shared/utils/property-utils";
import { DropzoneComponent } from "./complex-datatypes/dropzone/dropzone.component";
import { ObjectComponent } from "./complex-datatypes/object/object.component";
import { PolygonComponent } from "./complex-datatypes/object/polygon/polygon.component";
import { RectangleComponent } from "./complex-datatypes/object/rectangle/rectangle.component";
import { DynamicFormComponent } from "./dynamic-form.component";

describe("DynamicFormComponent", () => {
  let fixture: ComponentFixture<DynamicFormComponent>;
  let component: DynamicFormComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        DynamicFormComponent,
        DropzoneComponent,
        ObjectComponent,
        PolygonComponent,
        RectangleComponent
      ],
      imports: [
        ReactiveFormsModule,
        MatCheckboxModule,
        MatFormFieldModule,
        MatIconModule,
        MatListModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatSelectModule,
        XpDropzoneModule,
        XpTranslocoTestModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DynamicFormComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska konvertera JSONType: 'string' till InputType: 'text'", () => {
    //Given
    const testProperty: Property<string> = {
      title: "Test",
      key: "test",
      type: "string"
    };

    //When
    const convertInputType = component.convertToInputType(testProperty)

    //Then
    expect(convertInputType).toEqual("text");
  });
});
