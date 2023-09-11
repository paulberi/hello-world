import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { mockAttributes } from "../../vendure/cart/cart/cart.mock";
import { OrderLine, XpOrderLineComponent } from "./order-line.component";
import { XpOrderLineModule } from "./order-line.module";

export default {
    title: "Komponenter/E-handel/Orderrad",
    component: XpOrderLineComponent,
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                XpOrderLineModule
            ],
        }),
    ],

} as Meta;

const Template: StoryFn<XpOrderLineComponent> = (args: XpOrderLineComponent) => ({
    props: {
        ...args,
        deleteOrderLine: action("deleteOrderLine")
    }
});

export const orderLine = Template.bind({});
orderLine.args = {
    orderLine: {
        orderLineId: "1",
        attributes: mockAttributes,
        currency: "kr",
        linePrice: 100,
        vat: 25
    } as OrderLine,
    isDeleteButtonVisible: true
};

export const withoutDelete = Template.bind({});
withoutDelete.args = {
    orderLine: {
        orderLineId: "2",
        attributes: mockAttributes,
        currency: "kr",
        linePrice: 100,
        vat: 25
    } as OrderLine,
    isDeleteButtonVisible: false
};