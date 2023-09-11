import { MkAgareEditComponent } from "./agare-edit.component";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { testMarkagare } from "../../../test/data";
import { MkAgareEditModule } from "./agare-edit.module";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { action } from "@storybook/addon-actions";
import { MkAgare } from "../../model/agare";

export default {
  title: "Applikationer/Markkoll/Avtal/Ägare/Edit",
  component: MkAgareEditComponent,
  parameters: {
    docs: {
      description: {
        component: "Formulär för att redigare en markägare."
      }
    }
  },
  argTypes: {
    agare: {
      description: "Ägare"
    },
    showKontaktperson: {
      description: "Visa fält för kontaktperson"
    },
    kontaktpersonTooltip: {
      description: "Tooltip som visas i informationsruta för kontaktperson"
    },
    agareChange: {
      description: "Event när användaren klickar på Spara"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MkAgareEditModule
      ],
    }),
  ],
};

const Template: StoryFn<MkAgareEditComponent> = (args: MkAgareEditComponent) => ({
  props: {...
    args,
    agareChange: action("agareChange")
  }
});

export const edit = Template.bind({});
edit.storyName = "Redigera ägare";
edit.args = { 
  agare: testMarkagare(), 
  showKontaktperson: false, 
  kontaktpersonTooltip: "Ett litet tooltip" 
};

export const ingetKontaktpersonfält = Template.bind({});
ingetKontaktpersonfält.storyName = "Inget kontaktpersonfält";
ingetKontaktpersonfält.args = { 
  agare: testMarkagare(), 
  showKontaktperson: false, 
  kontaktpersonTooltip: ""
};

export const ersattningUtbetalas = Template.bind({});
ersattningUtbetalas.storyName = "Ersättning utbetald";
ersattningUtbetalas.args = { 
  agare: { 
    ...testMarkagare(), 
    status: Avtalsstatus.ERSATTNINGUTBETALD, 
    utbetalningsdatum: "2022-02-22" 
  }, 
  showKontaktperson: false, 
  kontaktpersonTooltip: "" };

export const nullAgare = Template.bind({});
nullAgare.storyName = "Ingen ägare";
nullAgare.args = { 
  agare: {} as MkAgare, 
  showKontaktperson: false,
  kontaktpersonTooltip: "" 
  };
