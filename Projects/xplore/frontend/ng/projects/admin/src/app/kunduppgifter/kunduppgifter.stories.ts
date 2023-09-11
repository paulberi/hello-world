import { ErrorStateMatcher } from "@angular/material/core";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { AdmKunduppgifterComponent } from "./kunduppgifter.component";
import { AdmKunduppgifterModule } from "./kunduppgifter.module";
import { ShowErrorStateMatcher } from "../show-error-state-matcher";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Admin/Kund/Kunduppgifter",
  component: AdmKunduppgifterComponent,
  decorators: [
    moduleMetadata({
      providers: [{ provide: ErrorStateMatcher, useClass: ShowErrorStateMatcher }],
      imports: [AdmKunduppgifterModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmKunduppgifterComponent> = (args: AdmKunduppgifterComponent) => ({
  props: {
    ...args,
    kundChange: action("kundChange"),
    kundDelete: action("kundDelete")
  }
});

export const Kunduppgifter = Template.bind({});
Kunduppgifter.storyName = "Kunduppgifter";
Kunduppgifter.args = {
  kund: {
    namn: "Piteå Fiber AB",
    kontaktperson: "Christoffer Karlsson",
    epost: "chka@pifab.se",
    telefon: "0701211212"
  },
};

export const OfullstandigaUppgifter = Template.bind({});
OfullstandigaUppgifter.storyName = "Ofullständiga uppgifter"
OfullstandigaUppgifter.args = {
  kund: {
    kontaktperson: "Christoffer Karlsson",
    epost: "chka@pifab.se",
    telefon: "0701211212"
  },
};
