import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { MkProjektloggComponent } from "./projektlogg.component";
import { MkProjektloggModule } from "./projektlogg.module";
import { Avtalhandelse, Infobrevhandelse, Projekthandelse, ProjekthandelseTyp, ProjektLoggItem, ProjektLoggType } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektlogg",
  component: MkProjektloggComponent,
  decorators: [
    moduleMetadata({
      imports: [MkProjektloggModule, getTranslocoModule()],
    }),
  ],
} as Meta;

const Template: StoryFn<MkProjektloggComponent> = (args: MkProjektloggComponent) => ({
  props: {
    ...args,
    filDownload: action("filDownload"),
    sortDirectionChange: action("sortDirectionChange"),
  }
});

const loggHamtaMarkagare: Projekthandelse = {
  projektLoggType: ProjektLoggType.PROJEKTHANDELSE,
  projekthandelseTyp: ProjekthandelseTyp.HAMTAMARKAGARE,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
};

const loggProjektinformationRedigerad: Projekthandelse = {
  projektLoggType: ProjektLoggType.PROJEKTHANDELSE,
  projekthandelseTyp: ProjekthandelseTyp.PROJEKTINFORMATIONREDIGERAD,
  skapadAv: "Britt-Marie",
  skapadDatum: "1986-03-04"
};

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

export const ingaHandelser = Template.bind({});
ingaHandelser.storyName = "Inga händelser";
ingaHandelser.args = {
  projektlogg: [] as ProjektLoggItem[],
  title: "Datum",
  sortDirection: "desc"
}

export const projekthandelser = Template.bind({});
projekthandelser.storyName = "Projekthändelser";
projekthandelser.args = {
  projektlogg: [
    loggHamtaMarkagare,
    loggProjektinformationRedigerad,
    loggVersionAterstalld,
    loggVersionBorttagen,
    loggVersionImporterad
  ],
  title: "Datum",
  sortDirection: "desc"
};

export const dokumenthandelser = Template.bind({});
dokumenthandelser.storyName = "Dokumenthändelser";
dokumenthandelser.args = {
  projektlogg: [
    loggInfobrev,
    loggMarkupplatelseavtal
  ],
  title: "Datum",
  sortDirection: "desc"
};
