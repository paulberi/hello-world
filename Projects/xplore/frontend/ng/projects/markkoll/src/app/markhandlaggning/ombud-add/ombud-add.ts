import { moduleMetadata } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkOmbudAddComponent } from "./ombud-add.component";
import { MkOmbudAddModule } from "./ombud-add.module";

const Template = (args: MkOmbudAddComponent) => ({
  component: MkOmbudAddComponent,
  props: args,
});

export default {
  title: "Markkoll/Markhandläggning/Presentation/Markägare/Ombud Add",
  decorators: [
    moduleMetadata({
      imports: [MkOmbudAddModule, getTranslocoModule()],
    }),
  ],
};

export const add = Template.bind({});
add.storyName = "Lägg till ombud";
add.args = { ombud: null };
