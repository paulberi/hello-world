import { ComponentFixture, TestBed } from '@angular/core/testing';
import { XpTranslocoTestModule } from '../../../../../../lib/translate/translocoTest.module.translate';
import { testInvalidGeometry, testNoGeometry, testValidGeometry } from '../../../../test/data';
import { PolygonDropzoneComponent } from './polygon-dropzone.component';
import { MMPolygonDropzoneModule } from './polygon-dropzone.module';

describe('PolygonDropzoneComponent', () => {  
  let component: PolygonDropzoneComponent;
  let fixture: ComponentFixture<PolygonDropzoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PolygonDropzoneComponent ],
      imports: [
        MMPolygonDropzoneModule,
        XpTranslocoTestModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PolygonDropzoneComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska ej visa felmeddelande ifall geometrin är polygon", () => {
    //Given
    component.fileUpload = {
      files: []
    }

    //When
    component.checkIfPolygon(testValidGeometry);

    //Then
    expect(component.conversionError).toEqual(false);
  });

  it("Ska visa felmeddelande för ogilitg geometri", () => {
    //Given
    component.fileUpload = {
      files: []
    }

    //When
    component.checkIfPolygon(testInvalidGeometry);

    //Then
    expect(component.conversionError).toEqual(true);
  });

  it("Ska visa felmeddelande ifall det inte finns geometri", () => {
    //Given
    component.fileUpload = {
      files: []
    }

    //When
    component.checkIfPolygon(testNoGeometry);

    //Then
    expect(component.conversionError).toEqual(true);
  });
});