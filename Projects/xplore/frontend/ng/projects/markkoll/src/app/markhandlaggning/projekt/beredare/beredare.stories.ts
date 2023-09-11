import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkBeredareComponent } from "./beredare.component";
import { MkBeredareModule } from "./beredare.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Redigera beredare",
  component: MkBeredareComponent,
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        getTranslocoModule(),
        MkBeredareModule
      ]
    })
  ]
};

const Template: StoryFn<MkBeredareComponent> = (args: MkBeredareComponent) => ({
  props: {
    ...args,
    beredareChange: action("beredareChange")
  }
});

export const edit = Template.bind({});
edit.storyName = "Beredare";
edit.args = {
  beredare: {
    namn: "Åke Åsgaard",
    telefonnummer: "581 204",
    adress: "Beredningsvägen 3",
    postnummer: "987 65",
    ort: "Robertsfors"
  }
};
