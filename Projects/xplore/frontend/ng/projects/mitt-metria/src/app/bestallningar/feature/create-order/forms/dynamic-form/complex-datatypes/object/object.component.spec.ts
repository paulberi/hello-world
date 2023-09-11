import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpTranslocoTestModule } from "../../../../../../../../../../lib/translate/translocoTest.module.translate";
import { XpDropzoneModule } from "../../../../../../../../../../lib/ui/dropzone/dropzone.module";
import { DropzoneComponent } from "../dropzone/dropzone.component";
import { ObjectComponent } from "./object.component";
import { PolygonComponent } from "./polygon/polygon.component";
import { RectangleComponent } from "./rectangle/rectangle.component";

describe("ObjectComponent", () => {
  let fixture: ComponentFixture<ObjectComponent>;
  let component: ObjectComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        ObjectComponent,
        PolygonComponent,
        DropzoneComponent,
        RectangleComponent
      ],
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

    fixture = TestBed.createComponent(ObjectComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
