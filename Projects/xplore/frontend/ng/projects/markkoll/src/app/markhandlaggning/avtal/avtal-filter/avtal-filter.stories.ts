import { moduleMetadata, StoryFn } from "@storybook/angular";
import { MkAvtalFilterComponent } from "./avtal-filter.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkAvtalFilterModule } from "./avtal-filter.module";
import { action } from "@storybook/addon-actions";

const Template: StoryFn<MkAvtalFilterComponent> = (args: MkAvtalFilterComponent) => ({
  props: {
    ...args,
    filterChange: action("filterChange")
  }
});

export default {
  title: "Applikationer/Markkoll/Avtal/Filter",
  component: MkAvtalFilterComponent,
  parameters: {
    docs: {
      description: {
        component: "Filtrera på avtal efter önskade filterinställningar"
      }
    }
  },
  argTypes: {
    filter: {
      description: "Filterinställningar"
    },
    filterChange: {
      description: "Emittas när ändring har skett i filterinställningarna"
    },
    statusOptions: {
      description: "Menyval för avtalsstatusar"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        MkAvtalFilterModule
      ]
    })
  ]
};

export const Default = Template.bind({});
Default.storyName = "Fastighetsbeteckning och avtalsstatus";
Default.args = {
  filter: {
    search: "Höljes 1:23",
    status: "AVTALSKICKAT"
  },
  statusOptions: [
    {
      value: "EJBEHANDLAT",
      label: "Ej behandlat"
    },
    {
      value: "AVTALSKICKAT",
      label: "Avtal skickat"
    },
    {
      value: "AVTALSKONFLIKT",
      label: "Avtalskonflikt"
    }
  ]
};
