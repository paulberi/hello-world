import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";

import { ProductListComponent } from "./product-list.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MMProductCardModule } from "../product-card/product-card.module";

export default {
  title: "Applikationer/Mitt Metria/Product list",
  component: ProductListComponent,
  parameters: {
    docs: {
      description: {
        component: "Lista med produktkort"
      }
    }
  },
  argTypes: {
    products: {
      description: "Produkter att visa i listan"
    },
    showLoadMore: {
      description: "Om load more-knappen ska visas"
    },
    loadMoreButtonText: {
      description: "Text för knappen"
    },
  },
  decorators: [
    moduleMetadata({
      imports: [
        MMProductCardModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<ProductListComponent> = (args: ProductListComponent) => ({
  props: {
    ...args,
    loadMore: action("loadMore"),
    selectedProduct: action("selectedProduct")
  }
});

export const productList = Template.bind({});



productList.args = {
  products: [
    {
      id: 0,
      image: "",
      name: "Skogsanalys",
      description: "Metria Skogsanalys samlar all nödvändig information i en enkel och intuitiv webbtjänst för analyser av skogliga data. Med aktuella och faktabaserade underlag blir värdering av skogs- eller lantbruksfastigheter mer exakt.",
    },
    {
      id: 1,
      image: "",
      name: "Miljökoll",
      description: "Metria Miljökoll är ett digitalt stöd för att samla in, granska, validera och rapportera miljödata. Tjänsten är utvecklad för att stödja genomförande och uppföljning av kontrollprogram i syfte att följa upp miljöpåverkan av exempelvis en industriell verksamhet eller under ett infrastrukturprojekt.",
    }
  ],
  loadMore: true,
  loadMoreButtonText: "Visa fler produkter"
};
