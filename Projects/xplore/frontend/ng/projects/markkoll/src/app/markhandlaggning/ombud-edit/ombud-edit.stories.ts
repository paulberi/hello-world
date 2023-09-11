import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { testOmbud } from "../../../test/data";
import { MkAgare } from "../../model/agare";
import { MkOmbudEditComponent } from "./ombud-edit.component";
import { MkOmbudEditModule } from "./ombud-edit.module";

const Template: StoryFn<MkOmbudEditComponent> = (args: MkOmbudEditComponent) => ({
  props: {
    ...args,
    ombudChange: action("ombudChange"),
    delete: action("delete")
  }
});

export default {
  title: "Applikationer/Markkoll/Avtal/Ägare/Ombud Edit",
  component: MkOmbudEditComponent,
  parameters: {
    docs: {
      description: {
        component: "Formulär för att redigera ett ombud."
      }
    }
  },
  argTypes: {
    ombud: {
      description: "Ombud"
    },
    showKontaktperson: {
      description: "Visa fält för kontaktperson"
    },
    kontaktpersonTooltip: {
      description: "Tooltip som visas i informationsruta för kontaktperson"
    },
    ombudChange: {
      description: "Event när användaren klickar på Spara"
    },
    delete: {
      description: "Event när användaren klickar på Ta bort"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MkOmbudEditModule
      ]
    })
  ]
};

export const edit = Template.bind({});
edit.storyName = "Redigera ombud";
edit.args = {
  ombud: testOmbud(),
  showKontaktperson: false, 
  kontaktpersonTooltip: ""
};

export const ersattningUtbetalas = Template.bind({});
ersattningUtbetalas.storyName = "Ersättning utbetald";
ersattningUtbetalas.args = { 
  ombud: { ...testOmbud(), 
    status: Avtalsstatus.ERSATTNINGUTBETALD, 
    utbetalningsdatum: "2022-02-22" 
  }, 
  showKontaktperson: 
  false, kontaktpersonTooltip: "" };

export const nullOmbud = Template.bind({});
nullOmbud.storyName = "Inget ombud";
nullOmbud.args = { 
  ombud: null as MkAgare, 
  showKontaktperson: 
  false, kontaktpersonTooltip: "" 
};
