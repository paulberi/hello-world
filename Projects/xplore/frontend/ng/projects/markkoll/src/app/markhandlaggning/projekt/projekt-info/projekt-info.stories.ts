import { MatStepperModule } from "@angular/material/stepper";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { NisKalla, ProjektInfo } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkProjektInfoComponent } from "./projekt-info.component";
import { MkProjektInfoModule } from "./projekt-info.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Projekt-Info",
  component: MkProjektInfoComponent,
  parameters: {
    docs: {
      description: {
        component: "Skapa ett nytt projekt i Markkoll."
      }
    }
  },
  argTypes: {
    projektInfo: {
      description: "Generell projektinformation."
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
        MkProjektInfoModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkProjektInfoComponent> = (args: MkProjektInfoComponent) => ({
  props: {
    ...args,
    formChange: action("formChange"),
    valid: action("valid")
  }
});

export const newProjektInfo = Template.bind({});
newProjektInfo.storyName = "Nytt projekt";
newProjektInfo.args = {
  projektInfo: {} as ProjektInfo,
  isReadonly: false,
  nisKalla: {} as NisKalla
};

export const editProjektInfo = Template.bind({});
editProjektInfo.storyName = "Redigera projekt";
editProjektInfo.args = {
  projektInfo: {
    namn: "Projektet",
    projektTyp: "FIBER",
    ort: "Umeå",
    startDate: "",
    description: "So here's the story from a to z"
  } as ProjektInfo,
  isReadonly: false,
  nisKalla: {} as NisKalla
};


