import { action } from "@storybook/addon-actions";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { Projekthandelse, ProjektLoggType, ProjekthandelseTyp, DokumentTyp, Infobrevhandelse, Avtalhandelse } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkProjektloggComponent } from "../projektlogg/projektlogg.component";
import { MkProjektloggTabComponent } from "./projektlogg-tab.component";
import { MkProjektloggTabModule } from "./projektlogg-tab.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektlogg Tab",
  component: MkProjektloggTabComponent,
  decorators: [
    moduleMetadata({
      imports: [MkProjektloggTabModule, getTranslocoModule()],
    }),
  ],
} as Meta;

const Template: StoryFn<MkProjektloggComponent> = (args: MkProjektloggComponent) => ({
  props: {
    ...args,
    filDownload: action("filDownload"),
    filterChange: action("filterChange"),
    pageChange: action("pageChange"),
    sortDirectionChange: action("sortDirectionChange")
  }
});

const loggVersionAterstalld: Projekthandelse = {
  projektLoggType: ProjektLoggType.PROJEKTHANDELSE,
  projekthandelseTyp: ProjekthandelseTyp.VERSIONATERSTALLD,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
};

const loggVersionBorttagen: Projekthandelse = {
  projektLoggType: ProjektLoggType.PROJEKTHANDELSE,
  projekthandelseTyp: ProjekthandelseTyp.VERSIONBORTTAGEN,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
};

const loggVersionImporterad: Projekthandelse = {
  projektLoggType: ProjektLoggType.PROJEKTHANDELSE,
  projekthandelseTyp: ProjekthandelseTyp.VERSIONIMPORTERAD,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
};

const loggInfobrev: Infobrevhandelse = {
  projektLoggType: ProjektLoggType.INFOBREVHANDELSE,
  infobrevsjobbId: "123",
  antalFastigheter: 5,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
}

const loggMarkupplatelseavtal: Avtalhandelse = {
  projektLoggType: ProjektLoggType.AVTALHANDELSE,
  avtalsjobbId: "123",
  antalFastigheter: null,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
}

export const projektloggTab = Template.bind({});
projektloggTab.storyName = "Projektlogg tab";
projektloggTab.args = {
  projektloggPage: {
    number: 1,
    numberOfElements: 15,
    totalElements: 15,
    totalPages: 8,
    content: [
      loggVersionAterstalld,
      loggInfobrev,
      loggVersionBorttagen,
      loggMarkupplatelseavtal,
      loggVersionImporterad
    ]
  },
  sortDirection: "desc"
}
