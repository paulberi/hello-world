import XpThemeV2 from "!css-loader!sass-loader!../projects/lib/ui/theme/themes/xp-theme-v2.scss";
import XpTheme from "!css-loader!sass-loader!../projects/lib/ui/theme/themes/xp-theme.scss";
import { setCompodocJson } from "@storybook/addon-docs/angular";
import docJson from "../documentation.json";
import { componentWrapperDecorator } from '@storybook/angular';
//Blir "argType.defaultValue` is deprecated and will be removed in Storybook 7.0" när docjson används för att generera inputs, properties, outputs
//setCompodocJson(docJson);

//Ett verktyg för att switcha mellan nuvarande Xp-Layout-temat och det nya (V2)
export const globalTypes = {
  theme: {
    title: "Theme",
    description: "Choose theme",
    defaultValue: "Xp-Theme",
    toolbar: {
      icon: "edit",
      items: ["Xp-Theme", "Metria Theme"],
      dynamicTitle: true,
    },
  },
};

const storybookStyles = document.createElement("style");

const withThemeProvider=(storyFn,context) => {
  const theme = context.globals.theme;
  if (theme === "Xp-Theme") {
    storybookStyles.innerHTML = XpTheme;
    document.body.appendChild(storybookStyles);
  } else if (theme === "Metria Theme") {
    storybookStyles.innerHTML = XpThemeV2;
    document.body.appendChild(storybookStyles);
  }
  return storyFn(context);
}

export const decorators = [withThemeProvider];

/**
 * Fånga upp output-events från komponenter som slutar på "Create", "Change", "Delete" eller "Click".
 * Vi använder inte prefixet "on" pga att det bryter mot Angulars kodstil, https://angular.io/guide/styleguide#style-05-16
 */
export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*|.*Create|.*Change|.*Delete|.*Click" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
  options: {
    storySort: {
      order: ["Xplore Designsystem", "Riktlinjer", "Komponenter", "Applikationer"],
    },
  },
  docs: { 
    inlineStories: true 
  },
}

//En bugg uppstår hos storybook vid importering av componentWrapperDecorator https://github.com/storybookjs/storybook/issues/17330
//Det finns en buggfix, men den har inte releasats till 7.0.0-alpha.33, kanske kommer till 7.0.0-alpha.34
// Vi lägger på mat-typography i index.html för att använda typsnitt från Material Design
/* export const decorators = [
  componentWrapperDecorator((story) => `<div class="mat-typography">${story}</div>`),
]; */
