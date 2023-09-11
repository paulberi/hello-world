import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { nyttOmbud } from "../../../test/data";
import { MkOmbudAddComponent } from "./ombud-add.component";
import { MkOmbudAddModule } from "./ombud-add.module";

const Template: StoryFn<MkOmbudAddComponent> = (args: MkOmbudAddComponent) => ({
  props: {
    ...args,
    ombudChange: action("ombudChange"),
    cancel: action("cancel")
  }
});

export default {
  title: "Applikationer/Markkoll/Avtal/Ägare/Ombud Add",
  component: MkOmbudAddComponent,
  parameters: {
    docs: {
      description: {
        component: "Formulär för att lägga till ett ombud."
      }
    }
  },
  argTypes: {
    ombud: {
      description: "Ombud"
    },
    ombudChange: {
      description: "Event när användaren klickar på Spara"
    },
    cancel: {
      description: "Event när användaren klickar på Avbryt"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MkOmbudAddModule
      ],
    }),
  ],
};

export const nullOmbud = Template.bind({});
nullOmbud.storyName = "Nytt ombud";
nullOmbud.args = { ombud: nyttOmbud };
