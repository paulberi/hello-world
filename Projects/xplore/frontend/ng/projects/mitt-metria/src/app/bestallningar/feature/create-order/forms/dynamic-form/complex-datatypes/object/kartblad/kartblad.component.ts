import { Component, Input } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { readFileContentAsText } from "../../../../../../../../shared/utils/file-utils";
import { Property } from "../../../../../../../../shared/utils/property-utils";

export enum ErrorMessage {
  UNVALID_FILE = "Filen är ogilitig, vänligen ladda upp en ny. Den får bara innehålla ett kartbladsnamn per rad",
  NOT_READABLE_FILE = "Kunde inte läsa in filen, vänligen ladda upp en ny.",
  EMPTY_FILE = "Filen är tom, vänligen ladda upp en ny.",
}

@Component({
  selector: "mm-kartblad",
  templateUrl: "./kartblad.component.html",
  styleUrls: ["./kartblad.component.scss"]
})
export class KartbladComponent {
  @Input() dynamicForm: UntypedFormGroup;
  @Input() property: Property<string>;
  isLoading: boolean;
  conversionError: boolean;
  conversionErrorMsg: string;
  fileUploaded = false;
  fileName: string;
  dropzoneLabel = "Ladda upp en textfil (.txt)"

  constructor() { }

  onChanges(files: FileList) {
    this.conversionError = false;
    if (files && files[0]?.type === "text/plain") {
      this.isLoading = true;
      if (files[0]?.name) {
        this.fileName = files[0].name;
      }
      if (files[0]?.size) {
        this.readFile(files[0])
      } else {
        this.isLoading = false;
        this.conversionError = this.dropzoneErrors(ErrorMessage.EMPTY_FILE)
      }
    }
  }

  readFile(file: File) {
    readFileContentAsText(file)?.subscribe(text => {
      if (text) {
        this.readKartblad(text)
      }
    }, error => {
      this.isLoading = false;
      console.error("Could not read file. Message:", error?.message)
      this.conversionError = this.dropzoneErrors(ErrorMessage.NOT_READABLE_FILE)
    });
  }

  readKartblad(kartblad: string) {
    this.isLoading = false;
    const kartbladArray = kartblad.split(/\r?\n/) || [];
    if (this.isValid(kartbladArray)) {
      const kartbladWithoutSpaces = kartbladArray.filter(item => item)?.map(item => item.replace(" ", "")) || [];
      this.fileUploaded = true;
      this.dynamicForm.get(this.property.key).setValue(kartbladWithoutSpaces);
    } else {
      this.conversionError = this.dropzoneErrors(ErrorMessage.UNVALID_FILE)
    }
  }

  isValid(kartblad: string[]): boolean {
    if (kartblad) {
      const withoutEmptyRows = kartblad.filter(item => item);
      let valid: boolean = true;
      withoutEmptyRows.forEach(element => {
        if (valid) {
          if (element.endsWith(" ")) {
            const found = element.match(/[" "]/);
            if (found?.length < 2) {
              const noSpace = element.replace(" ", "")
              valid = (/^(\d)+(\_)+[\d_]+$/).test(noSpace)
            } else {
              return false;
            }
          } else {
            valid = (/^(\d)+(\_)+[\d_]+$/).test(element)
          }
        }
      })
      return valid;
    } else {
      return false;
    }
  }

  changeFile() {
    this.fileUploaded = false;
    this.dynamicForm.get(this.property.key).reset();
  }

  dropzoneErrors(errorMessage: ErrorMessage): boolean {
    const formControl = this.dynamicForm.get(this.property.key);
    formControl ? formControl.reset() : null;
    this.conversionErrorMsg = errorMessage;
    return true;
  }
}
