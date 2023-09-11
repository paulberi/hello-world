import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpVerticalTabsComponent } from "./vertical-tabs.component";
import { XpVerticalTabsModule } from "./vertical-tabs.module";


export default {
  title: "Komponenter/Navigation/Vertical Tabs",
  component: XpVerticalTabsComponent,
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpVerticalTabsModule
      ]
    })
  ]
} as Meta

const Template: StoryFn<XpVerticalTabsComponent> = (args: XpVerticalTabsComponent) => ({
  template: `
            <xp-vertical-tabs activeTab="tab2">
              <xp-vertical-tab tabId="tab1" tabTitle="Sök" tabIcon="search">
                Tab 1 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab2" tabTitle="Kartlager" tabIcon="layers">
                Tab 2 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab3" tabTitle="Skapa" tabIcon="edit">
                Tab 3 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab4" tabTitle="Redigera" tabIcon="crop">
                Tab 4 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab5" tabTitle="Markera" tabIcon="straighten">
                Tab 5 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab6" tabTitle="Mät" tabIcon="straighten">
                Tab 6 content.
              </xp-vertical-tab>
              <xp-vertical-tab tabId="tab7" tabTitle="Dela" tabIcon="share">
                Tab 7 content.
              </xp-vertical-tab>
            </xp-vertical-tabs>
            `,
  props: args,
});

export const verticalTabs = Template.bind({});
verticalTabs.storyName = "Tabbar";
verticalTabs.args = {};
