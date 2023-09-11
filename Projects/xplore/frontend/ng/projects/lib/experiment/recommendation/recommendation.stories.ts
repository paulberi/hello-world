import { moduleMetadata, StoryFn, Meta } from "@storybook/angular";
import { XpRecommendationComponent } from "./recommendation.component";
import { getTranslocoModule } from "../../translate/transloco-testing.module";
import { XpRecommendationModule } from "./recommendation.module";
import { RecommendationItems } from "./data";
import { action } from "@storybook/addon-actions";

export default {
  title: "Komponenter/Experiment/Recommendation",
  component: XpRecommendationComponent,
  parameters: {
    docs: {
      description: {
        component: "Experimentiell komponent för att visa olika typer av rekommendationer, ex. ur Metrias produktkatalog. Innehållet för rekommendationen är mockad i den här komponenten, men ska framöver hämtas från en backend-tjänst. Förbättringsförlag: Hantering av bilder ska inte vara hårdkodad med css."
      }
    }
  },
  decorators: [
    moduleMetadata({
      imports: [
        XpRecommendationModule,
        getTranslocoModule()],
    }),
  ],

} as Meta;

const Template: StoryFn<XpRecommendationComponent> = (args: XpRecommendationComponent) => ({
  props: {
    ...args,
    showMoreClick: action("showMoreClick"),
    showCatalogueClick: action("showCatalogueClick")
  }
});

export const kartskikt = Template.bind({});
kartskikt.storyName = "Förslag på kartskikt"
kartskikt.args = {
  items: RecommendationItems.splice(0, 3)
};

export const flerkartskikt = Template.bind({});
flerkartskikt.storyName = "Fler förslag"
flerkartskikt.args = {
  items: RecommendationItems.splice(0, 3)
};