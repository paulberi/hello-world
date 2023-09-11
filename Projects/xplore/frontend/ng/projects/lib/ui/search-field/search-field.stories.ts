import { moduleMetadata, StoryFn } from "@storybook/angular";
import { XpUiSearchFieldComponent } from "./search-field.component";
import { XpUiSearchFieldModule } from "./search-field.module";

const Template: StoryFn<XpUiSearchFieldComponent> = (args: XpUiSearchFieldComponent) => ({
  props: args
});

export default {
  title: "Komponenter/Form/Search Field",
  component: XpUiSearchFieldComponent,
  parameters: {
    docs: {
      description: {
        component: "Ett tänkt sökfält med en viss 'debounce time' på 500ms innan värdet i sökfältet emittas."
      }
    }
  },
  argTypes: {
    placeholder: {
      description: "Etikett."
    },
    debounceTime: {
      description: "Tid innan värdet från sökfältet emittas."
    },
    ariaLabel: {
      description: "aria-label för \"Rensa sökfält\"-knappen."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        XpUiSearchFieldModule
      ]
    })
  ]
};

export const Default = Template.bind({});
Default.storyName = "Sökfält";
Default.args = {
  placeholder: "Placeholdertext",
  debounceTime: 500,
  ariaLabel: "Rensa"
};
