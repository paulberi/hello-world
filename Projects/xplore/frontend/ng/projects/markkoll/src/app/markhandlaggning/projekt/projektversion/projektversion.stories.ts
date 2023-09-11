import { moduleMetadata, StoryFn } from "@storybook/angular";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkProjektversionComponent } from "./projektversion.component";
import { MkProjektversionModule } from "./projektversion.module";
import { DialogService } from "../../../services/dialog.service";
import { MockService } from "ng-mocks";
import { Version } from "../../../../../../../generated/markkoll-api";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektversion",
  component: MkProjektversionComponent,
  parameters: {
    docs: {
      description: {
        component: "Skapa ett nytt projekt i Markkoll."
      }
    }
  },
  argTypes: {
    projektversion: {
      description: "Projektversion"
    },
    isCurrent: {
      description: "Är detta den aktuella versionen"
    },
    isPrevious: {
      description: "Är detta den föregående versionen"
    },
    restoreVersionChange: {
      description: "Event när denna projektversionen ska återställas till den aktuella versionen"
    },
    deleteVersionChange: {
      description: "Event när denna projektversion skall tas bort"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkProjektversionModule,
        getTranslocoModule(),
      ],
      providers: [
        { provide: DialogService, use: MockService(DialogService) }
      ]
    }),
  ],
};

const Template: StoryFn<MkProjektversionComponent> = (args: MkProjektversionComponent) => ({
  props: {
    ...args,
    restoreVersionChange: action("restoreVersionChange"),
    deleteVersionChange: action("deleteVersionChange"),
  }
});

const projektVersion: Version = {
  id: "1",
  filnamn: "fil.zip",
  skapadDatum: new Date().toDateString(),
  buffert: 0
};

export const Base = Template.bind({});
Base.storyName = "Vanlig";
Base.args = {
  projektversion: projektVersion,
  isCurrent: false,
  isPrevious: false
};

export const Current = Template.bind({});
Current.storyName = "Nuvarande";
Current.args = {
  projektversion: projektVersion,
  isCurrent: true,
  isPrevious: false
};

export const Previous = Template.bind({});
Previous.storyName = "Previous";
Previous.args = {
  projektversion: projektVersion,
  isCurrent: false,
  isPrevious: true
};
