import { ErrorStateMatcher } from "@angular/material/core";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, Meta, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { ShowErrorStateMatcher } from "../show-error-state-matcher";
import { AdmMetriaMapsAuthFormComponent } from "./metria-maps-auth-form.component";
import { AdmMetriaMapsAuthFormModule } from "./metria-maps-auth-form.module";

export default {
  title: "Applikationer/Admin/Inloggningsuppgifter",
  component: AdmMetriaMapsAuthFormComponent,
  parameters: {
    docs: {
      description: {
        component: "Ett formulär med inloggningsuppgifter"
      }
    }
  },
  argTypes: {
    metriaMapsAuth: {
      description: "Autentiseringsuppgifter för MetriaMaps"
    },
    metriaMapsAuthChange: {
      description: "Event som emittas när autentiseringsuppgifter uppdateras"
    },
    cancel: {
      description: "Event som emittas när man avbryter processen"
    },
    reset: {
      description: "Event som emittas när användarne vill radera systeminloggningen"
    }
  },
  decorators: [
    moduleMetadata({
      providers: [{ provide: ErrorStateMatcher, useClass: ShowErrorStateMatcher }],
      imports: [AdmMetriaMapsAuthFormModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmMetriaMapsAuthFormComponent> = (args: AdmMetriaMapsAuthFormComponent) => ({
  props: {
    ...args,
    metriaMapsAuthChange: action("metriaMapsAuthChange"),
    cancel: action("cancel"),
    reset: action("reset")
  }
});

export const Inloggningsuppgifter = Template.bind({});
Inloggningsuppgifter.storyName = "Inloggningsuppgifter";
Inloggningsuppgifter.args = {
  metriaMapsAuth: {
    id: "id",
    system: "METRIA_MAPS",
    username: "chka",
    password: "pa77word"
  },
};

export const OfullstandigaUppgifter = Template.bind({});
OfullstandigaUppgifter.storyName = "Ofullständioga uppgifter";
OfullstandigaUppgifter.args = {
  metriaMapsAuth: {
    id: "id",
    system: "METRIA_MAPS",
    username: null,
    password: null
  },
};
