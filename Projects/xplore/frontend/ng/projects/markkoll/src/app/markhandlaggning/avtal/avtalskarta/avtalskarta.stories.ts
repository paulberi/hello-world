import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkAvtalskartaComponent } from "./avtalskarta.component";
import { MkAvtalskartaModule } from "./avtalskarta.module";
import { testAvtalskartaMedEttFastighetsomrade, testAvtalskartaTvaFastighetsomraden } from "../../../../test/data";
import { HttpClientTestingModule } from "@angular/common/http/testing";

const Template: StoryFn<MkAvtalskartaComponent> = (args: MkAvtalskartaComponent) => ({
  props: args
});

export default {
  title: "Applikationer/Markkoll/Avtal/Avtalskarta",
  component: MkAvtalskartaComponent,
  parameters: {
    docs: {
      description: {
        component: "Avtalskarta med intrångsöversikt och intrångskarta."
      }
    }
  },
  argTypes: {
    avtalskarta: {
      description: "Information som behövs för att visa avtalskarta."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkAvtalskartaModule,
        getTranslocoModule(),
        HttpClientTestingModule
      ],
    })
  ]

} as Meta;

export const ettFastighetsområde = Template.bind({});
ettFastighetsområde.storyName = "Ett fastighetsområde";
ettFastighetsområde.args = {
  avtalskarta: testAvtalskartaMedEttFastighetsomrade
};

export const tvaFastighetsomraden = Template.bind({});
tvaFastighetsomraden.storyName = "Två fastighetsområden";
tvaFastighetsomraden.args = {
  avtalskarta: testAvtalskartaTvaFastighetsomraden
};
