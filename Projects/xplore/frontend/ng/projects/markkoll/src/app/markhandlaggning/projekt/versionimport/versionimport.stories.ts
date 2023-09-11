import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { NisKalla, ProjektTyp, Version } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkVersionImportComponent } from "./versionimport.component";
import { MkVersionImportModule } from "./versionimport.module";

export default {
  title: "Applikationer/Markkoll/Projekt/Versionimport",
  component: MkVersionImportComponent,
  argTypes: {
    versioner: {
      description: "Lista med projektversioner "
    },
    isImporting: {
      description: "Om import av ny version pågår "
    },
    projektTyp: {
      description: "Projekttyp"
    },
    nisKalla: {
      description: "NIS-Källa "
    },
    importVersion: {
      description: "Event när användaren importerar en ny version"
    },
    restoreVersion: {
      description: "Event när användaren återställer en version"
    },
    deleteVersion: {
      description: "Event när användaren importerar tar bort en version"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkVersionImportModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<MkVersionImportComponent> = (args: MkVersionImportComponent) => ({
  props: {
    ...args,
    importVersion: action("importVersion"),
    restoreVersion: action("restoreVersion"),
    deleteVersion: action("deleteVersion")
  }
});

const versioner: Version[] = [
  {
    id: "id",
    filnamn: "Version 2",
    skapadDatum: "2012-12-12",
    buffert: 0
  },
  {
    id: "id",
    filnamn: "Version 3",
    skapadDatum: "2003-03-03",
    buffert: 0
  },
  {
    id: "id",
    filnamn: "Version 1",
    skapadDatum: "2022-22-02",
    buffert: 0
  },
];

export const VersionImport = Template.bind({});
VersionImport.StoryName = "Versionimporter";
VersionImport.args = {
  versioner: versioner,
  isImporting: false,
  projektTyp: ProjektTyp.FIBER,
  nisKalla: {} as NisKalla
};

export const Importerar = Template.bind({});
Importerar.StoryName = "Importerar version";
Importerar.args = {
  versioner: versioner,
  isImporting: true,
  projektTyp: ProjektTyp.LOKALNAT,
  nisKalla: {} as NisKalla
};

export const IngaVersioner = Template.bind({});
IngaVersioner.StoryName = "Inga importversioner";
IngaVersioner.args = {
  versioner: [],
  isImporting: false,
  projektTyp: ProjektTyp.REGIONNAT,
  nisKalla: {} as NisKalla
};
