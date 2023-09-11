import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { ChipListComponent } from "./chip-list.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MMChipListModule } from "./chip-list.module";

export default {
  title: "Applikationer/Mitt Metria/Chip list",
  component: ChipListComponent,
  parameters: {
    docs: {
      description: {
        component: "Lista med materials chips"
      }
    }
  },
  argTypes: {
    chips: {
      description: "Chips att visa i listan"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MMChipListModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<ChipListComponent> = (args: ChipListComponent) => ({
  props: {
    ...args,
    removeChip: action("removeChip")
  }
});

export const chipList = Template.bind({});

chipList.args = {
  chips: [
    {
      id: 0,
      title: "Färg",
      text: "Röd",
    },
    {
      id: 1,
      title: "Sökresultat",
      text: "pdf",
    },
    {
      id: 2,
      text: "Jag har ingen titel",
    }
  ],
};
