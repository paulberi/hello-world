import { Directive, HostListener, HostBinding, Output, EventEmitter } from "@angular/core";

@Directive({
  selector: "[mhDnd]"
})
export class DndDirective {
    @HostBinding("class.fileover") fileOver: boolean;
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
