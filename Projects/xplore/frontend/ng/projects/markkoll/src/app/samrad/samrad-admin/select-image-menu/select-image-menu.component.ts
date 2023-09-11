import {
  Component,
  EventEmitter,
  Input,
  Output,
} from "@angular/core";
import { CommonModule } from "@angular/common";
import { Editor } from "ngx-editor";
import { MatIconModule } from "@angular/material/icon";

@Component({
  selector: "mk-select-image-menu",
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: "./select-image-menu.component.html",
  styleUrls: ["./select-image-menu.component.scss"],
})
export class SelectImageMenuComponent {
  constructor() {}

  @Input() editor: Editor;

  @Input() selectImageIsActive: boolean;

  @Output() toggle = new EventEmitter<boolean>();

  toggleModal() {
    this.toggle.emit(!this.selectImageIsActive);
  }
}
