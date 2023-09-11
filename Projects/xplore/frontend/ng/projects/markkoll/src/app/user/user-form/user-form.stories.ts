import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { Produkt, Roll } from "../../../../../../generated/kundconfig-api";
import { MarkkollUser } from "../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkUserFormComponent } from "./user-form.component";
import { MkUserFormModule } from "./user-form.module";

export default {
  title: "Komponenter/Page/User/User Form",
  component: MkUserFormComponent,
  argTypes: {
    editUser: {
      description: "Användare man vill editera, om inget stoppas in så förväntar komponenten sig att man vill skapa en ny användare."
    },
    loggedInUser: {
      description: "Inloggade användaren."
    },
    submitClick: {
      description: "Submitta ett User-objekt."
    },
    deleteClick: {
      description: "Ta bort en användare."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [MkUserFormModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<MkUserFormComponent> = (args: MkUserFormComponent) => ({
  props: {...
    args,
    submitClick: action("submitClick"),
    deleteClick: action("deleteClick")
  }
});

export const showUser = Template.bind({});
showUser.storyName = "Ny användare";
showUser.args = {
  editUser: null,
  loggedInUser: null
}

export const editUser = Template.bind({});
editUser.storyName = "Redigera användare";
editUser.args = {
  editUser: {
    id: "epost",
    email: "epost@epost.se",
    fornamn: "Test",
    efternamn: "Test Testsson",
    kundId: "Testkund AB"
  } as MarkkollUser,
  loggedInUser: null
};
