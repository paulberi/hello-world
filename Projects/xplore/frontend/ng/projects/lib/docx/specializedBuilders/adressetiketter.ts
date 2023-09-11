import {
  BorderStyle,
  convertInchesToTwip,
  TableLayoutType,
  WidthType,
} from "docx";
import { DocxBuilder } from "../docxBuilder";
import { DocxSectionBuilder } from "../docxSectionBuilder";
import { DocxTableBuilder } from "../docxTableBuilder";
import { DocxTableCellBuilder } from "../docxTableCellBuilder";

const borders = {
  top: {
    style: BorderStyle.DASH_SMALL_GAP,
    size: 1,
    color: "ffffff",
  },
  bottom: {
    style: BorderStyle.DASH_SMALL_GAP,
    size: 1,
    color: "ffffff",
  },
  left: {
    style: BorderStyle.DASH_SMALL_GAP,
    size: 1,
    color: "ffffff",
  },
  right: {
    style: BorderStyle.DASH_SMALL_GAP,
    size: 1,
    color: "ffffff",
  },
};

const tableOptions = {
  borders,
  width: {
    size: 11454,
    type: WidthType.DXA,
  },
  layout: TableLayoutType.FIXED,
};

const fastighetOptions = {
  borders: borders,
  margins: {
    top: convertInchesToTwip(0.18),
    bottom: convertInchesToTwip(0.18),
    right: convertInchesToTwip(0),
    left: convertInchesToTwip(0.15),
  },
  width: {
    size: 3818,
    type: WidthType.DXA,
  }
};

const adressOptions = {
  borders,
  margins: {
    top: convertInchesToTwip(0.01),
    bottom: convertInchesToTwip(0),
    right: convertInchesToTwip(0),
    left: convertInchesToTwip(0.15),
  }
};

const seperatorOptions = {
  borders: borders,
  margins: {
    top: convertInchesToTwip(0.09),
    bottom: convertInchesToTwip(0),
    right: convertInchesToTwip(0.15),
    left: convertInchesToTwip(0),
  },
};

const pageOptions = {
  margin: {
    top: convertInchesToTwip(0.04),
      right: 0,
      bottom: 0,
      left: convertInchesToTwip(0.2),
  }
};

const styleOptions = {
  paragraphStyles: [
    {
      name: "Normal",
      run: {
        size: 24
      }
    }
  ],
};

export class AdressEtiketter {
  private numberOfAdresses = 0;
  private docxTableBuilder: DocxTableBuilder;

  constructor() {
    this.docxTableBuilder = new DocxTableBuilder()
      .setProperties(tableOptions)
      .setRowProperties({ cantSplit: true });
  }

  addAdress(
    fastighet: string,
    namn: string,
    adress: string,
    postnummer: string,
    postort: string
  ) {
    this.docxTableBuilder.addCell(
      new DocxTableCellBuilder().addTable(
        new DocxTableBuilder()
          .setProperties({ borders })
          .addCell(
            new DocxTableCellBuilder()
              .addParagraph(fastighet)
              .setProperties(fastighetOptions),
            0
          )
          .addCell(
            new DocxTableCellBuilder()
              .addParagraph(namn)
              .setProperties(adressOptions),
            1)
          .addCell(
            new DocxTableCellBuilder()
              .addParagraph(adress)
              .setProperties(adressOptions),
          2)
          .addCell(
            new DocxTableCellBuilder()
              .addParagraph(`${postnummer} ${postort}`)
              .setProperties(adressOptions),
            3
          )
          .addCell(
            new DocxTableCellBuilder().setProperties(seperatorOptions),
            4
          )
      ),
      Math.floor(this.numberOfAdresses / 3),
      this.numberOfAdresses % 3
    );

    this.numberOfAdresses++;
  }

  save() {

    // Fyll ut med tomma poster så vi alltid har minst tre, annars påverkar det layouten.
    if (this.numberOfAdresses < 3) {
      for (let i = 0; i <= (3 - this.numberOfAdresses); i++) {
        this.addAdress("", "", "", "", "");
      }
    }

    const docSectionBuilder = new DocxSectionBuilder();
    docSectionBuilder.setProperties({
      page: pageOptions
    });
    docSectionBuilder.addChild(this.docxTableBuilder);
    const doc = new DocxBuilder({
      styles: styleOptions
    }).addSection(
      docSectionBuilder
    );

    doc.saveDocument("Adressetikett.docx");
  }
}
