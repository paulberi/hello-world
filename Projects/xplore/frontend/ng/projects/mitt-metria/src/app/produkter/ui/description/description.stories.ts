import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { DescriptionComponent } from "./description.component";
import { MMDescriptionModule } from "./description.module";

export default {
    title: "Applikationer/Mitt Metria/Textbeskrivning",
    component: DescriptionComponent,
    parameters: {
        docs: {
            description: {
                component: "En textbeskrivning som har en begränsning av tecken. Eventuella knappar för att läsa mer och mindre av texten."
            }
        }
    },
    argTypes: {
        content: {
            description: "Text som ska synas."
        },
        limit: {
            description: "Antal tecken som ska synas."
        },
        innerHtml: {
            description: "Om innerHtml ska användas för content."
        },
        showReadMoreAndLessButton: {
            description: "Om knapp ska visas om limiten överskridits."
        },
        readMoreText: {
            description: "Eventuell text på läs-mer-knappen."
        },
        readLessText: {
            description: "Eventuell text på läs-mindre-knappen."
        }
    },
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                MMDescriptionModule
            ],
        }),
    ],

} as Meta;

const Template: StoryFn<DescriptionComponent> = (args: DescriptionComponent) => ({
    props: {
        ...args,
    }
});

export const description = Template.bind({});
description.args = {
    content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ultrices semper aliquam. Donec non purus mattis, ornare nisi eu, vehicula lorem. Suspendisse tellus arcu, rutrum ac mattis sed, varius quis libero. Donec sodales, magna eu varius efficitur, nunc est facilisis velit, id feugiat nisl enim eget lorem. Donec porttitor lobortis eleifend. Sed vulputate massa nec diam facilisis, at pretium mi porttitor. Aliquam vehicula viverra eros vel finibus. Suspendisse felis nulla, malesuada sit amet semper sed, sagittis quis urna.",
    limit: 100,
    innerHtml: false,
    showReadMoreAndLessButton: true,
    readMoreText: "Läs mer",
    readLessText: "Läs mindre"
};