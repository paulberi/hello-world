import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { action } from '@storybook/addon-actions';
import { UtcheckningWrapperComponent } from "./utcheckning-wrapper.component";
import { MMUtcheckningWrapperUiModule } from "./utcheckning-wrapper.module";

export default {
  title: "Applikationer/Mitt Metria/Utcheckning wrapper",
  component: UtcheckningWrapperComponent,
  parameters: {
    docs: {
      description: {
        component: "Wrapper för utcheckning"
      }
    }
  },
  argTypes: {
    goBackLabel: {
      description: "Text på label. Default är Fortsätt handla."
    },
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MMUtcheckningWrapperUiModule
      ],
    }),
  ],

} as Meta;

const Template: StoryFn<UtcheckningWrapperComponent> = (args: UtcheckningWrapperComponent) => ({
  props: {
    ...args,
    back: action("back")
  }
});

export const wrapper = Template.bind({});
wrapper.args = {
  goBackLabel: "Fortsätt handla"
};
