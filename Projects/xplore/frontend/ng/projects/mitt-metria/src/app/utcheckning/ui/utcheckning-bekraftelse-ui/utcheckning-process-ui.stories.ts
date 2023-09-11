import { MMUtcheckningBekraftelseUiModule } from './utcheckning-bekraftelse-ui.module';
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { UtcheckningBekraftelseUiComponent } from './utcheckning-bekraftelse-ui.component';

export default {
  title: "Applikationer/Mitt Metria/Orderbekräftelse",
  component: UtcheckningBekraftelseUiComponent,
  parameters: {
    docs: {
      description: {
        component: "Orderbekräftelse"
      }
    }
  },
  argTypes: {
    orderHistoryPath: {
      description: "Url till orderhistorik. Default är /orderhistorik"
    },
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MMUtcheckningBekraftelseUiModule
      ],
    }),
  ],

} as Meta;

const Template: StoryFn<UtcheckningBekraftelseUiComponent> = (args: UtcheckningBekraftelseUiComponent) => ({
  props: {
    ...args,
  }
});

export const confirmation = Template.bind({});

