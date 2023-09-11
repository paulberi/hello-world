import { moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../../lib/translate/transloco-testing.module";
import { XpDropzoneComponent } from "./dropzone.component";
import { XpDropzoneModule } from "./dropzone.module";

export default {
  title: "Komponenter/Form/Dropzone",
  component: XpDropzoneComponent,
  parameters: {
    docs: {
      description: {
        component: "Komponent som tillåter uppladdning av filer, antingen genom att klicka på den, eller med ett drag-and-drop-interface."
      }
    }
  },
  argTypes: {
    acceptedFileEndings: {
      description: "De filändelser som komponenten ska acceptera. Ex. \".zip\" eller \".docx\". Som standard tillåts alla filändelser."
    },
    isMultpleFilesAllowed: {
      description: "Om det går att ladda upp flera filer eller bara en åt gången."
    },
    label: {
      description: "Etikett som ska synas om inga filer är valda."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        XpDropzoneModule,
        getTranslocoModule(),
      ],
    }),
  ],
};

const Template: StoryFn<XpDropzoneComponent> = (args: XpDropzoneComponent) => ({
  props: args,
});

export const emptyDropzone = Template.bind({});
emptyDropzone.storyName = "Standard";
emptyDropzone.args = {
  acceptedFileEndings: [],
  isMultpleFilesAllowed: false,
  label: "Ladda upp en valfri fil"
};

export const onlyZipFiles = Template.bind({});
onlyZipFiles.storyName = "En eller flera zip-filer";
onlyZipFiles.args = {
  acceptedFileEndings: [".zip"],
  isMultipleFilesAllowed: true,
  label: "Ladda upp en eller flera zip-filer"
};

export const onlyWordExcel = Template.bind({});
onlyWordExcel.storyName = "Endast en Word eller Excel";
onlyWordExcel.args = {  
  acceptedFileEndings: [".docx", ".xlsx"],
  isMultpleFilesAllowed: false,
  label: "Ladda upp en Word (.docx) eller Excel (.xlsx)"
};
