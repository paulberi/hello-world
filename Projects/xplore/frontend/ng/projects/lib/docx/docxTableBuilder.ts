import {
  AlignmentType,
  ITableBordersOptions,
  ITableFloatOptions,
  ITableRowPropertiesOptions,
  ITableWidthProperties,
  Table,
  TableLayoutType,
  TableRow,
} from "docx";
import { ITableCellMarginOptions } from "docx/build/file/table/table-properties/table-cell-margin";
import {
  DocxTableCellBuilder,
  DocxTableCellChild,
} from "./docxTableCellBuilder";

interface ITableOptionsWithoutRows {
  readonly width?: ITableWidthProperties;
  readonly columnWidths?: number[];
  readonly margins?: ITableCellMarginOptions;
  readonly indent?: ITableWidthProperties;
  readonly float?: ITableFloatOptions;
  readonly layout?: TableLayoutType;
  readonly style?: string;
  readonly borders?: ITableBordersOptions;
  readonly alignment?: AlignmentType;
  readonly visuallyRightToLeft?: boolean;
}

export class DocxTableBuilder implements DocxTableCellChild {
  private rows: [DocxTableCellBuilder[]];
  private properties: ITableOptionsWithoutRows;
  private rowProperties: ITableRowPropertiesOptions;

  constructor() {
    this.rows = [[]];
  }

  addCell(
    cellBuilder: DocxTableCellBuilder,
    row: number,
    column: number = !!this.rows[row] ? this.rows[row].length : 0
  ): DocxTableBuilder {
    while (row > this.rows.length - 1) {
      this.rows.push([]);
    }

    while (column > this.rows[row].length - 1) {
      this.rows[row].push(
        new DocxTableCellBuilder().setProperties(this.properties)
      );
    }

    if (cellBuilder.getProperties() == null) {
      cellBuilder.setProperties(this.properties);
    }

    this.rows[row][column] = cellBuilder;

    return this;
  }

  getCell(row: number, column: number) {
    return this.rows[row][column];
  }

  setProperties(properties: ITableOptionsWithoutRows): DocxTableBuilder {
    this.properties = properties;
    return this;
  }

  getProperties(): ITableOptionsWithoutRows {
    return this.properties;
  }

  setRowProperties(properties: ITableRowPropertiesOptions): DocxTableBuilder {
    this.rowProperties = properties;
    return this;
  }

  getRowProperties(): ITableRowPropertiesOptions {
    return this.rowProperties;
  }

  toDocx() {
    return new Table({
      rows: this.rows.map(
        (row) =>
          new TableRow({
            children: row.map((cell) => cell.toDocx()),
            ...this.rowProperties,
          })
      ),
      ...this.properties,
    });
  }
}
