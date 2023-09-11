import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { XpMessageComponent, XpMessageSeverity } from "./message.component";
import { getTranslocoModule } from "../../../translate/transloco-testing.module";
import { XpMessageModule } from "./message.module";
import { action } from "@storybook/addon-actions";

export default {
  title: "Komponenter/Feedback/Message",
  component: XpMessageComponent,
  parameters: {
    docs: {
      description: {
        component: "Visa ett meddelande för användaren med en viss allvarlighetsgrad: success, information, warning eller error. Som standard visas success. Meddelande går att ta bort med ett kryss eller bekräfta med en knapp."
      }
    }
  },
  argTypes: {
    severity: { 
      control: { type: "radio", },
      options: [
      XpMessageSeverity.Success, 
      XpMessageSeverity.Information, 
      XpMessageSeverity.Warning, 
      XpMessageSeverity.Error
      ], 
      description: "Nivå av allvarlighet för meddelandet." 
    },
    text: {
      description: "Meddelande som visas för användaren."
    },
    isClosable: {
      description: "Om meddelande ska kunna stängas."
    },
    isActionable: {
      description: "Om det ska finnas en knapp för användaren att agera på."
    },
    actionLabel: {
      description: "Text för knapp (om den är påslagen med isActionable)."
    },
    onClose: {
      description: "Event när användaren stänger meddelandet."
    },
    onAction: {
      description: "Event när användaren klickar på knapp."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        XpMessageModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<XpMessageComponent> = (args: XpMessageComponent) => ({
  props: {
    ...args,
    onClose: action("onClose"),
    onAction: action("onAction")
  }
});

export const success = Template.bind({});
success.args = {
  severity: XpMessageSeverity.Success,
  text: "Bra gjort du är klar!",
  isClosable: true,
  isActionable: false,
  actionLabel: "Okej",
};

export const information = Template.bind({});
information.args = {
  severity: XpMessageSeverity.Information,
  text: "Det här vill vi informera om.",
  isClosable: false,
  isActionable: true,
  actionLabel: "Jag förstår",
};

export const warning = Template.bind({});
warning.args = {
  severity: XpMessageSeverity.Warning,
  text: "Det här vill vi varna om.",
  isClosable: false,
  isActionable: true,
  actionLabel: "Jag förstår",
};

export const error = Template.bind({});
error.args = {
  severity: XpMessageSeverity.Error,
  text: "Ett fel har uppstått som du behöver göra något åt.",
  isClosable: false,
  isActionable: true,
  actionLabel: "Jag förstår",
};
