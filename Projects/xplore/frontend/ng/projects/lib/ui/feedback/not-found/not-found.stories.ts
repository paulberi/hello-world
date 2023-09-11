import { HttpClientModule } from "@angular/common/http";
import { RouterTestingModule } from "@angular/router/testing";
import { Meta, StoryFn, moduleMetadata } from "@storybook/angular";
import { getTranslocoModule } from "../../../translate/transloco-testing.module";
import { XpNotFoundComponent } from "./not-found.component";
import { XpNotFoundModule } from "./not-found.module";

export default {
  title: "Komponenter/Feedback/Not found",
  component: XpNotFoundComponent,
  parameters: {
    docs: {
      description: {
        component: "Meddela användaren att det inte går att hitta sidan och ge möjlighet att navigera till en länk. Används tex. vid 404-svar från backend."
      }
    }
  },
  argTypes: {
    title: {
      description: "Titel som visas på sidan. Valfri."
    },
    linkTitle: {
      description: "Titel för länk. Valfri."
    },
    linkUrl: {
      description: "Url för länk. Valfri. Standardlänk är till roten."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        XpNotFoundModule,
        HttpClientModule,
        RouterTestingModule,
        getTranslocoModule(),
      ],
    }),
  ]
} as Meta;

const Template: StoryFn<XpNotFoundComponent> = (args: XpNotFoundComponent) => ({
  props: args,
});

export const Default = Template.bind({});
Default.storyName = "Ingen sida";
Default.args = {
  title: "Kunde inte hitta sidan",
  linkTitle: "Till startsidan",
  linkUrl: "/"
};

