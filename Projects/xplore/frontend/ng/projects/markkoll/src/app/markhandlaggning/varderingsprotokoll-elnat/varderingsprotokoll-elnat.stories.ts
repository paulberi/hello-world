import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import { VarderingConfig, VARDERING_CONFIG } from "../../services/vardering-elnat.service";
import { MkElnatVarderingsprotokollComponent } from "./varderingsprotokoll-elnat.component";
import { MkElnatVarderingsprotokollModule } from "./varderingsprotokoll-elnat.module";
import { HttpClientTestingModule } from '@angular/common/http/testing'
import { MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { action } from "@storybook/addon-actions";

const config: VarderingConfig = {
  skogsmark: {
    prisomrade: {
      norrlandsInland: 1,
      norrlandsKustland: 2,
      tillvaxtomrade3: 3,
      tillvaxtomrade4A: 4,
      tillvaxtomrade4B: 5
    },
  },

  punktersattning: {
    kabelSkap: {
      skog: 6,
      jordbruksimpediment: 7,
      ovrigMark: 8
    },
    natstation: {
      skog: {
        yta6x6m: 9,
        yta8x8m: 10,
        yta10x10m: 11
      },
      jordbruksimpediment: {
        yta6x6m: 12,
        yta8x8m: 13,
        yta10x10m: 14
      },
      ovrigMark: {
        yta6x6m: 15,
        yta8x8m: 16,
        yta10x10m: 17
      }
    },
    sjokabelskylt: {
      skog: {
        yta6x6m: 18,
        yta8x8m: 19,
        yta10x10m: 20
      },
      jordbruksimpediment: {
        yta6x6m: 21,
        yta8x8m: 22,
        yta10x10m: 23
      },
      ovrigMark: {
        yta6x6m: 24,
        yta8x8m: 25,
        yta10x10m: 26
      }
    }
  },

  markledning: {
    factor: 27
  },

  vaganlaggning: {
    zon1: 28,
    zon2: 29
  },

  prisbasbelopp: 30,
  minimumersattning: 31,
  forhojdMinimumersattning: 32,
  sarskildErsattningSkogsbruksavtalet: 33
};

export default {
  title: "Applikationer/Markkoll/Avtal/Värdering/Elnät värderingsprotokoll",
  component: MkElnatVarderingsprotokollComponent,
  parameters: {
    docs: {
      description: {
        component: "Komponent för att fylla på infomation inför skapandet av värderingsprotokoll, som är en bilaga till avtalet."
      }
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        MkElnatVarderingsprotokollModule,
        getTranslocoModule(),
        HttpClientTestingModule,
        MatDialogModule
      ],
      providers: [
        {
          provide: VARDERING_CONFIG,
          useValue: config,
        },
      ]
    }),
  ],
};

const Template: StoryFn<MkElnatVarderingsprotokollComponent> = (args: MkElnatVarderingsprotokollComponent) => ({
  props: {
    ...args,
    avtalMetadataChange: action("avtalMetadataChange"),
    vpChange: action("vpChange"),
    bilagorChange: action("bilagorChange"),
  }
});

export const emptyProtokoll = Template.bind({});
emptyProtokoll.storyName = "Tomt protokoll";
emptyProtokoll.args = {
  projektId: "projektId",
  vp: {
    id: "vpId",
    config: {

    },
    metadata: {

    },
    punktersattning: [],
    markledning: [],
    ssbSkogsmark: [],
    ssbVaganlaggning: [],
    hinderAkermark: [],
    ledningSkogsmark: [],
    ovrigtIntrang: [],
    rotnetto: null,
    prisomrade: {

    }
  },
  bilagor: null,
  avtalMetadata: null,
  uppdragsnummer: "",
  summering: {}
};
