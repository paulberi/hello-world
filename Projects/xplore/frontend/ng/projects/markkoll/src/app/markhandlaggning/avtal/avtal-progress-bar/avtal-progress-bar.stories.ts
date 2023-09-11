import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { MkAvtalProgressBarComponent } from "./avtal-progress-bar.component";
import { Avtalsstatus } from "../../../../../../../generated/markkoll-api";
import { MkAvtalProgressBarModule } from "./avtal-progress-bar.module";

export default {
  title: "Applikationer/Markkoll/Avtal/Progress Bar",
  component: MkAvtalProgressBarComponent,
  parameters: {
    docs: {
      description: {
        component: "Visa progressen på ett avtal."
      }
    }
  },
  argTypes: {
    avtalsstatus: {
      control: { type: "radio" },
      options: [
        Avtalsstatus.AVTALJUSTERAS,
        Avtalsstatus.AVTALSIGNERAT,
        Avtalsstatus.AVTALSKICKAT,
        Avtalsstatus.AVTALSKONFLIKT,
        Avtalsstatus.EJBEHANDLAT,
        Avtalsstatus.ERSATTNINGUTBETALAS,
        Avtalsstatus.ERSATTNINGUTBETALD,
        Avtalsstatus.PAMINNELSESKICKAD
      ],
      description: "Avtalets status"
    },
    labelPaborjat: {
      description: "Påbörjat, etikett placerad längst till vänster"
    },
    labelAvslutat: {
      description: "Avslutat, etikett placerad längst till höger"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [MkAvtalProgressBarModule],
    }),
  ],
} as Meta;

const template: StoryFn<MkAvtalProgressBarComponent> = (
  args: MkAvtalProgressBarComponent
) => ({
  props: args,
});

export const avtalskonflikt = template.bind({});
avtalskonflikt.storyName = "Avtalskonflikt";
avtalskonflikt.args = {
  avtalsstatus: Avtalsstatus.AVTALSKONFLIKT,
  labelPaborjat: "Påbörjat",
  labelAvslutat: "Avslutat"
};

export const ejBehandlat = template.bind({});
ejBehandlat.storyName = "Ej behandlat";
ejBehandlat.args = {
  avtalsstatus: Avtalsstatus.EJBEHANDLAT,
  labelPaborjat: "Påbörjat",
  labelAvslutat: "Avslutat"
};

export const avtalSkickat = template.bind({});
avtalSkickat.storyName = "Avtal skickat";
avtalSkickat.args = {
  avtalsstatus: Avtalsstatus.AVTALSKICKAT,
  labelPaborjat: "Påbörjat",
  labelAvslutat: "Avslutat"
};

export const avtalJusteras = template.bind({});
avtalJusteras.storyName = "Avtal justeras";
avtalJusteras.args = {
  avtalsstatus: Avtalsstatus.AVTALJUSTERAS,
  labelPaborjat: "Påbörjat",
  labelAvslutat: "Avslutat"
};

export const avtalSignerat = template.bind({});
avtalSignerat.storyName = "Avtal signerat";
avtalSignerat.args = {
  avtalsstatus: Avtalsstatus.AVTALSIGNERAT,
  labelPaborjat: "Påbörjat",
  labelAvslutat: "Avslutat"
};
