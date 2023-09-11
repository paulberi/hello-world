import { MatTableModule } from "@angular/material/table";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { MkAgareTableComponent } from "./agare-table.component";
import { MkAgareTableModule } from "./agare-table.module";
import { MkAgareEditModule } from "../agare-edit/agare-edit.module";
import { testMarkagare, testMarkagare2 } from "../../../test/data";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Avtal/Ägare/Table",
  component: MkAgareTableComponent,
  parameters: {
    docs: {
      description: {
        component: "En tabell med markägare. Inkluderar möjlighet för en utfällbar panel för varje tabellrad."
      }
    }
  },
  argTypes: {
    agare: {
      description: "Lista med markägare"
    },
    title: {
      description: "Tabelltitel. Dyker upp som headerrubrik i första kolumnen"
    },
    index: {
      description: "Valt tabellindex"
    },
    isSigneraAvtalVisible: {
      description: "Visa kolumnen \"Signera avtal\""
    },
    showHeaders: {
      description: "Visa tabellheaders"
    },
    indexChange: {
      description: "Event när tabellindex ändras"
    },
    agareChange: {
      description: "Event när en markägare har ändrats"
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkAgareTableModule,
        MkAgareEditModule,
        MatTableModule,
        getTranslocoModule()
      ]
    })
  ]
};

const Template: StoryFn<MkAgareTableComponent> = (args: MkAgareTableComponent) => ({
  props: {
    ...args,
    indexChange: action("indexChange"),
    agareChange: action("agareChange")
  },
  template: `
    <mk-agare-table [agare]="agare" title="Lagfarna ägare" index="0"
                    [isSigneraAvtalVisible]="isSigneraAvtalVisible"
                    [showHeaders]="showHeaders">
      <ng-template let-element>
        <mk-agare-edit [agare]="element"></mk-agare-edit>
      </ng-template>
    </mk-agare-table>
  `
});

export const withColumn = Template.bind({});
withColumn.storyName = "Med 'Signera avtal'";
withColumn.args = {
  agare: [
    testMarkagare(),
    testMarkagare2()
  ],
  title: "",
  index: null,
  isSigneraAvtalVisible: true,
  showHeaders: true
};

export const withoutColumn = Template.bind({});
withoutColumn.storyName = "Utan 'Signera avtal'";
withoutColumn.args = {
  agare: [
    testMarkagare(),
    testMarkagare()
  ],
  title: "",
  index: null,
  isSigneraAvtalVisible: false,
  showHeaders: true
};

export const hideHeaders = Template.bind({});
hideHeaders.storyName = "Utan rubriker";
hideHeaders.args = {
  agare: [
    testMarkagare(),
    testMarkagare()
  ],
  title: "", 
  index: null,
  isSigneraAvtalVisible: true,
  showHeaders: false
};
