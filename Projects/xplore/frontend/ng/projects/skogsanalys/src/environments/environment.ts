// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  configurationUrl: "/api/config/app-config",
  metriaMapsPrefix: "/api/metria-maps",
  wmsUrl: "/api/metria-maps/geoserver/wms",
  sokServiceUrl: "/api/sok",
  skogligaGrunddataUrl: "/api/skog/skogliga-grunddata",
  nmdUrl: "/api/skog/nmd",
  avverkningsstatistikUrl: "/api/skog/avverkningsstatistik",
  huggningsklasserUrl: "/api/skog/huggningsklasser",
  skyddadeOmradenUrl: "/api/skog/skyddade-omraden",
  availableLanguages: ["sv"],
  defaultLanguage: "sv"
};
