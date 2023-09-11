import { Meta, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../lib/translate/transloco-testing.module";
import { AdmAbonnemangComponent } from "./abonnemang.component";
import { AdmAbonnemangModule } from "./abonnemang.module";
import { moduleMetadata } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { Abonnemang as AbonnemangInterface } from "../../../../../generated/kundconfig-api";

export default {
  title: "Applikationer/Admin/Produkter & Tjänster",
  component: AdmAbonnemangComponent,
  decorators: [
    moduleMetadata({
      imports: [AdmAbonnemangModule, getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<AdmAbonnemangComponent> = (args: AdmAbonnemangComponent) => ({
  props: {
    ...args,
    abonnemangAdd: action("abonnemangAdd"),
    abonnemangDelete: action("abonnemangDelete"),
    showFormChange: action("showFormChange") 
  }
});

export const Abonnemang = Template.bind({});
Abonnemang.storyName = "Abonnemang";
Abonnemang.args = {
  abonnemang: [
    { produkt: "MARKKOLL", typ: "GULD" }
  ],
  produkter: [
    "MARKKOLL",
    "MILJOKOLL"
  ],
  abonnemangTyper: [
    "GULD",
    "SILVER",
  ],
  showForm: false
};

export const VisaForm = Template.bind({});
VisaForm.storyName = "Visa formulär";
VisaForm.args = {
  abonnemang: [
    { produkt: "MARKKOLL", typ: "GULD" }
  ],
  produkter: [
    "MARKKOLL",
    "MILJOKOLL"
  ],
  abonnemangTyper: [
    "GULD",
    "SILVER",
  ],
  showForm: true
}

export const NoAbonnemang = Template.bind({});
NoAbonnemang.storyName = "Inga abonnemang";
NoAbonnemang.args = {
  abonnemang: [] as AbonnemangInterface[],
  produkter: [] as string[],
  abonnemangType: [] as string[],
  showForm: true
}
