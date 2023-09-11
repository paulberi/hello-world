import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { ElnatInfo } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkElnatInfoComponent } from "./elnat-info.component";
import { MkElnatInfoModule } from "./elnat-info.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Elnät-Info",
  component: MkElnatInfoComponent,
  parameters: {
    docs: {
      description: {
        component: "Formulär för Elnätsprojekt."
      }
    }
  },
  argTypes: {
    elnatInfo: {
      description: "Elnätsinformation."
    },
    formChange: {
      description: "Event när värden i formuläret ändras."
    },
    valid: {
      description: "Event med en flagga om formuläret är rätt ifyllt."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkElnatInfoModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkElnatInfoComponent> = (args: MkElnatInfoComponent) => ({
  props: {
    ...args,
    formChange: action("formChange"),
    valid: action("valid")
  }
});

export const newProjekt = Template.bind({});
newProjekt.storyName = "Nytt projekt";
newProjekt.args = {
  elnatInfo: {} as ElnatInfo,
  ledningsagare: [] as string[],
  isReadonly: false
};

export const editProjekt = Template.bind({});
editProjekt.storyName = "Redigera projekt";
editProjekt.args = {
  elnatInfo: {
    bestallare: "Beställar-Bertil",
    ledningsagare: "Telia",
    ledningsstracka: "Lilla berget - Storsjön",
  } as ElnatInfo,
  ledningsagare: [] as string[],
  isReadonly: false
};
