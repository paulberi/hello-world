const { pathsToModuleNameMapper } = require('ts-jest');
const { paths } = require('./tsconfig.json').compilerOptions;

var pathsFixed = paths == null ? [] : paths;

// eslint-disable-next-line no-undef
globalThis.ngJest = {
  skipNgcc: false,
  tsconfig: 'tsconfig.spec.json',
};

/** @type {import('ts-jest/dist/types').InitialOptionsTsJest} */
module.exports = {
  preset: 'jest-preset-angular',
  globalSetup: 'jest-preset-angular/global-setup',
  moduleNameMapper: pathsToModuleNameMapper(pathsFixed, { prefix: '<rootDir>' }),
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  transformIgnorePatterns: [
    // Ta med Open Layers och JSTS i transformen samt alla .mjs filer (det senare är default).
    'node_modules/(?!(.*\.mjs$)|(ol/)|(jsts/))'
  ],
  setupFiles: [
    "jest-canvas-mock"
  ]
};
