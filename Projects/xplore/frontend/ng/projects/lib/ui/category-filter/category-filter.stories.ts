import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { action } from "@storybook/addon-actions";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpCategoryFilterComponent } from "./category-filter.component";
import { XpCategoryFilterModule } from "./category-filter.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

export default {
    title: "Komponenter/Navigation/Category filter",
    component: XpCategoryFilterComponent,
    parameters: {
        docs: {
            description: {
                component: "Filtreringskomponent som visar en kategori innehållande en lista med filter. Komponenten använder materials mat-expansion-panel för dropdowns."
            }
        }
    },
    argTypes: {
        items: {
            description: "Items med kategorier och tillhörande filter."
        },
        activeItemIds: {
            description: "Lista med aktiva filter."
        },
    },
    decorators: [
        moduleMetadata({
            imports: [
                XpCategoryFilterModule,
                BrowserAnimationsModule,
                getTranslocoModule()],
        }),
    ],

} as Meta;

const Template: StoryFn<XpCategoryFilterComponent> = (args: XpCategoryFilterComponent) => ({
    props: {
        ...args,
        filterUpdated: action("filterUpdated")
    }
});

export const color = Template.bind({});
color.args = {
    items: [{

        id: "0",
        name: "Färg",
        values: [
            {
                id: "0",
                name: "Blå"
            },
            {
                id: "1",
                name: "Röd"
            },
            {
                id: "2",
                name: "Gul"
            }
        ]
    }],
    activeItemIds: [0, 1],
};

