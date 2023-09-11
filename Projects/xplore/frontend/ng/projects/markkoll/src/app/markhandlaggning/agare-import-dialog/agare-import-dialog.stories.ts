import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { of, timer } from "rxjs";
import { map } from "rxjs/operators";
import { getTranslocoModule } from "../../../../../lib/translate/transloco-testing.module";
import {
  MkAgareImportDialogComponent,
  MkAgareImportDialogData,
} from "./agare-import-dialog.component";
import { MkAgareImportDialogModule } from "./agare-import-dialog.module";

const Template: StoryFn<MkAgareImportDialogComponent> = (args: MkAgareImportDialogComponent) => ({
  props: args,
});

const data: MkAgareImportDialogData = {
  numOfFastigheter: 24,
  hamtaMarkagare$: timer(3000).pipe(map((_) => 1)),
};
export default {
  title: "Applikationer/Markkoll/Avtal/Ägare/Import Dialog",
  component: MkAgareImportDialogComponent,
  decorators: [
    moduleMetadata({
      imports: [MkAgareImportDialogModule, getTranslocoModule()],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {
            close: (_) => {},
          },
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: data,
        },
      ],
    }),
  ],
} as Meta;

export const Dialog = Template.bind({});
Dialog.storyName = "Hämta markägare-dialog";
Dialog.args = {};
