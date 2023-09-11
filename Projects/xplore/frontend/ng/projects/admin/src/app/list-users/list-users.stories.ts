import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { users } from "../kundvy/kundvy.testdata";
import { AdmListUsersComponent } from "./list-users.component";
import { AdmUsersModule } from "./list-users.module";

export default {
  title: "Applikationer/Admin/User",
  component: AdmListUsersComponent,
  decorators: [
    moduleMetadata({
      imports: [AdmUsersModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmListUsersComponent> = (args: AdmListUsersComponent) => ({
  props: {...
    args,
    create: action("create"),
    delete: action("delete")
  }
});

export const showUsers = Template.bind({});
showUsers.storyName = "Lista med användare";
showUsers.args = {
  users: users,
  spinnerActive: false,
  isAddKundAdminVisible: false
};

export const addUser = Template.bind({});
addUser.storyName = "Ny användare";
addUser.args = {
  users: users,
  addKundAdmin: true,
  spinnerActive: false,
  isAddKundAdminVisible: false
};
