import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkCreateProjektComponent } from "./create-projekt.component";
import { MkCreateProjektModule } from "./create-projekt.module";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { action } from "@storybook/addon-actions";
import { NisKalla } from "../../../../../../../generated/markkoll-api";

export default {
  title: "Applikationer/Markkoll/Projekt/Create Projekt",
  component: MkCreateProjektComponent,
  parameters: {
    docs: {
      description: {
        component: "Skapa ett nytt projekt i Markkoll, antingen med projekttypen Fiber eller Elnät."
      }
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkCreateProjektModule,
        getTranslocoModule(),
        HttpClientTestingModule
      ],
    }),
  ],
};

const Template: StoryFn<MkCreateProjektComponent> = (args: MkCreateProjektComponent) => ({
  props: {
    ...args,
    create: action("create")
  }
});

export const createProjekt = Template.bind({});
createProjekt.storyName = "Skapa nytt projekt";
createProjekt.args = {
  isCreatingProjekt: false,
  ledningsagare: [] as string[],
  nisKalla: {} as NisKalla
};
