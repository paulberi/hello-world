module.exports = {
  features: {
    previewMdx2: false
  },
  core: {},
  "stories": [
    "../projects/**/*.stories.mdx",
    "../projects/**/*.stories.@(js|jsx|ts|tsx)"
  ],
  "addons": [
    "@storybook/addon-links",
    "@storybook/addon-essentials",
    "@storybook/addon-a11y"
  ],
  framework: {
    name: "@storybook/angular",
    options: {}
  }
};