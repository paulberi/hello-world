import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { XpTranslocoTestModule } from "../../../../../../../../../../../lib/translate/translocoTest.module.translate";
import { PolygonComponent } from "./polygon.component";
import { MMPolygonDropzoneModule } from "../../../../../../../../shared/ui/polygon-dropzone/polygon-dropzone.module";

describe("PolygonComponent", () => {
  let fixture: ComponentFixture<PolygonComponent>;
  let component: PolygonComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PolygonComponent],
      imports: [
        ReactiveFormsModule,
        MMPolygonDropzoneModule,
        XpTranslocoTestModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PolygonComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
