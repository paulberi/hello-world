import { MMUtcheckningProcessUiModule } from './utcheckning-process-ui.module';
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { action } from '@storybook/addon-actions';
import { UtcheckningProcessUiComponent } from './utcheckning-process-ui.component';

export default {
  title: "Applikationer/Mitt Metria/Utcheckning",
  component: UtcheckningProcessUiComponent,
  parameters: {
    docs: {
      description: {
        component: "Utcheckning innehållande leveransinformation och val av betalmetod"
      }
    }
  },
  argTypes: {
    customerFirstName: {
      description: "Kundens förnamn"
    },
    customerLastName: {
      description: "Kundens efternamn"
    },
    customerEmail: {
      description: "Kundens email"
    },
    termsLink: {
      description: "Länk till villkor"
    },
    price: {
      description: "Pris inklusive moms"
    },
    vat: {
      description: "Moms"
    },
    currency: {
      description: "Valuta för price och vat"
    },
    paymentMethods: {
      description: "Betalningsmetoder, code och name."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MMUtcheckningProcessUiModule
      ],
    }),
  ],

} as Meta;

const Template: StoryFn<UtcheckningProcessUiComponent> = (args: UtcheckningProcessUiComponent) => ({
  props: {
    ...args,
    pay: action("pay")
  }
});

export const utcheckning = Template.bind({});
utcheckning.args = {
  customer: {
    firstName: "Toi",
    lastName: "Story",
    emailAddress: "toi.story@mail.com",
  },
  termsLink: "/lank/till/villkor",
  price: "1379",
  vat: "275.80",
  currency: "kr",
  paymentMethods: [
    {
      name: "Faktura",
      code: "invoice"
    },
    {
      name: "Visa/Mastercard",
      code: "card"
    }
  ],
  invoiceRef: "12345678"

};
