import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { MMDinKartproduktModule } from './din-kartprodukt.module';
import { getTranslocoModule } from '../../../../../../lib/translate/transloco-testing.module';
import { DinKartproduktComponent } from "./din-kartprodukt.component";

export default {
  title: "Applikationer/Mitt Metria/Din kartprodukt",
  component: DinKartproduktComponent,
  parameters: {
    docs: {
      description: {
        component: "Visar information om en kartprodukt under pågående konfiguration."
      }
    }
  },
  argTypes: {
    header: {
      description: "Rubrik"
    },
    displayAttributes: {
      description: "Attributen som ska visas. Display name och value"
    },
    price: {
      description: "Pris"
    },
    currency: {
      description: "Valuta"
    },
    disableButton: {
      description: "Om knappen 'Lägg till i varukorg' ska vara inaktiverad"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MMDinKartproduktModule,
        getTranslocoModule()]
    }),
  ],

} as Meta;

const Template: StoryFn<DinKartproduktComponent> = (args: DinKartproduktComponent) => ({
  props: {
    ...args,
    removeChip: action("addToCart")
  }
});

export const kartprodukt = Template.bind({});

kartprodukt.args = {
  header: "Din kartprodukt",
  displayAttributes: [
    {
      displayName: "Produkt",
      value: "Fastighetsrapport"
    },
    {
      displayName: "Leveransformat",
      value: "PDF"
    },
    {
      displayName: "Fastighet",
      value: "UMEÅ KYCKLINGEN 4"
    }
  ],
  price: "15",
  currency: "kr",
  disableButton: false,
};
