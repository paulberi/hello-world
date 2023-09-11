import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import {StepperComponent} from "./stepper.component";
import {MMStepperModule} from "./stepper.module";
import {action} from "@storybook/addon-actions";

export default {
  title: "Applikationer/Mitt Metria/Stepper",
  component: StepperComponent,
  parameters: {
    docs: {
      description: {
        component: "En lista av steg i köpprocessen"
      }
    }
  },
  argTypes: {
    steps: {
      description: "Stegen i processen i JSON-format"
    },
    activeStep: {
      description: "Det nuvarande aktiva steget i processen"
    },
    title: {
      description: "Titeln som förklarar vad en kund förväntas göra"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MMStepperModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<StepperComponent> = (args: StepperComponent) => ({
  props: {
    ...args,
    onCancelOrder: action("onCancelOrder")
  }
});

export const stepper = Template.bind({});
export const stepper_empty = Template.bind({});
export const stepper_long_title = Template.bind({});

stepper.args = {
  steps: [
    {
      stepNumber: 1,
      title: "Välj område"
    },
    {
      stepNumber: 2,
      title: "Välj referenssystem"
    },
    {
      stepNumber: 3,
      title: "Välj leveranssystem"
    }
  ],
  activeStep: 3,
  title: "Skapa din rapport"
};

stepper_empty.args = {
  steps: [ ],
  title: "Tom stepper!"
}

stepper_long_title.args = {
  steps: [
    {
      stepNumber: 1,
      title: "Välj område"
    },
    {
      stepNumber: 2,
      title: "Välj referenssystem"
    },
    {
      stepNumber: 3,
      title: "Välj leveranssystem"
    }
  ],
  activeStep: 1,
  title: "Skapa din rapport och flera andra eftersom det är en rolig sak att göra och se om vi förstör något när vi har så här lång text i en stepper!"
};
