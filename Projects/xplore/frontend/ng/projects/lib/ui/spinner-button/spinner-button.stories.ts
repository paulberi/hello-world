import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { XpSpinnerButtonComponent } from "./spinner-button.component";
import { XpSpinnerButtonModule } from "./spinner-button.module";

/** 
 * Baseline för hur man bygger upp en stories-fil i Storybook.
 * Använder en template för att bygga upp en komponent med ng-content.
 * Fångar upp event för spinnerButtonClick och kopplar till action-metod
 * för att händelse i Storybooks Action-flik. 
 **/
export default {
  title: "Komponenter/Form/Spinner Button",
  component: XpSpinnerButtonComponent,
  parameters: {
    docs: {
      description: {
        component: "En knapp med en laddningsindikator (spinner) som är möjlig att aktivera."
      }
    }
  },
  argTypes: {
    color: { 
      control: { type: "radio"},
      options: ["primary", "secondary", "accent"],
      description: "Färg för knapp från Material Design, antingen primary, secondary eller accent." 
    },
    type: { 
      control: { type: "radio"},
      options: ["raised", "flat"],
      description: "Vilket utseende knappen ska ha." 
    },
    isLoading: {
      description: "Om spinner ska visas eller inte."
    },
    isDisabled: {
      description: "Om knappen ska vara aktiv eller inte."
    },
    spinnerButtonClick: {
      description: "Event när användaren trycker på knappen."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [XpSpinnerButtonModule],
    }),
  ],
  excludeStories: /.*Data$/,
};

export const actionsData = {
  spinnerButtonClick: action("spinnerButtonClick"),
};

const Template: StoryFn<XpSpinnerButtonComponent> = (args: XpSpinnerButtonComponent) => ({
  template: `<xp-spinner-button
              [isLoading]="isLoading"
              [isDisabled]="isDisabled"
              [color]="color"
              (spinnerButtonClick)="spinnerButtonClick()">
              {{ text }}
            </xp-spinner-button>`,
  props: {
    ...args,
    spinnerButtonClick: actionsData.spinnerButtonClick,
  },
});

export const primaryNoSpin = Template.bind({});
primaryNoSpin.storyName = "Primär utan spinner";
primaryNoSpin.args = {
  color: "primary",
  isLoading: false,
  text: "Primär utan spinner",
  isDisabled: false,
  type: "raised"
};

export const primarySpin = Template.bind({});
primarySpin.storyName = "Primär med spinner";
primarySpin.args = {
  color: "primary",
  isLoading: true,
  text: "Primär med spinner",
  isDisabled: false,
  type: "raised"
};


export const secondaryNoSpin = Template.bind({});
secondaryNoSpin.storyName = "Sekundär utan spinner";
secondaryNoSpin.args = {
  color: "secondary",
  isLoading: false,
  text: "Sekundär utan spinner",
  isDisabled: false,
  type: "raised"
};

export const secondarySpin = Template.bind({});
secondarySpin.storyName = "Sekundär med spinner";
secondarySpin.args = {
  color: "secondary",
  isLoading: true,
  text: "Sekundär med spinner",
  isDisabled: false,
  type: "raised"
};

export const disabled = Template.bind({});
disabled.storyName = "Inaktiverad";
disabled.args = {
  color: "primary",
  isLoading: false,
  isDisabled: true,
  text: "Inaktiverad knapp",
  type: "raised"
};
