/* Det är något som är trasigt när jag försöker skriva stories till den här komponenten. Tycks som
om samma modul importeras flera gånger. Får t.ex. det här felet:

ERROR Error: NG0300: Multiple components match node with tagname mk-haglofimport.

Sker i storybook, men inte i hela webappen. Kan inte hitta var felet ligger, och jag hittar ingenting
på Google om andra som haft samma problem i Storybook. Jag går bet och kommenterar ut tills vidare.
*/

/*import { moduleMetadata, Story } from "@storybook/angular";
import { ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { ImportTyp } from "../../../model/importTyp";
import { MkProjektimporterComponent } from "./projektimporter.component";
import { MkProjektimporterModule } from "./projektimporter.module";
import { HttpClientTestingModule } from "@angular/common/http/testing";

export default {
  title: "Markkoll/Markhandläggning/Projekt/Projektimporter",
  component: MkProjektimporterComponent,
  decorators: [
    moduleMetadata({
      imports: [
        MkProjektimporterModule,
        getTranslocoModule(),
      ],
      providers: [
        HttpClientTestingModule
      ]
    }),
  ],
};

const Template: Story<MkProjektimporterComponent> = args => ({
  props: args
});

export const Projektimporter = Template.bind({});
Projektimporter.storyname = "Haglöf Import";
Projektimporter.args = {
  importTyp: ImportTyp.HAGLOF,
  projektTyp: ProjektTyp.FIBER
};
*/
