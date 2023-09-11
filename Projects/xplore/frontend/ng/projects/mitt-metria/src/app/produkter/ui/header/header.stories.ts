import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { HeaderComponent } from "./header.component";
import { action } from "@storybook/addon-actions";
import { MMHeaderModule } from "./header.module";

export default {
    title: "Applikationer/Mitt Metria/Header med knapp",
    component: HeaderComponent,
    parameters: {
        docs: {
            description: {
                component: "En komponent med en titel och en knapp. Knappen är valfri."
            }
        }
    },
    argTypes: {
        header: {
            description: "Titel i headern."
        },
        buttonText: {
            description: "Text på knappen."
        },
        buttonColor: {
            control: { type: "radio", },
            options: [
                "primary",
                "accent",
                "warn"
            ],
            description: "Färg på knappen."
        },
        isButtonVisible: {
            description: "Om knappen ska synas."
        }
    },
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                MMHeaderModule
            ],
        }),
    ],

} as Meta;

const Template: StoryFn<HeaderComponent> = (args: HeaderComponent) => ({
    props: {
        buttonClick: action("buttonClick"),
        ...args,
    }
});

export const titleWithButton = Template.bind({});
titleWithButton.args = {
    header: "Produkt 1",
    buttonText: "Påbörja beställning",
    buttonColor: "accent",
    isButtonVisible: true
};

export const titleWithoutButton = Template.bind({});
titleWithoutButton.args = {
    header: "Välkommen",
    buttonText: "",
    buttonColor: "primary",
    isButtonVisible: false
};