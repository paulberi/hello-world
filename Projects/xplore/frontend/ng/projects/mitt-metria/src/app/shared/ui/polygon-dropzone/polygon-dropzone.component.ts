import { Component, forwardRef, Input, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { convertShapeFileToGeoJSON, readFileContent } from '../../utils/file-utils';

enum ErrorMessage {
  MULTIPLE_FEATURECOLLECTIONS = "Filen innehåller flera listor av geometrier. Vänligen ladda upp en shapefil med en Polygon.",
  MULTIPLE_GEOMETRIES = "Filen innehåller flera geometrier. Vänligen ladda upp en shapefil med en Polygon.",
  NO_GEOMETRIES_FOUND = "Inga geometrier hittades. Vänligen ladda upp en annan shapefil.",
  NOT_POLYGON = "Geometrin är inte av typen Polygon. Vänligen ladda upp en shapefil med en Polygon."
}

@Component({
  selector: 'mm-polygon-dropzone',
  templateUrl: './polygon-dropzone.component.html',
  styleUrls: ['./polygon-dropzone.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PolygonDropzoneComponent),
      multi: true
    }
  ]
})
export class PolygonDropzoneComponent implements ControlValueAccessor {
  @ViewChild("fileUpload") fileUpload;

  @Input() label: string;
  @Input() acceptedFileEndings: string[] = [".zip"];
  @Input() isMultipleFilesAllowed: boolean = false;

  geojson: any;
  onChange: any = () => { };
  onTouch: any = () => { };
  isLoading: boolean;
  conversionError: boolean;
  conversionErrorMessage: string;
  fileUploaded: boolean = false;
  fileName: string;

  constructor() { }

  writeValue(obj: any): void {
    if (obj) {
      this.fileUploaded = true;
      this.geojson = obj;
      this.fileName = this.geojson.properties.fileName;
    } else {
      this.fileUploaded = false;
      this.geojson = null;
      this.fileName = null;
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  dropFiles(files: FileList) {
    if (files) {
      if (files[0]?.name) {
        this.fileName = files[0]?.name;
      }
      this.conversionError = false;
      readFileContent(files[0])?.subscribe(fileContent => {
        this.isLoading = true;
        convertShapeFileToGeoJSON(fileContent).then((res) => {
          if (res) {
            this.isLoading = false;
            if (res?.length > 1) {
              this.conversionError = this.dropzoneErrors(ErrorMessage.MULTIPLE_FEATURECOLLECTIONS)
            } else {
              res?.features?.length > 1 ? this.conversionError = this.dropzoneErrors(ErrorMessage.MULTIPLE_GEOMETRIES) :
                this.checkIfPolygon(res);
            }
          }
        }, error => {
          this.isLoading = false;
          this.conversionError = this.dropzoneErrors(ErrorMessage.NO_GEOMETRIES_FOUND);
        })
      })
    }
  }

  changeFile() {
    this.fileUploaded = false;
    this.onChange(null);
  }

  checkIfPolygon(geojson: any) {
    if (geojson.features?.length) {
      if (geojson.features[0].geometry.type === "Polygon") {
        this.fileUploaded = true;
        this.conversionError = false;

        this.geojson = geojson?.features[0];
        this.geojson.properties["fileName"] = this.fileName;
        this.onChange(this.geojson);
      } else {
        this.conversionError = this.dropzoneErrors(ErrorMessage.NOT_POLYGON);
      }
    } else {
      this.conversionError = this.dropzoneErrors(ErrorMessage.NO_GEOMETRIES_FOUND);
    }
  }

  dropzoneErrors(errorMessage: ErrorMessage): boolean {
    this.fileUpload.files = []
    this.conversionErrorMessage = errorMessage;
    return true;
  }
}