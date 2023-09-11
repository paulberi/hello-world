import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { BreadcrumbsComponent } from "./breadcrumbs.component";
import { getTranslocoModule } from "../../../../../../lib/translate/transloco-testing.module";
import { RouterTestingModule } from "@angular/router/testing";
import { MMBreadcrumbsModule } from "./breadcrumbs.module";

export default {
    title: "Applikationer/Mitt Metria/Breadcrumbs",
    component: BreadcrumbsComponent,
    parameters: {
        docs: {
            description: {
                component: "Brödsmulor som kan användas för navigering och att synliggöra vilken sida/sidor användaren står på och kommer ifrån. Den aktiva path:en har fetstil (testa genom att klicka på 'Breadcrumbs')."
            }
        }
    },
    argTypes: {
        items: {
            description: "Brödsmulor. Innehåller name och path."
        }
    },
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                RouterTestingModule,
                MMBreadcrumbsModule
            ]
            ,
        }),
    ],

} as Meta;

const Template: StoryFn<BreadcrumbsComponent> = (args: BreadcrumbsComponent) => ({
    props: {
        ...args,
    }
});

export const breadcrumbs = Template.bind({});

breadcrumbs.args = {
    items: [
        {
            name: "Xplore Designsystem",
            path: "/lank-som-ej-fungerar",
        },
        {
            name: "Applikationer",
            path: "/lank-som-ej-fungerar",
        },
        {
            name: "Mitt Metria",
            path: "/lank-som-ej-fungerar",
        },
        {
            name: "Breadcrumbs",
            path: "/",
        },
    ],
};
