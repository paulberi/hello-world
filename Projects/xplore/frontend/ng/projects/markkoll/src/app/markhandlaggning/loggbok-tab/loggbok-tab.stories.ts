import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkMockLoggbok } from "../loggbok/loggbok.mock";
import { MkLoggbokTabComponent } from "./loggbok-tab.component";
import { MkLoggbokTabModule } from "./loggbok-tab.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Loggbok Tab",
  component: MkLoggbokTabComponent,
  decorators: [
    moduleMetadata({
      imports: [
        MkLoggbokTabModule,
        getTranslocoModule()
      ]
    })
  ]
};

const Template: StoryFn<MkLoggbokTabComponent> = (args: MkLoggbokTabComponent) => ({
  props: {
    ...args,
    anteckningarChange: action("anteckningarChange")
  }
});

export const loggbok = Template.bind({});
loggbok.storyName = "Loggbok";
loggbok.args = {
  loggbok: MkMockLoggbok,
  anteckningar: "Anteckningar",
  savingAnteckningar: false
};

export const spararAnteckningar = Template.bind({});
spararAnteckningar.storyName = "Sparar anteckningar";
spararAnteckningar.args = {
  loggbok: MkMockLoggbok,
  anteckningar: "Anteckningar",
  savingAnteckningar: true
};
