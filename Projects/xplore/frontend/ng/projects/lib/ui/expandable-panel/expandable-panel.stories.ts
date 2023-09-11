import { MatTabsModule } from "@angular/material/tabs";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpVerticalTabsModule } from "../vertical-tabs/vertical-tabs.module";
import { XpExpandablePanelComponent } from "./expandable-panel.component";
import { XpExpandablePanelModule } from "./expandable-panel.module";

export default {
  title: "Komponenter/Navigation/Expandable Panel",
  component: XpExpandablePanelComponent,
  argTypes: {
    tabPosition: { 
      control: { type: "radio"},
      options: ["left", "right"],
      description: "Position på taben." 
    },
    tabTitle: {
      description: "Titel på taben."
    },
    tabIcon: {
      description: "Icon på taben."
    },
    initialHeight: {
      description: "Hur högt panel ska expandera vid klick på tab."
    },
    viewportTopMargin: {
      description: "Marginal för hur långt upp panelen kan dras."
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpExpandablePanelModule,
        XpVerticalTabsModule,
        MatTabsModule
      ]
    })
  ]
} as Meta

const Template: StoryFn<XpExpandablePanelComponent> = (args: XpExpandablePanelComponent) => ({
  template: `<div style="position:absolute; left:0; bottom: 0; height:100%; width:100%; overflow: hidden; background-color: gray;"><xp-expandable-panel
              [tabTitle]="tabTitle"
              [tabIcon]="tabIcon"
              [tabPosition]="tabPosition" style="position: absolute; bottom: 0px;">
              <p style="padding-bottom: 15px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
              incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
              exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute
              irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
              pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
              deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
              incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
              exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute
              irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
              pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
              deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
              incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
              exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute
              irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
              pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
              deserunt mollit anim id est laborum. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
              deserunt mollit anim id est laborum.</p>
            </xp-expandable-panel></div>`,
  props: args,
});

const TemplateWithTabs: StoryFn<XpExpandablePanelComponent> = (args: XpExpandablePanelComponent) => ({
  template: `<div style="position:absolute; left:0; bottom: 0; height:100%; width:100%; overflow: hidden; background-color: gray;">
            <xp-expandable-panel
              [tabTitle]="tabTitle"
              [tabIcon]="tabIcon"
              [tabPosition]="tabPosition" style="position: absolute; bottom: 0px; width: 50%">
                <xp-vertical-tabs activeTab="tab1">
                  <xp-vertical-tab tabId="tab1" tabTitle="Sök" tabIcon="search">
                      Tab 1 content. 
                      Lorem ipsum dolor sit amet consectetur, adipisicing elit. Necessitatibus dolore consequatur rem earum quos! Deleniti nemo debitis beatae, 
                      architecto placeat quis, adipisci cum veritatis at, consequuntur et! Quod, quae eaque!
                      Lorem ipsum dolor sit amet consectetur, adipisicing elit. Necessitatibus dolore consequatur rem earum quos! Deleniti nemo debitis beatae, 
                      architecto placeat quis, adipisci cum veritatis at, consequuntur et! Quod, quae eaque!
                      Lorem ipsum dolor sit amet consectetur, adipisicing elit. Necessitatibus dolore consequatur rem earum quos! Deleniti nemo debitis beatae, 
                      architecto placeat quis, adipisci cum veritatis at, consequuntur et! Quod, quae eaque!
                      Lorem ipsum dolor sit amet consectetur, adipisicing elit. Necessitatibus dolore consequatur rem earum quos! Deleniti nemo debitis beatae, 
                      architecto placeat quis, adipisci cum veritatis at, consequuntur et! Quod, quae eaque!
                  </xp-vertical-tab>
                  <xp-vertical-tab tabId="tab2" tabTitle="Kartlager" tabIcon="layers">
                    <mat-tab-group>
                      <mat-tab label="First">Content 1</mat-tab>
                      <mat-tab label="Second">Content 2</mat-tab>
                      <mat-tab label="Third">Content 3</mat-tab>
                    </mat-tab-group>
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
            </xp-expandable-panel></div>`,
  props: args,
});


export const expandablePanelLeft = Template.bind({});
expandablePanelLeft.storyName = "Utforskaren";
expandablePanelLeft.args = {
  tabPosition: "left",
  tabTitle: "Utforska kartan",
  tabIcon: "travel_explore",
  initialHeight: 100,
  viewportTopMargin: 0
};

export const expandablePanelRight = Template.bind({});
expandablePanelRight.storyName = "Lämna feedback";
expandablePanelRight.args = {
  tabPosition: "right",
  tabTitle: "Lämna feedback",
  tabIcon: "thumbs_up_down",
  initialHeight: 100,
  viewportTopMargin: 0
};

export const expandablePanelWithTabs = TemplateWithTabs.bind({});
expandablePanelWithTabs.storyName = "Med vertikala tabbar";
expandablePanelWithTabs.args = {
  tabPosition: "left",
  tabTitle: "Tabbar",
  tabIcon: "travel_explore",
  initialHeight: 100,
  viewportTopMargin: 0
};
