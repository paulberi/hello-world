import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { BannerComponent } from "./banner.component";
import { MMBannerModule } from "./banner.module";

export default {
  title: "Applikationer/Mitt Metria/Banner",
  component: BannerComponent,
  decorators: [
    moduleMetadata({
      imports: [
        MMBannerModule,
        getTranslocoModule()
      ]
    })
  ]
} as Meta

const Template: StoryFn<BannerComponent> = (args: BannerComponent) => ({
  props: {
    ...args
  }
});

export const bannerWithText = Template.bind({});
bannerWithText.storyName = "Sida med bannerbild";
bannerWithText.args = {
  fullImage: "assets/mockBannerImage1.jpg",
  tabletOrMobileImage: "assets/mockBannerImage1.jpg",
  altText: "Vindkraftverk",
}