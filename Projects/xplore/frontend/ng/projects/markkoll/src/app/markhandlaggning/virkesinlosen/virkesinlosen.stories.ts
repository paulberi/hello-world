import {MkVirkesinlosenComponent} from "./virkesinlosen.component";
import {moduleMetadata, StoryFn} from "@storybook/angular";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {getTranslocoModule} from "../../../../../lib/translate/transloco-testing.module";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatButtonModule} from "@angular/material/button";
import {MkVirkesinlosenModule} from "./virkesinlosen.module";
import { TillvaratagandeTyp } from "../../../../../../generated/markkoll-api";
import { action } from "@storybook/addon-actions";

export default {
  title: "Applikationer/Markkoll/Avtal/Virkesinlösen",
  component: MkVirkesinlosenComponent,
  argTypes: {
    tillvaratagandeTyp: {
      control: { type: "radio" },
      options: [
        TillvaratagandeTyp.EJBESLUTAT,
        TillvaratagandeTyp.EGETTILLVARATAGANDE,
        TillvaratagandeTyp.ROTNETTO
      ],
    },
  },
  decorators: [
    moduleMetadata({
      imports: [
        CommonModule,
        MatFormFieldModule,
        getTranslocoModule(),
        MatInputModule,
        FormsModule,
        ReactiveFormsModule,
        MatSlideToggleModule,
        BrowserAnimationsModule,
        MatCheckboxModule,
        MatButtonModule,
        MatFormFieldModule,
        MkVirkesinlosenModule
      ]
    })
  ]
};

const Template: StoryFn<MkVirkesinlosenComponent> = (args: MkVirkesinlosenComponent) => ({
  props: {
    ...args,
    skogligVarderingChange: action("skogligVarderingChange"),
    tillvaratagandeTypChange: action("tillvaratagandeTypChange"),
    exportHms: action("exportHms"),
  }
});

export const Empty = Template.bind({});
Empty.storyName = "Tom";
Empty.args = {
  skogligVardering: false,
  tillvaratagandeTyp: TillvaratagandeTyp.ROTNETTO,
  rotnetto: 0,
  egetTillvaratagande: 0
};

export const EgetTillvaratagande = Template.bind({});
EgetTillvaratagande.storyName = "Eget tillvaratagande";
EgetTillvaratagande.args = {
  skogligVardering: true,
  tillvaratagandetyp: TillvaratagandeTyp.EGETTILLVARATAGANDE,
  rotnetto: 9001,
  egetTillvaratagande: 123
};

export const Rotnetto = Template.bind({});
Rotnetto.storyName = "Rotnetto";
Rotnetto.args = {
  skogligVardering: true,
  tillvaratagandeTyp: TillvaratagandeTyp.ROTNETTO,
  rotnetto: 9001,
  egetTillvaratagande: 123
};
