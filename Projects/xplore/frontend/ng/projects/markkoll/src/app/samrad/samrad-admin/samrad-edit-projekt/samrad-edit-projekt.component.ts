import { Component, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Editor, schema, Toolbar } from "ngx-editor";
import {
  FilRsp as Fil,
  ProjektRsp as Projekt,
} from "../../../../../../../generated/samrad-api";
import { DialogService } from "../../../services/dialog.service";
import { SamradService } from "../../../services/samrad/samrad.service";
import { SamradProjektinformationPresenter } from "./samrad-edit.presenter";
import { EditorView } from "prosemirror-view";

@Component({
  selector: "mk-samrad-edit-projekt",
  providers: [SamradProjektinformationPresenter],
  templateUrl: "./samrad-edit-projekt.component.html",
  styleUrls: ["./samrad-edit-projekt.component.scss"],
})
export class SamradEditProjektComponent implements OnInit, OnDestroy {
  constructor(
    private samradService: SamradService,
    private presenter: SamradProjektinformationPresenter,
    private dialogService: DialogService
  ) {}

  @Input() samradProjekt: Projekt;
  @Input() markkollProjekt: any;
  @Input() kundId: string;

  @Output() updateProjekt = this.presenter.updateProjekt;
  @Output() hasUnsavedChanges = this.presenter.canSave;

  images: Fil[];
  selectImageIsActive = false;

  editor: Editor;
  toolbar: Toolbar = [
    ["bold", "italic"],
    ["underline", "strike"],
    ["blockquote"],
    ["ordered_list", "bullet_list"],
    [{ heading: ["h1", "h2", "h3", "h4", "h5", "h6"] }],
    ["text_color"],
    ["align_left", "align_center", "align_right", "align_justify"],
  ];

  get form() {
    return this.presenter.form;
  }

  toggleSelectImage(value: boolean) {
    this.selectImageIsActive = value;
  }

  ngOnInit(): void {
    this.editor = new Editor();
    this.presenter.initializeForm(this.samradProjekt);

    if(this.samradProjekt) {
      this.samradService
      .listSamradFiles(this.kundId, this.samradProjekt.id)
      .subscribe(
        (files) =>
          (this.images = files.filter((files) =>
            files.mimetyp.includes("image")
          ))
      );
    }

  }

  uploadImage(file: Fil) {
    const url = `/samrad/api/admin/kunder/${this.kundId}/projekt/${this.samradProjekt.id}/filer/${file.id}/innehall`;
    this.startImageUpload(this.editor.view, url);
  }

  startImageUpload(view: EditorView, url: string) {
    const tr = view.state.tr;
    const pos = tr.selection.from;

    view.dispatch(
      tr.replaceWith(pos, pos, schema.nodes.image.create({ src: url }))
    );
  }

  onSlugInformation(): void {
    this.dialogService.slugInformationDialog().subscribe();
  }

  ngOnDestroy(): void {
    this.editor.destroy();
  }
}
