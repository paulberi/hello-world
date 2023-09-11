import { MkAvtalActionsComponent } from "./avtal-actions.component";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { XpPrefixedSelectionModule } from "../../../../../../lib/ui/prefixed-selection/prefixed-selection.module";
import { MkAvtalActionsModule } from "./avtal-actions.module";
import { AvtalsjobbStatus } from "../../../../../../../generated/markkoll-api";
import { action } from "@storybook/addon-actions";

const template: StoryFn<MkAvtalActionsComponent> = (args: MkAvtalActionsComponent) => ({
  props: {
    ...args,
    avtalsActionChange: action("avtalsActionChange"),
    executeAction: action("executeAction"),
    resetAction: action("resetAction")
  }
});

export default {
  title: "Applikationer/Markkoll/Avtal/Actions",
  component: MkAvtalActionsComponent,
  argTypes: {
    jobbStatus: { 
      control: { type: "radio"},
      options: [
        AvtalsjobbStatus.CANCELLED, 
        AvtalsjobbStatus.DONE, 
        AvtalsjobbStatus.ERROR, 
        AvtalsjobbStatus.INPROGRESS, 
        AvtalsjobbStatus.NONE
      ]
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpPrefixedSelectionModule,
        MkAvtalActionsModule
      ]
    })
  ]
};

export const Default = template.bind({});
Default.storyName = "Start";
Default.args = {
  filter: null,
  jobbStatus: AvtalsjobbStatus.NONE,
  submitButtonText: "Skapa alla avtal",
  resetButtonText: "Rensa",
  actionsDisabled: false,
  dokumentmallar: [
    {
      dokumenttyp: "MARKUPPLATELSEAVTAL",
      id: "328b64b0-7c76-4862-b227-5d7df7a97c67",
      namn: "avtal 1"
    },
    {
      dokumenttyp: "MARKUPPLATELSEAVTAL",
      id: "a7e72c32-61af-4a6b-b425-c9ed85155cc9",
      namn: "avtal 2"
    },
    {
      dokumenttyp: "INFOBREV",
      id: "328b64b0-9g83-4862-b227-5d7df7a97c67",
      namn: "info 1"
    }
  ]
};

export const WithoutInfobrev = template.bind({});
WithoutInfobrev.storyName = "Utan mall";
WithoutInfobrev.args = {
  filter: null,
  jobbStatus: AvtalsjobbStatus.NONE,
  submitButtonText: "Skapa alla avtal",
  resetButtonText: "Rensa",
  actionsDisabled: false,
  dokumentmallar: [
    {
      dokumenttyp: "MARKUPPLATELSEAVTAL",
      id: "328b64b0-7c76-4862-b227-5d7df7a97c67",
      namn: "avtal 1"
    },
    {
      dokumenttyp: "MARKUPPLATELSEAVTAL",
      id: "a7e72c32-61af-4a6b-b425-c9ed85155cc9",
      namn: "avtal 2",
      selected: true
    }
  ]
};
