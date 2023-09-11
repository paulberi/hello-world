import { MatStepperModule } from "@angular/material/stepper";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { FiberInfo } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkFiberInfoComponent } from "./fiber-info.component";
import { MkFiberInfoModule } from "./fiber-info.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Fiber-Info",
  component: MkFiberInfoComponent,
  parameters: {
    docs: {
      description: {
        component: "Formulär för fiberprojekt."
      }
    }
  },
  argTypes: {
    fiberInfo: {
      description: "Fiberinformation."
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
        MkFiberInfoModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkFiberInfoComponent> = (args: MkFiberInfoComponent) => ({
  props: {
    ...args,
    formChange: action("formChange"),
    valid: action("valid")
  }
});

export const newProjekt = Template.bind({});
newProjekt.storyName = "Nytt projekt";
newProjekt.args = {
  fiberInfo: {} as FiberInfo,
  isReadonly: false,
  ledningsagare: [] as string[]
};

export const editProjektInfo = Template.bind({});
editProjektInfo.storyName = "Redigera projekt";
editProjektInfo.args = {
  fiberInfo: {
    bestallare: "Beställar-Bertil",
    bidragsprojekt: true,
    ledningsagare: "Telia",
    ledningsstracka: "Lilla berget - Storsjön",
  } as FiberInfo,
  isReadonly: false,
  ledningsagare: [] as string[]
};

