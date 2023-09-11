import { ISectionPropertiesOptions } from "docx";

export class DocxSectionBuilder {
  private properties: ISectionPropertiesOptions;
  private children;

  constructor() {
    this.children = [];
  }

  addChild(child): DocxSectionBuilder {
    this.children.push(child);
    return this;
  }

  getChild(child: number) {
    return this.children[child];
  }

  setProperties(properties: ISectionPropertiesOptions): DocxSectionBuilder {
    this.properties = properties;
    return this;
  }

  toDocx() {
    return {
      properties: this.properties,
      children: this.children.map((child) => child.toDocx()),
    };
  }
}
