import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { MkLoggItem } from "../../model/loggItem";
import { MkLoggbokComponent } from "./loggbok.component";
import { MkMockLoggbok } from "./loggbok.mock";
import { MkLoggbokModule } from "./loggbok.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Loggbok",
  component: MkLoggbokComponent,
  parameters: {
    docs: {
      description: {
        component: "Loggbok för händelser i Markkoll."
      }
    }
  },
  argTypes: {
    title: {
      description: "Titel på loggbok."
    },
    loggbok: {
      description: "Loggar som ska visas."
    },
    emptyText: {
      description: "Text som visas om det inte finns några loggar."
    },
    isShowMoreVisible: {
      description: "Ska knapp för att visa fler vara synlig."
    },
    showMoreTitle: {
      description: "Titel för knapp som laddar in fler logghändelser."
    },
    onShowMore: {
      description: "Event när användaren klickar på Visa fler."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [MkLoggbokModule],
    }),
  ],
} as Meta;

const Template: StoryFn<MkLoggbokComponent> = (args: MkLoggbokComponent) => ({
  props: {...
    args,
    onShowMore: action("onShowMore")
  }
});

export const LoggbokFemLoggar = Template.bind({});
LoggbokFemLoggar.storyName = "Med loggar";
LoggbokFemLoggar.args = {
  loggbok: MkMockLoggbok,
  title: "Fastighetslogg",
  emptyText: "Det finns ingen händelse i loggboken ännu.",
  isShowMoreVisible: true,
  showMoreTitle: "Visa fler"
};

export const LoggbokUtanLoggar = Template.bind({});
LoggbokUtanLoggar.storyName = "Utan loggar";
LoggbokUtanLoggar.args = {
  loggbok: [] as MkLoggItem[],
  title: "Fastighetslogg",
  emptyText: "Det finns ingen händelse i loggboken ännu.",
  isShowMoreVisible: true,
  showMoreTitle: "Visa fler"
};
