import { create } from '@storybook/theming';

export default create({
  base: 'light',

  colorPrimary: '#333333',
  colorSecondary: '#007565',

  // UI
  appBg: '#C7DEDB',
  appContentBg: 'white',
  appBorderColor: 'lightgrey',
  appBorderRadius: 4,

  // Typography
  /* fontBase: '"Open Sans", sans-serif',
  fontCode: 'monospace', */

  // Text colors
  textColor: '#333333',
  textInverseColor: '#707070',

  // Toolbar default and active colors
  barTextColor: 'rgb(153, 153, 153)',
  barSelectedColor: '#007565',
  barBg: 'white',

  // Form colors
  inputBg: 'white',
  inputBorder: 'lightgrey',
  inputTextColor: 'black',
  inputBorderRadius: 4,

  brandTitle: 'Xplore Designsystem',
  /* brandUrl: 'https://example.com',
  brandImage: 'https://place-hold.it/350x150', */
});
