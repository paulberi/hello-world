import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { ProjektTyp, NisKalla } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkUploadFileComponent } from "./upload-file.component";
import { MkUploadFileModule } from "./upload-file.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Upload File",
  component: MkUploadFileComponent,
  parameters: {
    docs: {
      description: {
        component: "Ladda upp en fil till ett Markkoll-projekt med geografisk information om intrång. Sätt även NIS som används som indatakälla."
      }
    }
  },
  argTypes: {
    projektTyp: {
      description: "Filtrera drowndown med NIS beroende på projekttyp."
    },
    nisKalla: {
      description: "Filtrera drowndown med NIS beroende på inställningar."
    },
    disableValidation: {
      description: "Om komponenten ska validera att man har valt en godtagbar fil"
    },
    filesChange: {
      description: "Event när filer läggs till eller tas bort."
    },
    validChange: {
      description: "Event med en flagga om formuläret är rätt ifyllt."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkUploadFileModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkUploadFileComponent> = (args: MkUploadFileComponent) => ({
  props: {
    ...args,
    filesChange: action("filesChange"),
    validChange: action("validChange"),
  }
});

export const uploadFiber = Template.bind({});
uploadFiber.storyName = "Ladda upp till fiberprojekt";
uploadFiber.args = {
  projektTyp: ProjektTyp.FIBER,
  nisKalla: {} as NisKalla,
  disableValidation: true
};

export const uploadElnat = Template.bind({});
uploadElnat.storyName = "Ladda upp till elnätsprojekt";
uploadElnat.args = {
  projektTyp: ProjektTyp.LOKALNAT,
  nisKalla: {} as NisKalla,
  disableValidation: true
};
