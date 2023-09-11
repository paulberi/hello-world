import { moduleMetadata, StoryFn } from "@storybook/angular";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { testIntrang, testMarkagare, testMarkagare2 } from "../../../test/data";
import { MkFastighetComponent } from "./fastighet.component";
import { MkFastighetsinformationModule } from "./fastighet.module";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { XpMessageSeverity } from "../../../../../lib/ui/feedback/message/message.component";
import * as AvtalskartaStories from "../avtal/avtalskarta/avtalskarta.stories";
import * as AvtalsjobbButtonStories from "../avtal/avtal-generate-button/avtal-generate-button.stories";
import { MkMockLoggbok } from "../loggbok/loggbok.mock";
import { OAuthModule } from "angular-oauth2-oidc";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Avtal/Fastighet",
  component: MkFastighetComponent,
  parameters: {
    docs: {
      description: {
        component: "Redigerbar fastighetsinformation för ett avtal."
      }
    }
  },
  argTypes: {
    intrangChange: {
      description: "Event när användaren har redigerat intrång."
    },
    agareChange: {
      description: "Event när användaren sparar en markägare."
    },
    skogsfastighetChange: {
      description: "Event när användaren sätter skogsfastighetsstatus för ett avtal."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkFastighetsinformationModule,
        getTranslocoModule(),
        OAuthModule.forRoot()
      ],
    }),
  ],
};

const Template: StoryFn<MkFastighetComponent> = (args: MkFastighetComponent) => ({
  props: {...
    args,
    intrangChange: action("intrangChange"),
    agareChange: action("agareChange"),
    skogsfastighetChange: action("skogsfastighetChange"),
  }
});

export const singleAgare = Template.bind({});
singleAgare.storyName = "Enstaka ägare";
singleAgare.args = {
  avtal: {
    status: Avtalsstatus.AVTALJUSTERAS,
    lagfarnaAgare: [testMarkagare()],
    avtalskarta: AvtalskartaStories.tvaFastighetsomraden.avtalskarta,
  },
};

export const multipleAgare = Template.bind({});
multipleAgare.storyName = "Flera ägare";
multipleAgare.args = {
  avtal: {
    status: Avtalsstatus.AVTALSIGNERAT,
    lagfarnaAgare: [testMarkagare(), testMarkagare2()],
    avtalskarta: AvtalskartaStories.tvaFastighetsomraden.avtalskarta,
  },
};

export const inkluderaSome = Template.bind({});
inkluderaSome.storyName = "Vissa ägare signerar avtal";
inkluderaSome.args = {
  avtal: {
    status: Avtalsstatus.AVTALSKICKAT,
    lagfarnaAgare: [
      testMarkagare(),
      { ...testMarkagare2(), inkluderaIAvtal: false },
    ],
    avtalskarta: AvtalskartaStories.tvaFastighetsomraden.avtalskarta,
  },
};

export const finfo = Template.bind({});
finfo.storyName = "Tomträttsinnehavare och ombud";
finfo.args = {
  avtal: {
    lagfarnaAgare: [testMarkagare2(), testMarkagare()],
    tomtrattsinnehavare: [testMarkagare2(), testMarkagare()],
    ombud: [testMarkagare2(), testMarkagare()],
    intrang: testIntrang(),
    skapaAvtalStatus: AvtalsjobbButtonStories.def.status,
    avtalskarta: AvtalskartaStories.tvaFastighetsomraden.avtalskarta,
    outredd: false,
  },
};

export const empty = Template.bind({});
empty.storyName = "Markägare ej hämtade";
empty.args = {
  avtal: {
    status: null,
    lagfarnaAgare: [],
    tomtrattsinnehavare: [],
    ombud: [],
    intrang: null,

    skapaAvtalStatus: AvtalsjobbButtonStories.def.status,
    outredd: false,
  },
};

export const outredd = Template.bind({});
outredd.storyName = "Outredd fastighet";
outredd.args = {
  avtal: {
    outredd: true,
  },
};

export const updatedMessage = Template.bind({});
updatedMessage.storyName = "Uppdaterad fastighet";
updatedMessage.args = {
  versionMessage: {
    title: "Uppdaterad fastighet",
    text:
      "Intrånget på den här fastigheten har uppdaterats i den senaste versionen.",
    severity: XpMessageSeverity.Information,
    actionLabel: "Jag förstår",
  },
};

export const deletedMessage = Template.bind({});
deletedMessage.storyName = "Borttagen fastighet";
deletedMessage.args = {
  versionMessage: {
    title: "Borttagen fastighet",
    text:
      "Den här fastigheten har tagits bort i den senaste versionen.",
    severity: XpMessageSeverity.Warning,
    actionLabel: "Ta bort från listan",
  },
};




export const loggbok = Template.bind({});
loggbok.storyName = "Loggboksflik";
loggbok.args = {
  avtal: {
    loggbok: MkMockLoggbok,
    anteckning:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu feugiat mauris. Aliquam consectetur, velit sed egestas porta, ipsum augue mollis nisl, sed tincidunt lorem elit at lectus.",
  },
  tabIndex: 1,
};

export const addOmbud = Template.bind({});
addOmbud.storyName = "Lägg till ombud";
addOmbud.args = {
  ombudFormVisible: true,
  avtal: {
    lagfarnaAgare: [testMarkagare()],
  },
};

export const spinners = Template.bind({});
spinners.storyName = "Spinners";
spinners.args = {
  savingAnteckningar: true,
  savingIntrang: true,
  skapaAvtal: true,
};
