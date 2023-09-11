import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MkAvtalListComponent } from "./avtal-list.component";
import { MkAvtalListModule } from "./avtal-list.module";
import { RouterTestingModule } from "@angular/router/testing";
import { AvtalsjobbStatus, ProjektTyp } from "../../../../../../../generated/markkoll-api/model/models";
import { OAuthModule } from "angular-oauth2-oidc";
import { action } from "@storybook/addon-actions";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { MkAvtalSummary } from "../../../model/avtalSummary";
import { OptionItem } from "../../../common/filter-option/filter-option.component";

const Template: StoryFn<MkAvtalListComponent> = (args: MkAvtalListComponent) => ({
  props: {...
    args,
    hamtaMarkagareClick: action("hamtaMarkagareClick"),
    fastighetIndexChange: action("fastighetIndexChange"),
    samfallighetIndexChange: action("samfallighetIndexChange"),
    genericPageChange: action("genericPageChange"),
    fastighetRemove: action("fastighetRemove"),
    fastighetChange: action("fastighetChange"),
    samfallighetRemove: action("samfallighetRemove"),
    samfallighetChange: action("samfallighetChange"),
    selectionStatusChange: action("selectionStatusChange"),
  }
});

export default {
  title: "Applikationer/Markkoll/Avtal/Avtalslista",
  component: MkAvtalListComponent,
  decorators: [
    moduleMetadata({
      imports: [
        MkAvtalListModule,
        RouterTestingModule,
        getTranslocoModule(),
        OAuthModule.forRoot()
      ],
    }),
  ],
} as Meta;

export const Fastigheter = Template.bind({});
Fastigheter.storyName = "Fastigheter";
Fastigheter.args = {
  projektId: "123",
  projektnamn: "Projektet",
  projektTyp: ProjektTyp.FIBER,
  tabIndex: 0,
  fastighetIndex: null as number,
  samfallighetPage: null as  XpPage<MkAvtalSummary> ,
  samfallighetIndex: null as number,
  statusFilterOptions: [] as OptionItem[],
  hamtaMarkagareDisabled: false,
  skapaAvtalDisabled: false,
  goBackRouterLink: ["/projekt/"],
  showCompareToPreviousVersion: false,
  numOfAvtalSelected: 0,
  fastighetPage: {
    number: 0,
    numberOfElements: 5,
    totalElements: 5,
    totalPages: 1,
    content: [
      {
        fastighetId: "0",
        fastighetsbeteckning: "Höljes 1:9",
        labels: { avvikelse: true }
      },
      {
        fastighetId: "1",
        fastighetsbeteckning: "Höljes 1:10",
        labels: { borttagenFastighet: true }
      },
      {
        fastighetId: "2",
        fastighetsbeteckning: "Höljes 1:11",
        labels: { nyFastighet: true }
      },
      {
        fastighetId: "3",
        fastighetsbeteckning: "Höljes 1:12",
        labels: { outredd: true }
      },
      {
        fastighetId: "4",
        fastighetsbeteckning: "Höljes 1:13",
        labels: { uppdateradFastighet: true }
      },
    ]
  },
  pageSizeOptions: [5, 10],
  avtalsfilter: { status: null, search: null },
  avtalsjobbProgress: {
    id: null,
    status: AvtalsjobbStatus.NONE,
    generated: 0,
    total: 0
  },
};

export const Samfalligheter = Template.bind({});
Samfalligheter.storyName = "Samfälligheter";
Samfalligheter.args = {
  projektId: "123",
  projektnamn: "Projektet",
  tabIndex: 1,
  projektTyp: ProjektTyp.FIBER,
  fastighetIndex: null as number,
  fastighetPage: null as  XpPage<MkAvtalSummary> ,
  samfallighetIndex: null as number,
  statusFilterOptions: [] as OptionItem[],
  hamtaMarkagareDisabled: false,
  skapaAvtalDisabled: false,
  goBackRouterLink: ["/projekt/"],
  showCompareToPreviousVersion: false,
  numOfAvtalSelected: 0,
  samfallighetPage: {
    number: 0,
    numberOfElements: 2,
    totalElements: 2,
    totalPages: 1,
    content: [
      {
        fastighetId: "0",
        fastighetsbeteckning: "Höljes S1:9",
        labels: { avvikelse: true }
      },
      {
        fastighetId: "1",
        fastighetsbeteckning: "Höljes S1:10",
        labels: { borttagenFastighet: true }
      },
    ],
  },
  pageSizeOptions: [5, 10],
  avtalsfilter:  { status: null, search: null },
  avtalsjobbProgress: {
    id: null,
    status: AvtalsjobbStatus.NONE,
    generated: 0,
    total: 0
  },
};
