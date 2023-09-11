import { moduleMetadata, StoryFn } from "@storybook/angular";
import { CommonModule } from "@angular/common";
import { MkProjektdokumentModule } from "./projektdokument.module";
import { MkProjektdokumentComponent } from "./projektdokument.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { Dokumentmall, DokumentTyp } from "../../../../../../../generated/markkoll-api";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { OAuthModule } from "angular-oauth2-oidc";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektdokument Skapa",
  component: MkProjektdokumentComponent,
  argTypes: {
    dokumenttyper: {
      description: "Vilka dokumenttyper man ska kunna välja på när man skapar dokument"
    },
    dokument: {
      description: "Dokument att lista"
    },
    dokumentCreate: {
      description: "Information om dokumentet som ska skapas"
    },
    dokumentPrepare: {
      description: "Information om dokumentet som ska behandlas"
    },
    dokumentChange: {
      description: "En uppdaterad version av ett dokument"
    },
    dokumentDelete: {
      description: "Dokumentet man vill ta bort"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        CommonModule,
        getTranslocoModule(),
        MkProjektdokumentModule,
        HttpClientTestingModule,
        OAuthModule.forRoot()
      ]
    })
  ]
};

const Template: StoryFn<MkProjektdokumentComponent> = (args: MkProjektdokumentComponent) => ({
  props: {
    ...args,
    dokumentCreate: action("dokumentCreate"),
    dokumentPrepare: action("dokumentPrepare"),
    dokumentChange: action("dokumentChange"),
    dokumentDelete: action("dokumentDelete")
  }
});

export const Default = Template.bind({});
Default.storyName = "Skapa";
Default.args = {
  dokumenttyper: [{value: DokumentTyp.INFOBREV, label: "Infobrev"}, {value: DokumentTyp.MARKUPPLATELSEAVTAL, label: "Avtal"}],
  dokument: [] as Dokumentmall[]
};
