import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { CommonModule } from "@angular/common";
import { XpLayoutComponent, XpLayoutUserInfo } from "./layout.component";
import { XpLayoutModule } from "./layout.module";
import { mockMenuItems } from "./layout.mock";
import { RouterTestingModule } from "@angular/router/testing";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatDialogModule } from "@angular/material/dialog";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { action } from "@storybook/addon-actions";

export default {
  title: "Komponenter/Page/Layout",
  component: XpLayoutComponent,
  parameters: {
    docs: {
      description: {
        component: "Bygger upp Xplores gemensamma layout med sidhuvud, sidfot och plats för innehåll."
      }
    }
  },
  argTypes: {
    loggedInUserInfo: {
      description: "Inloggad användare i applikationen."
    },
    appName: {
      description: "Applikationens namn."
    },
    isFooterVisible: {
      description: "Ska fothuvud visas eller inte."
    },
    isHelpVisible: {
      description: "Ska hjälp visas i menyn."
    },
    menuItems: {
      description: "Lista med alternativ för navigationsmenyn."
    },
    logoutClick: {
      description: "Event när användaren klickar på logga ut."
    },
    helpClick: {
      description: "Event när användaren klickar på hjälp."
    },
    pageChange: {
      description: "Event när användaren byter sida i menyn."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        MatDialogModule,
        getTranslocoModule(),
        XpLayoutModule,
        RouterTestingModule,
      ],
    }),
  ]
} as Meta;

const mockUser: XpLayoutUserInfo = {
  id: "test@test",
  fornamn: "Test",
  efternamn: "Testsson",
  email: "test@test",
  kund: "Kund"
};

const Template: StoryFn<XpLayoutComponent> = (args: XpLayoutComponent) => ({
  props: {
    ...args,
    logoutClick: action("logoutClick"),
    helpClick: action("helpClick"),
    pageChange: action("pageChange") 
  }
});

export const headerAndFooter = Template.bind({});
headerAndFooter.storyName = "Sidhuvud och sidfot";
headerAndFooter.args = {
  loggedInUserInfo: mockUser,
  appName: "Markkoll",
  isFooterVisible: true,
  isHelpVisible: true,
  menuItems: mockMenuItems
};

export const header = Template.bind({});
header.storyName = "Sidhuvud";
header.args = {
  loggedInUserInfo: null,
  appName: "GeoVis",
  isFooterVisible: false,
  isHelpVisible: true,
  menuItems: []
};
