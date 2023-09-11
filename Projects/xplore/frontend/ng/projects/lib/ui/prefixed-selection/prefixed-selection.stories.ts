import {XpPrefixedSelectionComponent} from "./prefixed-selection.component";
import {moduleMetadata, StoryFn} from "@storybook/angular";
import {getTranslocoModule} from "../../translate/transloco-testing.module";
import { XpPrefixedSelectionModule } from "./prefixed-selection.module";

const Template: StoryFn<XpPrefixedSelectionComponent> = (args: XpPrefixedSelectionComponent) => ({
  props: args
});

export default {
  title: "Komponenter/Form/Prefixed Selection",
  component: XpPrefixedSelectionComponent,
  argTypes: {
    labelTextAlign: { 
      control: { type: "radio"},
      options: ["left", "center", "right"],
      description: "Hur man vill aligna texten." 
    },
    prefix: {
      description: "Text som visas innan selection." 
    },
    options: {
      description: "Vilka val som ska finnas." 
    },
    multiple: {
      description: "Om man ska kunna välja flera." 
    },
    label: {
      description: "Text som syns innan något är valt." 
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpPrefixedSelectionModule
      ]
    })
  ]
};

export const Default = Template.bind({});
Default.storyName = "Med label";
Default.args = {
  prefix: "Jag vill",
  label: "Välj aktivitet",
  multiple: false,
  labelTextAlign: "left",
  options: [
    {
      value: "markupplatelse",
      label: "Skapa markupplåtelseavtal"
    },
    {
      value: "infobrev",
      label: "Skapa Informationsbrev"
    },
    {
      value: "status",
      label: "Sätta status"
    }
  ]
};

export const NoLabel = Template.bind({});
NoLabel.storyName = "Ingen label";
NoLabel.args = {
  prefix: "Jag vill",
  label: null,
  multiple: false,
  labelTextAlign: "left",
  options: [
    {
      value: "markupplatelse",
      label: "Skapa markupplåtelseavtal"
    },
    {
      value: "infobrev",
      label: "Skapa Informationsbrev"
    },
    {
      value: "status",
      label: "Sätta status"
    }
  ]
};

export const DisabledOption = Template.bind({});
DisabledOption.storyName = "Avslaget alternativ";
DisabledOption.args = {
  prefix: "Jag vill",
  label: "Välj aktivitet",
  multiple: false,
  labelTextAlign: "left",
  options: [
    {
      value: "markupplatelse",
      label: "Skapa markupplåtelseavtal"
    },
    {
      value: "infobrev",
      label: "Skapa Informationsbrev",
      isDisabled: true
    },
    {
      value: "status",
      label: "Sätta status"
    }
  ]
};

export const Multiple = Template.bind({});
Multiple.storyName = "Multival";
Multiple.args = {
  prefix: "Jag vill",
  label: "Välj aktivitet",
  multiple: true,
  labelTextAlign: "left",
  options: [
    {
      value: "markupplatelse",
      label: "Skapa markupplåtelseavtal"
    },
    {
      value: "infobrev",
      label: "Skapa Informationsbrev"
    },
    {
      value: "status",
      label: "Sätta status"
    }
  ]
};
