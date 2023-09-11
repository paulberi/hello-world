import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { MMKonfigurationstypAUiModule } from "./konfigurationstyp-a-ui.module";
import { KonfigurationstypAFormControl, KonfigurationstypAUiComponent } from "./konfigurationstyp-a-ui.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { action } from "@storybook/addon-actions";
import { KonfigurationsTypAProductVariant } from "../../feature/konfigurationstyp-a/konfigurationstyp-a.component";

export default {
    title: "Applikationer/Mitt Metria/Konfigurationstyp A",
    component: KonfigurationstypAUiComponent,
    decorators: [
        moduleMetadata({
            imports: [
                BrowserAnimationsModule,
                MMKonfigurationstypAUiModule,
                getTranslocoModule()],
        }),
    ],

} as Meta;

const Template: StoryFn<KonfigurationstypAUiComponent> = (args: KonfigurationstypAUiComponent) => ({
    props: {
        ...args,
        formChanges: action("formChanges"),
        statusChanges: action("statusChanges"),
    }
});

const parentFormControlOptions: KonfigurationsTypAProductVariant[] = [
    {
        id: "1",
        key: "kommun",
        name: "Kommun",
        schema: null,
        currency: "SEK"
    }
];

const childFormControl: KonfigurationstypAFormControl = {
    label: "Välj kommun",
    multiple: true,
    options: ["Umeå", "Luleå"]
}


const mockReferenssystem = ["SWEREF 99 TM", "SWEREF 00 12 00"];
const mockLeveransformat = ["Shape", "JPEG"];

export const withDropzoneAndSelect = Template.bind({});
withDropzoneAndSelect.args = {
    showDropzone: true,
    parentFormControlOptions: parentFormControlOptions,
    childFormControl: childFormControl,
    referenssystem: mockReferenssystem,
    leveransformat: mockLeveransformat
};

export const withDropzone = Template.bind({});
withDropzone.args = {
    showDropzone: true,
    parentFormControlOptions: [],
    childFormControl: null,
    referenssystem: mockReferenssystem,
    leveransformat: mockLeveransformat
};

export const withSelect = Template.bind({});
withSelect.args = {
    showDropzone: false,
    parentFormControlOptions: parentFormControlOptions,
    childFormControl: childFormControl,
    referenssystem: mockReferenssystem,
    leveransformat: mockLeveransformat
};