import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { AdmKundvyComponent } from "./kundvy.component";
import { AdmKundvyModule } from "./kundvy.module";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { kunder } from "./kundvy.testdata";
import { roles } from "../roles";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Admin/Kund/Kundvy",
  component: AdmKundvyComponent,
  decorators: [
    moduleMetadata({
      imports: [AdmKundvyModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmKundvyComponent> = (args: AdmKundvyComponent) => ({
  props: {...
    args,
    kundDelete: action("kundDelete"),
    kundChange: action("kundChange"),
    kundAdd: action("kundAdd"),
    pageChange: action("pageChange"),
    resetGeofenceRulesClick: action("resetGeofenceRulesClick")
  }
});

export const kundvy = Template.bind({});
kundvy.storyName = "Lista med kunder";
kundvy.args = {
  page: {
    content: kunder,
    number: 0,
    numberOfElements: 10,
    totalElements: 135,
    totalPages: 10,
  },
  roles: [roles.ADMIN_API, roles.GLOBAL_ADMIN],
  selectedIndex: null
};

export const visaKunder = Template.bind({});
visaKunder.storyName = "Visa en kund";
visaKunder.args = {
  page: {
    content: kunder,
    number: 0,
    numberOfElements: 10,
    totalElements: 135,
    totalPages: 10,
  },
  roles: [roles.ADMIN_API, roles.GLOBAL_ADMIN],
  selectedIndex: 2
};