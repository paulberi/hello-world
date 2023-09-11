import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { HaglofImportVarningar } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkHaglofImportComponent } from "./haglofimport.component";
import { MkHaglofImportModule } from "./haglogimport.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Haglöf Import",
  component: MkHaglofImportComponent,
  argTypes: {
    warnings: {
      description: "Genererade varningar från importen"
    },
    import: {
      description: "Event när användaren vill importa en fil"
    },
    closeWarnings: {
      description: "Event när användaren stänger varningsrutan"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkHaglofImportModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkHaglofImportComponent> = (args: MkHaglofImportComponent) => ({
  props: {
    ...args,
    import: action("import"),
    closeWarnings: action("closeWarnings")
  }
});

const warnings: HaglofImportVarningar = {
  fastigheterMissing: ["Taj Mahal", "Eiffeltornet"]
};

export const HaglofImport = Template.bind({});
HaglofImport.storyname = "Haglöf Import";
HaglofImport.args = {
  warnings: warnings
};
