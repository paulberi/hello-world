// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  authIssuer: "/configAuthIssuer",
  keyCloakClientId: "markkoll",
  keyCloakScope: "openid profile email offline_access",
  allowedKeyCloakRole: [] as string[],
  availableLanguages: ["sv"],
  defaultLanguage: "sv",
  sokServiceUrl: "/api/sok",
  wmsUrl: "/metria-maps/geoserver/wms",
  configurationUrl: "/api/config/app-config",
  backendUrl: "/api",
  metriaMapsUrl: "/metria-maps/geoserver/wms",
  geoserverUrl: "/geoserver/markkoll/wms",
  markkollWfsUrl: "/geoserver/markkoll/wfs",
  samradBackendUrl: "/samrad/api/admin",
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import "zone.js/plugins/zone-error";  // Included with Angular CLI.
