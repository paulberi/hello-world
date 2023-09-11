import { Paragraph, TableCell } from "docx";
import { ITableCellPropertiesOptions } from "docx/build/file/table/table-cell/table-cell-properties";

export interface DocxTableCellChild {
  toDocx();
}

export class DocxTableCellBuilder {
  private children: (Paragraph | DocxTableCellChild)[];
  private properties: ITableCellPropertiesOptions;

  constructor() {
    this.children = [];
  }

  setProperties(properties: ITableCellPropertiesOptions): DocxTableCellBuilder {
    this.properties = properties;
    return this;
  }

  getProperties(): ITableCellPropertiesOptions {
    return this.properties;
  }

  addParagraph(text: string): DocxTableCellBuilder {
    this.children.push(new Paragraph({ text }));
    return this;
  }

  addTable(docxTableBuilder: DocxTableCellChild): DocxTableCellBuilder {
    this.children.push(docxTableBuilder);
    return this;
  }

  getChild(child: number) {
    return this.children[child];
  }

  toDocx() {
    return new TableCell({
      children: this.children.map((child) => {
        if ("toDocx" in child) {
          return child.toDocx();
        } else {
          return child;
        }
      }),
      ...this.properties,
    });
  }
}
