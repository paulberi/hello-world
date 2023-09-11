import { action } from "@storybook/addon-actions";
import { moduleMetadata, Meta, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkIntrang } from "../../model/intrang";
import { MkIntrangComponent } from "./intrang.component";
import { MkIntrangModule } from "./intrang.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Värdering/Fiber ersättning",
  component: MkIntrangComponent,
  parameters: {
    docs: {
      description: {
        component: "Visar aktuellt intrång på fastigheten och hur mycket markägaren ska få i ersättning."
      }
    }
  },
  argTypes: {
    title: {
      description: "Titel högst upp."
    },
    intrang: {
      description: "Information om intrång på fastighet."
    },
    ersattning: {
      description: "Ersättning till markägare i SEK."
    },
    saveLabel: {
      description: "Etikett för spara-knapp."
    },
    isSaving: {
      description: "Om indikator för sparning av ändringar ska visas"
    },
    intrangChange: {
      description: "Event när användaren sparar med ersättning som data."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [MkIntrangModule, getTranslocoModule()],
    }),
  ],
} as Meta;

const Template: StoryFn<MkIntrangComponent> = (args: MkIntrangComponent) => ({
  props: {
    ...args,
    intrangChange: action("intrangChange")
  }
});

export const fiberMedErsattning = Template.bind({});
fiberMedErsattning.storyName = "Fiber med ersättning";
fiberMedErsattning.args = {
  intrang: {
    markstrak: 100,
    luftstrak: 50,
  },
  ersattning: 3500,
  saveLabel: "Spara",
  title: "Intrångsersättning",
  isSaving: false
};

export const fiberUtanErsattning = Template.bind({});
fiberUtanErsattning.storyName = "Fiber utan ersättning";
fiberUtanErsattning.args = {
  intrang: {
    markstrak: 100,
    luftstrak: 50,
  },
  saveLabel: "Spara",
  ersattning: "",
  title: "Intrångsersättning",
  isSaving: false
};

export const saving = Template.bind({});
saving.storyName = "Visa sparningsindikator";
saving.args = {
  title: "Intrångsersättning",
  ersattning: "",
  intrang: {} as MkIntrang,
  isSaving: true,
  saveLabel: "Spara",
};
