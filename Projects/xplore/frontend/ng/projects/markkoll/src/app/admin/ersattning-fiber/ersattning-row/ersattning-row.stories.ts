import { moduleMetadata, Meta, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkErsattningRowComponent } from "./ersattning-row.component";
import { MkErsattningRowModule } from "./ersattning-row.module";

export default {
  title: "Applikationer/Markkoll/Admin/ErsättningFiber",
  component: MkErsattningRowComponent,
  decorators: [
    moduleMetadata({
      imports: [MkErsattningRowModule, getTranslocoModule()],
    }),
  ],
} as Meta;

const Template: StoryFn<MkErsattningRowComponent> = (args: MkErsattningRowComponent) => ({
  props: args,
});

export const Ersattningsrad = Template.bind({});
Ersattningsrad.storyName = "Ersättningsrad";
Ersattningsrad.args = {
  title: "Ersättning",
  value: 2200
};
