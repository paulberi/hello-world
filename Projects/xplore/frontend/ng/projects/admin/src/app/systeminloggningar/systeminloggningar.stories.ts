import { action } from "@storybook/addon-actions";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { AdmSysteminloggningarComponent } from "./systeminloggningar.component";
import { AdmSysteminloggningarModule } from "./systeminloggningar.module";

export default {
  title: "Applikationer/Admin/Systeminloggningar",
  component: AdmSysteminloggningarComponent,
  parameters: {
    docs: {
      description: {
        component: "En tabell med redigerbara systeminloggningar."
      }
    }
  },
  argTypes: {
    indexSelected: {
      description: "Index för den för nuvarande valda systeminloggningen"
    },
    metriaMapsAuthChange: {
      description: "Event som emittas när autentiseringsuppgifter för MetriaMaps uppdateras"
    },
    fastighetsokAuthChange: {
      description: "Event som emittas när autentiseringsuppgifter för FastighetSok uppdateras"
    },
    indexSelectedChange: {
      description: "Event som emittas när valt index ändras"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        AdmSysteminloggningarModule,
        getTranslocoModule()
      ],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmSysteminloggningarComponent> = (args: AdmSysteminloggningarComponent) => ({
  props: {
    ...args,
    metriaMapsAuthChange: action("metriaMapsAuthChange"),
    fastighetsokAuthChange: action("fastighetsokAuthChange"),
    authReset: action("authReset"),
    indexSelectedChange: action("indexSelectedChange")
  }
});

export const Systeminloggningar = Template.bind({});
Systeminloggningar.storyName = "Systeminloggningar";
Systeminloggningar.args = {
  auth: [
    {
      id: "0",
      username: "chka",
      password: "12345"
    },
    {
      id: "1",
      username: "fsok",
      password: "kosf",
      kundmarke: "kundmärke"
    },
  ],
  indexSelected: null
};

export const fsokUtfallt = Template.bind({});
fsokUtfallt.storyName = "Inloggningsuppgifter för Fastighetsök utfällda";
fsokUtfallt.args = {
  auth: [
    {
      id: "0",
      username: "chka",
      password: "12345"
    },
    {
      id: "1",
      username: "fsok",
      password: "kosf",
      kundmarke: "kundmärke"
    },
  ],
  indexSelected: 1,
};

export const mmapsUtfallt = Template.bind({});
mmapsUtfallt.storyName = "Inloggningsuppgifter för MetriaMaps utfällda";
mmapsUtfallt.args = {
  auth: [
    {
      id: "0",
      username: "chka",
      password: "12345"
    },
    {
      id: "1",
      username: "fsok",
      password: "kosf",
      kundmarke: "kundmärke"
    },
  ],
  indexSelected: 0,
};
