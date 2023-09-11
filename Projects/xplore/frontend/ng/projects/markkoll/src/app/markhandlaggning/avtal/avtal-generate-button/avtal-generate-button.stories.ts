import { MkAvtalGenerateButtonComponent } from "./avtal-generate-button.component";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { AvtalsjobbStatus } from "../../../../../../../generated/markkoll-api";
import { MkAvtalGenerateButtonModule } from "./avtal-generate-button.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Generate Button",
  component: MkAvtalGenerateButtonComponent,
  parameters: {
    docs: {
      description: {
        component: "En knapp avsedd för avtalsjobb, med olika utseenden beroende på avtalsjobbets status"
      }
    }
  },
  argTypes: {
    status: {
      control: { type: "radio"},
      options: [
        AvtalsjobbStatus.CANCELLED, 
        AvtalsjobbStatus.DONE, 
        AvtalsjobbStatus.ERROR, 
        AvtalsjobbStatus.INPROGRESS, 
        AvtalsjobbStatus.NONE
      ],
      description: "Nuvarande status för avtalsjobb"
    },
    text: {
      description: "Text som ska synas på knappen"
    },
    disabled: {
      description: "Om knappen är disabled"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkAvtalGenerateButtonModule
      ]
    })
  ]
} as Meta;

const template: StoryFn<MkAvtalGenerateButtonComponent> = (args: MkAvtalGenerateButtonComponent) => ({
  props: args,
});

export const def = template.bind({});
def.storyName = "Standard";
def.args = {
  status: AvtalsjobbStatus.NONE,
  text: "Skapa alla avtal",
  disabled: false
};

export const inProgress = template.bind({});
inProgress.storyName = "Pågående";
inProgress.args = {
  status: AvtalsjobbStatus.INPROGRESS,
  text: "Avtal skapade: 13 av 37",
  disabled: false
};

export const done = template.bind({});
done.storyName = "Klar";
done.args = {
  status: AvtalsjobbStatus.DONE,
  text: "Ladda ner skapade avtal",
  disabled: false
};

export const error = template.bind({});
error.storyName = "Fel";
error.args = {
  status: AvtalsjobbStatus.ERROR,
  text: "Fel vid generering av avtal",
  disabled: false
};
