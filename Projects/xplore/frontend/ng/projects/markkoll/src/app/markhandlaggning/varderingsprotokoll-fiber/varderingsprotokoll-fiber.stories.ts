import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkFiberVarderingsprotokollComponent } from "./varderingsprotokoll-fiber.component";
import { MkFiberVarderingsprotokollModule } from "./varderingsprotokoll-fiber.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Avtal/Värdering/Fiber värderingsprotokoll",
  component: MkFiberVarderingsprotokollComponent,
  parameters: {
    docs: {
      description: {
        component: "Komponent för att fylla på infomation inför skapandet av värderingsprotokoll, som är en bilaga till avtalet."
      }
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkFiberVarderingsprotokollModule,
        getTranslocoModule(),
        BrowserAnimationsModule
      ],
    }),
  ],
};

const Template: StoryFn<MkFiberVarderingsprotokollComponent> = (args: MkFiberVarderingsprotokollComponent) => ({
  props: {
    ...args,
    avtalMetadataChange: action("avtalMetadataChange"),
    vpChange: action("vpChange")
  }
});

export const emptyProtokoll = Template.bind({});
emptyProtokoll.storyName = "Tomt protokoll";
emptyProtokoll.args = {
  vp: {
    id: "7bd48d15-6f95-4d23-bfab-a5819c7a4066",
    config: {
      lagspanning: true,
      storskogsbruksavtalet: false,
      ingenGrundersattning: false,
      forhojdMinimumersattning: false,
    },
    metadata: {
      ledning: "",
      koncessionslopnr: "",
      varderingstidpunkt: "2021-11-23T15:11:44.982193",
      varderingsmanOchForetag: "",
    },
    punktersattning: [],
    markledning: [],
    ssbSkogsmark: [],
    ssbVaganlaggning: [],
    intrangAkerOchSkogsmark: [],
    ovrigIntrangsersattning: []
  },
  uppdragsnummer: "Uppdragsnummer",
  ersattning: null,
  avtalMetaData: null
};
