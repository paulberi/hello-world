import { Component, ElementRef, forwardRef, Input, OnInit, ViewChild } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";

/**
 * Komponent som tillåter uppladdning av filer, antingen genom att klicka på den, eller med ett
 * drag-and-drop-interface.
 */
@Component({
  selector: "xp-dropzone",
  templateUrl: "./dropzone.component.html",
  styleUrls: ["./dropzone.component.scss"],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => XpDropzoneComponent),
      multi: true
    }
  ]
})


export class XpDropzoneComponent implements ControlValueAccessor {

  constructor(private translateService: TranslocoService) { }

  get accept(): String {
    return this.acceptedFileEndings?.join();
  }

  @ViewChild("fileUpload") fileUpload: ElementRef;

  /**
   * De filändelser som komponenten ska acceptera. Ex. ".zip" eller ".docx".
   * Som standard tillåts alla filändelser.
   */
  @Input() acceptedFileEndings: String[] = [];

  /**
  * Om det går att ladda upp flera filer eller bara en åt gången.
  */
  @Input() isMultipleFilesAllowed = false;

  /**
   * Etikett som ska synas om inga filer är valda.
   */
  @Input() label = this.translateService.translate("xp.dropzone.label");

  files: File[] = [];
  isDisabled = false;
  onChange: any = () => { };
  onTouch: any = () => { };
  hasRejectedFiles = false;

  writeValue(obj: any): void {
    if (obj) {
      this.files = obj;
    } else {
      this.files = [];
      if(this.fileUpload?.nativeElement) {
        this.fileUpload.nativeElement.value = "";
      }
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  dropFiles(files: FileList) {
    this.hasRejectedFiles = false;
    const uploadedFiles = Array.from(files);

    if (this.isMultipleFilesAllowed) {
      this.files = uploadedFiles.filter((file) => this.isAcceptedFileEnding(file));
    } else if (!this.isMultipleFilesAllowed) {
      // Om endast en fil tillåts för uppladdning så behåll den första och kasta bort de andra
      if (this.isAcceptedFileEnding(uploadedFiles[0])) {
        this.files = [uploadedFiles[0]];
      }
    }

    const rejectedFiles = uploadedFiles.filter((file) => !this.isAcceptedFileEnding(file));
    if (rejectedFiles.length > 0) {
      this.hasRejectedFiles = true;
    }

    this.onChange(this.files);
  }

  private isAcceptedFileEnding(file: File) {
    const fileEnding = file?.name.match(/\.[0-9a-z]+$/i)[0].toLowerCase();
    return this.acceptedFileEndings.length > 0 ? this.acceptedFileEndings.some(x => x.toLowerCase() == fileEnding) : true;
  }
}
