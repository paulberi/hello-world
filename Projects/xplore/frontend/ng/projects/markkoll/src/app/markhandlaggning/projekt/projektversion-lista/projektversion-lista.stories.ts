import { moduleMetadata, StoryFn } from "@storybook/angular";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkProjektversionListaComponent } from "./projektversion-lista.component";
import { MkProjektversionListaModule } from "./projektversion-lista.module";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektversion Lista",
  component: MkProjektversionListaComponent,
  argTypes: {
    versioner: {
      description: "Lista med projektversioner"
    },
    restoreVersionChange: {
      description: "Event när en projektversion ska återställas"
    },
    deleteVersionChange: {
      description: "Event när en projektversion ska tas bort"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkProjektversionListaModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkProjektversionListaComponent> = (args: MkProjektversionListaComponent) => ({
  props: {
    ...args,
    restoreVersionChange: action("restoreVersionChange"),
    deleteVersionChange: action("deleteVersionChange"),
  }
});

export const Empty = Template.bind({});
Empty.storyName = "Tom lista";
Empty.args = {
  versioner: []
};

export const OneItem = Template.bind({});
OneItem.storyName = "En version";
OneItem.args = {
  versioner: [
    {
      id: 1,
      filnamn: "fil_v1.zip",
      skapadDatum: new Date().toDateString()
    }
  ]
};

export const TwoItems = Template.bind({});
TwoItems.storyName = "Två versioner";
TwoItems.args = {
  versioner: [
    {
      id: 2,
      filnamn: "fil_v2.zip",
      skapadDatum: new Date().toDateString()
    },
    ...OneItem.args.versioner
  ]
};

export const ThreeItems = Template.bind({});
ThreeItems.storyName = "Tre versioner";
ThreeItems.args = {
  versioner: [
    {
      id: 3,
      filnamn: "fil_v3.zip",
      skapadDatum: new Date().toDateString()
    },
    ...TwoItems.args.versioner
  ]
};

export const FourItems = Template.bind({});
FourItems.storyName = "Fyra versioner";
FourItems.args = {
  versioner: [
    {
      id: 4,
      filnamn: "fil_v4.zip",
      skapadDatum: new Date().toDateString()
    },
    ...ThreeItems.args.versioner
  ]
};
