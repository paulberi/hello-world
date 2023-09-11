import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { MarkkollUser } from "../../../../generated/kundconfig-api";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpEditUserComponent } from "./edit-user.component";
import { XpEditUserModule } from "./edit-user.module";

export default {
  title: "Komponenter/Page/User/Edit User",
  component: XpEditUserComponent,
  parameters: {
    docs: {
      description: {
        component: 'Den här komponenten ska främst användas för att skapa och redigera kundadministratörer i Xplore-Admin och "vanliga" användare inuti applikationerna. För att redigera en användare så skickar man in det User-objekt man vill redigera, så fylls formuläret i automatiskt.'
      }
    }
  },
  argTypes: {
    user: {
      description: "Användare att editera."
    },
    spinnerActive: {
      description: "Om spinner ska visas eller inte."
    },
    cancel: {
      description: "Avbryt."
    },
    submit: {
      description: "Submitta ett User-objekt."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [XpEditUserModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<XpEditUserComponent> = (args: XpEditUserComponent) => ({
  props: {...
    args,
    cancel: action("cancel"),
    submit: action("submit")
  }
});

export const showUser = Template.bind({});
showUser.storyName = "Ny användare";
showUser.args = {
  user: {} as MarkkollUser,
  spinnerActive: false
}
