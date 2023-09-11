import { action } from "@storybook/addon-actions";
import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { getTranslocoModule } from "../../../translate/transloco-testing.module";
import { XpCartComponent, Cart } from "./cart.component";
import { cartItems } from "./cart.mock";
import { XpCartModule } from "./cart.module";

export default {
    title: "Komponenter/E-handel/Varukorg",
    component: XpCartComponent,
    decorators: [
        moduleMetadata({
            imports: [
                getTranslocoModule(),
                XpCartModule
            ],
        }),
    ],

} as Meta;

const Template: StoryFn<XpCartComponent> = (args: XpCartComponent) => ({
    props: {
        ...args,
        closeCart: action("closeCart"),
        deleteCartItem: action("deleteItem"),
        checkout: action("checkout")
    }
});

export const withCartItems = Template.bind({});
withCartItems.args = {
    cart: {
        cartItems,
        totalPrice: 200,
        vat: 50,
        currency: "kr"
    } as Cart
};

export const emptyCart = Template.bind({});
emptyCart.args = {
    cart: {
        cartItems: [],
        totalPrice: null,
        vat: null,
        currency: null
    } as Cart
};