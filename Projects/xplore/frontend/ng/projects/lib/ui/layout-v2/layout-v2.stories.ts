import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { XpLayoutV2Component } from "./layout-v2.component";
import { XpLayoutV2Module } from "./layout-v2.module";
import { RouterTestingModule } from "@angular/router/testing";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { action } from "@storybook/addon-actions";
import { mockMenuItemsV2 } from "./layout-v2.mock";
import { cartItems } from "../../vendure/cart/cart/cart.mock";
import { Cart } from "../../vendure/cart/cart/cart.component";

export default {
  title: "Komponenter/Page/Layout-V2",
  component: XpLayoutV2Component,
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpLayoutV2Module,
        RouterTestingModule,
      ],
    }),
  ]
} as Meta;

const Template: StoryFn<XpLayoutV2Component> = (args: XpLayoutV2Component) => ({
  props: {
    ...args,
    menuIconClick: action("menuIconClick"),
    profileClick: action("profileClick"),
    logoutClick: action("logoutClick"),
    deleteCartItem: action("deleteCartItem"),
    toCheckout: action("toCheckout")
  }
});

export const headerAndFooter = Template.bind({});
headerAndFooter.storyName = "Sidhuvud och sidfot";
headerAndFooter.args = {
  appName: "Markkoll",
  isFooterVisible: true,
  isHeaderSticky: false,
  menuItems: mockMenuItemsV2,
  menuIcons: [],
  isCartIconVisible: false,
  cartBadgeNumber: null,
  cart: null,
};

export const header = Template.bind({});
header.storyName = "Sidhuvud";
header.args = {
  appName: "GeoVis",
  isFooterVisible: false,
  isHeaderSticky: false,
  menuItems: mockMenuItemsV2,
  menuIcons: [],
  isCartIconVisible: false,
  cartBadgeNumber: null,
  cart: null,
};

export const layoutWithCart = Template.bind({});
layoutWithCart.storyName = "Med varukorg";
layoutWithCart.args = {
  appName: "GeoVis",
  isFooterVisible: false,
  isHeaderSticky: false,
  menuItems: mockMenuItemsV2,
  menuIcons: [],
  isCartIconVisible: true,
  cartBadgeNumber: 2,
  cart: {
    cartItems,
    totalPrice: 200,
    vat: 50,
    currency: "kr"
  } as Cart,
};


