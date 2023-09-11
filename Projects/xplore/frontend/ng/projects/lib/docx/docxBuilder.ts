import {Document, IDocumentOptions, Packer} from "docx";
import * as fileSaver from "file-saver";
import { DocxSectionBuilder } from "./docxSectionBuilder";

export class DocxBuilder {
  private sections: DocxSectionBuilder[];
  private options?: IDocumentOptions;

  constructor(options?: any) {
    this.sections = [];
    this.options = options;
  }

  addSection(section: DocxSectionBuilder): DocxBuilder {
    this.sections.push(section);
    return this;
  }

  getSection(section: number): DocxSectionBuilder {
    return this.sections[section];
  }

  saveDocument(fileName: string) {
    const doc = new Document({
      ...this.options,
      sections: this.sections.map((section) => section.toDocx()),
    });

    Packer.toBlob(doc).then((buffer) => {
      fileSaver.saveAs(buffer, fileName);
    });
  }
}
