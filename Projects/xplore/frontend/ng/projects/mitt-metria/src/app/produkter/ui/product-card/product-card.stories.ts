import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { ProductCardComponent } from "./product-card.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MMProductCardModule } from "./product-card.module";

export default {
  title: "Applikationer/Mitt Metria/Product card",
  component: ProductCardComponent,
  parameters: {
    docs: {
      description: {
        component: "Kort som kan användas för att visa information om ex. en produkt eller en tjänst. Använder materials mat-card."
      }
    }
  },
  argTypes: {
    imageUrl: { 
      description: "Url till bild" 
    },
    header: {
      description: "Titel"
    },
    description: {
      description: "Beskrivning"
    },
    tags: {
      description: "Taggar som innehåller ett namn och en icon"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MMProductCardModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<ProductCardComponent> = (args: ProductCardComponent) => ({
  props: {
    ...args,
    selectedItem: action("selectedItem")
  }
});

export const productCard = Template.bind({});
productCard.args = {
  imageUrl: "",
  header: "Skogsanalys",
  description: "Metria Skogsanalys samlar all nödvändig information i en enkel och intuitiv webbtjänst för analyser av skogliga data. Med aktuella och faktabaserade underlag blir värdering av skogs- eller lantbruksfastigheter mer exakt.",
  tags: [],
};

