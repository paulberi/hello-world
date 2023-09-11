import { MkProjektinformationComponent } from "./projektinformation.component";
import { moduleMetadata, StoryFn } from "@storybook/angular";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MkProjektinformationModule } from "./projektinformation.module";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { Beredare, IndataTyp, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { UserOption, UserRoleEntry } from "../user-roles/user-roles.component";
import { Projekt } from "../../../model/projekt";
import { action } from "@storybook/addon-actions";
import { State } from "../../../model/loadState";

export default {
  title: "Applikationer/Markkoll/Projekt/Projektinformation",
  component: MkProjektinformationComponent,
  decorators: [
    moduleMetadata({
      imports: [
        BrowserAnimationsModule,
        MkProjektinformationModule,
        RouterTestingModule,
        HttpClientTestingModule,
        MatMomentDateModule,
        getTranslocoModule()
      ],
    }),
  ],
};

const Template: StoryFn<MkProjektinformationComponent> = (args: MkProjektinformationComponent) => ({
  props: {
    ...args,
    updateProjekt: action("updateProjekt"),
    deleteProjekt: action("deleteProjekt")
  }
});

export const ProjektWithVersions = Template.bind({});
ProjektWithVersions.storyName = "Projekt med versioner";
ProjektWithVersions.args = {
  projekt: {
    projektInfo: {
      id: "91b8e3dd-daea-4a1b-8b58-f894d60d7004",
      namn: "baronens nya intrång",
      ort: "Umeå",
      projektTyp: ProjektTyp.FIBER,
      description: "Här skall inträngas!",
      startDate: new Date()
    },
    fiberInfo: {
      ledningsagare: "Baronen",
      bestallare: "Baronen",
      bidragsprojekt: true,
      ledningsstracka: "------>"
    },
    indataTyp: IndataTyp.DPCOM
  },
  hasVersioner: true,
  showVersionMessage: true,
  beredare: {} as Beredare,
  ledningsagareOptions: [] as string[],
  isDeletingProjekt: false,
  projektState: State.Loading,
  projektRoleEntries: [] as UserRoleEntry[],
  userOptions: [] as UserOption[]
};

export const ProjektWithoutVersions = Template.bind({});
ProjektWithoutVersions.storyName = "Projekt utan versioner";
ProjektWithoutVersions.args = {
  projekt: {
    projektInfo: {
      id: "91b8e3dd-daea-4a1b-8b58-f894d60d7004",
      namn: "baronens nya intrång",
      ort: "Umeå",
      projektTyp: ProjektTyp.FIBER,
      description: "Här skall inträngas!",
      startDate: new Date()
    },
    fiberInfo: {
      ledningsagare: "Baronen",
      bestallare: "Baronen",
      bidragsprojekt: true,
      ledningsstracka: "------>"
    },
    indataTyp: IndataTyp.DPCOM
  },
  showVersionMessage: true,
  beredare: {} as Beredare,
  ledningsagareOptions: [] as string[],
  isDeletingProjekt: false,
  projektState: State.Loading,
  projektRoleEntries: [] as UserRoleEntry[],
  userOptions: [] as UserOption[]
};

export const Tom = Template.bind({});
Tom.storyName = "Inget projekt";
Tom.args = {
  versionerSorted: [],
  projekt: {} as Projekt,
  showVersionMessage: true,
  beredare: {} as Beredare,
  ledningsagareOptions: [] as string[],
  isDeletingProjekt: false,
  projektState: State.Loading,
  projektRoleEntries: [] as UserRoleEntry[],
  userOptions: [] as UserOption[]
};
