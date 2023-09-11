import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkSamfallighetComponent } from "./samfallighet.component";
import { MkSamfallighetModule } from "./samfallighet.module";
import { MkMockLoggbok } from "../loggbok/loggbok.mock";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { XpMessageSeverity } from "../../../../../lib/ui/feedback/message/message.component";
import { testIntrang, testMarkagare, testMarkagare2 } from "../../../test/data";
import { OAuthModule } from "angular-oauth2-oidc";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Avtal/Samfällighet",
  component: MkSamfallighetComponent,
  parameters: {
    docs: {
      description: {
        component: "Redigerbar samfällighetsinformation för ett avtal."
      }
    }
  },
  argTypes: {
    intrangChange: {
      description: "Event när intrångsersättning sparas."
    },
    ombudChange: {
      description: "Event när användaren sparar ombud."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkSamfallighetModule,
        getTranslocoModule(),
        OAuthModule.forRoot()
      ]
    })
  ]
};

const Template: StoryFn<MkSamfallighetComponent> = (args: MkSamfallighetComponent) => ({
  props: {
    ...args,
    intrangChange: action("intrangChange"),
    ombudChange: action("ombudChange")
  }
});

export const samfallighet = Template.bind({});
samfallighet.storyName = "Med delägande fastigheter";
samfallighet.args = {
  avtal: {
    ingaendeFastigheter: [
      "HÖLJES S:3",
      "HÖLJES S:6",
      "HÖLJES S:9",
      "HÖLJES S:10",
      "HÖLJES S:12",
      "HÖLJES S:13",
    ],
    ersattning: 123,
    anteckning: "Anteckning",
    loggbok: MkMockLoggbok,
    status: Avtalsstatus.AVTALJUSTERAS,
    intrang: {
      markstrak: 12.3,
      luftstrak: 45.6
    }
  }
};

export const emptyIngaendeFastigheter = Template.bind({});
emptyIngaendeFastigheter.storyName = "Utan delägande fastigheter";
emptyIngaendeFastigheter.args = {
  avtal: {
    ingaendeFastigheter: [],
  },
};

export const ombud = Template.bind({});
ombud.storyName = "Med ombud";
ombud.args = {
  avtal: {
    ombud: [testMarkagare2(), testMarkagare()],
    intrang: testIntrang(),
  },
};
ombud.args.avtal.status = Avtalsstatus.AVTALSIGNERAT;

export const updatedMessage = Template.bind({});
updatedMessage.storyName = "Uppdaterad fastighet";
updatedMessage.args = {
  avtal: {
    ingaendeFastigheter: [
      "HÖLJES S:3",
      "HÖLJES S:6",
      "HÖLJES S:9",
      "HÖLJES S:10",
      "HÖLJES S:12",
      "HÖLJES S:13",
    ],
    ersattning: 123,
    anteckning: "Anteckning",
    loggbok: MkMockLoggbok,
    status: Avtalsstatus.AVTALJUSTERAS,
    intrang: {
      markstrak: 12.3,
      luftstrak: 45.6
    }
  },
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
  avtal: {
    ingaendeFastigheter: [
      "HÖLJES S:3",
      "HÖLJES S:6",
      "HÖLJES S:9",
      "HÖLJES S:10",
      "HÖLJES S:12",
      "HÖLJES S:13",
    ],
    ersattning: 123,
    anteckning: "Anteckning",
    loggbok: MkMockLoggbok,
    status: Avtalsstatus.AVTALJUSTERAS,
    intrang: {
      markstrak: 12.3,
      luftstrak: 45.6
    }
  },
  versionMessage: {
    title: "Borttagen fastighet",
    text:
      "Den här fastigheten har tagits bort i den senaste versionen.",
    severity: XpMessageSeverity.Warning,
    actionLabel: "Ta bort från listan",
  },
};
