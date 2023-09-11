// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  host: "http://localhost:9025",
  availableLanguages: ["sv"],
  defaultLanguage: "sv",
  authIssuer: "/configAuthIssuer",
  authIssuerBankId: "/authIssuerBankId",
  keyCloakClientId: "samrad-frontend",
  keyCloakScope: "openid profile email offline_access",
  backendUrl: "/api",
  geoserverUrl: "/geoserver/mapCMS/wms",
  geoserverwfsUrl: "/geoserver/mapCMS/wfs",
  backendUrlAdmin: "/api/admin",
  metriaMapsUrl: "/geoserver/metria/wms",
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
