import { Directive, HostListener, HostBinding, Output, EventEmitter } from "@angular/core";

/**
 * Direktiv som möjliggör ett drag-and-drop-interface av föremål, för t.ex. filuppladdning.
 */
@Directive({
  selector: "[xpDragNDrop]"
})
export class XpDragAndDropDirective {
  @HostBinding("class.fileover") fileOver: boolean;

  /**
   * Händelse som meddelar att man har släppt en fil över komponenten.
   */
  @Output() fileDropped = new EventEmitter<any>();

  constructor() { }

  @HostListener("dragover", ["$event"]) onDragOver(evt) {
    evt.preventDefault();
    evt.stopPropagation();
    this.fileOver = true;
  }

  @HostListener("dragleave", ["$event"]) public onDragLeave(evt) {
    evt.preventDefault();
    evt.stopPropagation();
    this.fileOver = false;
  }

  @HostListener("drop", ["$event"]) public ondrop(evt) {
    evt.preventDefault();
    evt.stopPropagation();
    this.fileOver = false;
    const files = evt.dataTransfer.files;
    if (files.length > 0) {
      this.fileDropped.emit(files);
    }
  }
}
