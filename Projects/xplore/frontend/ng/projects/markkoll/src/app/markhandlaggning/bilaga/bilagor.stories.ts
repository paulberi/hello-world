import { action } from "@storybook/addon-actions";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { MkBilagorComponent } from "./bilagor.component";
import { MkBilagorModule } from "./bilagor.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Bilagor",
  component: MkBilagorComponent,
  decorators: [
    moduleMetadata({
      imports: [
        MkBilagorModule,
        getTranslocoModule(),
      ],
    }),
  ],
} as Meta;

const template: StoryFn<MkBilagorComponent> = (
  args: MkBilagorComponent
) => ({
  props: {
    ...args,
    addBilaga: action("addBilaga"),
    removeBilaga: action("removeBilaga"),
    downloadBilaga: action("downloadBilaga"),
    sortChange: action("sortChange")
  }
});

export const bilagor = template.bind({});
bilagor.storyName = "Bilagor";
bilagor.args = {
  bilagor: [
    {
      fil: {
        filnamn: "Bilaga 1"
      },
      skapadDatum: "2021-09-01",
      typ: "AKERNORM_74"
    },
    {
      fil: {
        filnamn: "Bilaga 1",
      },
      skapadDatum: "2021-09-03",
      typ: "ROTNETTO"
    },
  ]
};
