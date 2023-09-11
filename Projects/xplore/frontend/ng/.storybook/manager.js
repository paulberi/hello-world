import { addons } from '@storybook/addons';
import xploreTheme from './xplore-theme.js';

//run --no-manager-cache för att se ändringar i temat (man behöver också uppdatera sidan)
addons.setConfig({
  theme: xploreTheme,
});
