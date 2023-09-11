import { MatTabsModule } from "@angular/material/tabs";
import { Meta, moduleMetadata, StoryFn } from "@storybook/angular";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpVerticalTabsModule } from "../vertical-tabs/vertical-tabs.module";
import { XpDraggablePanelComponent } from "./draggable-panel.component";
import { XpDraggablePanelModule } from "./draggable-panel.module";


export default {
  title: "Komponenter/Navigation/Draggable Panel",
  component: XpDraggablePanelComponent,
  decorators: [
    moduleMetadata({
      imports: [
        getTranslocoModule(),
        XpDraggablePanelModule,
        XpVerticalTabsModule, 
        MatTabsModule
      ]
    })
  ]
} as Meta

const Template: StoryFn<XpDraggablePanelComponent> = (args: XpDraggablePanelComponent) => ({
  template: `
  <xp-draggable-panel [title]="title" [boundary]="boundary" >
    <span>Content</span>
  </xp-draggable-panel>
  `,
  props: args,
});

export const Default = Template.bind({});
Default.storyName = "Default";
Default.args = {
  title: "Verktygslåda",
  boundary: "body",
};