import { action } from "@storybook/addon-actions";
import { moduleMetadata } from "@storybook/angular";
import { Meta, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { kunder } from "../kundvy/kundvy.testdata";
import { roles } from "../roles";
import { AdmCreateKundComponent } from "./create-kund.component";
import { AdmCreateKundModule } from "./create-kund.module";

export default {
  title: "Applikationer/Admin/Kund/Create",
  component: AdmCreateKundComponent,
  decorators: [
    moduleMetadata({
      imports: [AdmCreateKundModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmCreateKundComponent> = (args: AdmCreateKundComponent) => ({
  props: {
    ...args,
    submit: action("submit"),
    back: action("back")
  }
});

export const CreateKunder = Template.bind({});
CreateKunder.storyName = "Ny kund";
CreateKunder.args = {
  kunder: kunder,
  nyaKunder: [],
  roles: [roles.ADMIN_API, roles.GLOBAL_ADMIN],
  createMore: true
};

export const CreateFleraKunder = Template.bind({});
CreateFleraKunder.storyName = "Flera kunder";
CreateFleraKunder.args = {
  kunder: kunder,
  nyaKunder: kunder,
  roles: [roles.ADMIN_API, roles.GLOBAL_ADMIN],
  createMore: true
};
