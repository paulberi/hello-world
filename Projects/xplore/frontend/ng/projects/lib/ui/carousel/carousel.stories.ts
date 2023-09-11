import { CommonModule } from "@angular/common";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpCarouselComponent } from "./carousel.component";
import { mockCarouselItems } from "./carousel.mock";
import { XpCarouselModule } from "./carousel.module";

export default {
  title: "Komponenter/Navigation/Carousel",
  component: XpCarouselComponent,
  argTypes: {
    color: { 
      control: { type: "radio"},
      options: ["primary", "accent"],
      description: "Färg på pilarna och cirklarna." 
    },
    titlePosition: { 
      control: { type: "radio"},
      options: ["bottom", "on"],
      description: "Om titelns positions ska vara under eller på bilden." 
    },
    carouselItems: {
      description: "Innehåll i karusellen."
    },
    isTitleVisible: {
      description: "Om titel ska visas."
    },
    borderRadius: {
      description: "Border-radius på ramen i px."
    },
    imageClick: {
      description: "Event för klick på bild"
    }
  },
  parameters: {
    docs: {
      description: {
        component: "En responsiv bildkarusell."
      }
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        XpCarouselModule,
        getTranslocoModule()
      ]
    })
  ]
} as Meta

const Template: StoryFn<XpCarouselComponent> = (args: XpCarouselComponent) => ({
  props: {
    ...args,
    imageClick: action("imageClick")
  }
});

export const layers = Template.bind({});
layers.storyName = "Lagerhanterare";
layers.args = {
  color: "primary",
  carouselItems: mockCarouselItems,
  isTitleVisible: true,
  titlePosition: "bottom",
  borderRadius: 0
}

export const products = Template.bind({});
products.storyName = "Produkter";
products.args = {
  color: "accent",
  carouselItems: mockCarouselItems,
  isTitleVisible: true,
  titlePosition: "on",
  borderRadius: 10
};
