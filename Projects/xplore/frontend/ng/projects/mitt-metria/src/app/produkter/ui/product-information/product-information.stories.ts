import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { ProductInformationComponent } from "./product-information.component";
import { MMProductInformationModule } from "./product-information.module";

export default {
    title: "Applikationer/Mitt Metria/Produktinformation",
    component: ProductInformationComponent,
    parameters: {
        docs: {
            description: {
                component: "Information om en produkt."
            }
        }
    },
    argTypes: {
        productName: {
            description: "Produktnamn."
        },
        productDescription: {
            description: "Produktbeskrivning."
        },
        productImages: {
            description: "Produktbilder."
        },
        productDocuments: {
            description: "Dokument."
        }
    },
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                MMProductInformationModule
            ],
        }),
    ],

} as Meta;

const Template: StoryFn<ProductInformationComponent> = (args: ProductInformationComponent) => ({
    props: {
        startOrder: action("startOrder"),
        downloadDocument: action("downloadDocument"),
        ...args,
    }
});

export const productPage = Template.bind({});
productPage.storyName = "Produktsida",
    productPage.args = {
        productName: "Fastighetskartan",
        productDescription: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ultrices semper aliquam. Donec non purus mattis, ornare nisi eu, vehicula lorem. Suspendisse tellus arcu, rutrum ac mattis sed, varius quis libero. Donec sodales, magna eu varius efficitur, nunc est facilisis velit, id feugiat nisl enim eget lorem. Donec porttitor lobortis eleifend. Sed vulputate massa nec diam facilisis, at pretium mi porttitor. Aliquam vehicula viverra eros vel finibus. Suspendisse felis nulla, malesuada sit amet semper sed, sagittis quis urna.",
        productImages: [
            {
                name: "Vindkraftverk",
                source: "assets/mockBannerImage1.jpg"
            }
        ],
        productDocuments: [],
    };