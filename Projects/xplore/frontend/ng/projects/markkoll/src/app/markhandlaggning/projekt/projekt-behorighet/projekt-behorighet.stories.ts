import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { of } from "rxjs";
import { Produkt, Roll } from "../../../../../../../generated/kundconfig-api";
import { MarkkollUser, RoleType } from "../../../../../../../generated/markkoll-api";
import { users } from "../../../../../../admin/src/app/kundvy/kundvy.testdata";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkProjektBehorighetComponent } from "./projekt-behorighet.component";
import { UserWithRoll } from "./projekt-behorighet.container";
import { MkProjektBehorighetModule } from "./projekt-behorighet.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Projekt-Behörighet",
  component: MkProjektBehorighetComponent,
  argTypes: {
    users: {
      description: "Array med användare man kan lägga till till projektet"
    },
    formChange: {
      description: "Event när värden i formuläret ändras."
    },
    valid: {
      description: "Event med en flagga om formuläret är rätt ifyllt."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkProjektBehorighetModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkProjektBehorighetComponent> = (args: MkProjektBehorighetComponent) => ({
  props: {
    ...args,
    formChange: action("formChange"),
    valid: action("valid")
  }
});

const loggedInUser: MarkkollUser = {
  fornamn: "Test",
  efternamn: "Testsson",
  email: "email",
  kundId: "Metria",
  id: "email",
  roles: [{ objectId: "Metria", roleType: RoleType.KUNDADMIN }]
}
const usersWithRoll: UserWithRoll = {
  user: loggedInUser,
  roll: RoleType.PROJEKTADMIN
}

export const projektBehorighet = Template.bind({});
projektBehorighet.storyName = "Välj användare";
projektBehorighet.args = {
  loggedInUser: loggedInUser,
  users: of([loggedInUser]),
  originalProjektUsers$: of(usersWithRoll),
  createProjekt: false,
  label: "Lägg till användare till projektet",
  readonly: false
};
