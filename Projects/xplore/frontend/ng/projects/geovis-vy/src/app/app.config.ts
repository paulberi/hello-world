import {environment} from "../environments/environment";
import {AppConfig, UrlConfig} from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "geovis-vy",
  fastighetsSelection: true,
  selectFastighetOnlyIfLayerIsActive: true,
  exportCopyrightText: "© Lantmäteriet, Metria",   /* Copyright text specific for this application */
  multiSelect: true
};

export const urlConfig: UrlConfig = {
  authUrl: environment.authUrl,
  defaultConfigurationUrl: environment.defaultConfigurationUrl,
  configurationUrl: environment.configurationUrl,
  wmsUrl: environment.wmsUrl,
  sokServiceUrl: environment.sokServiceUrl,
  ownerListUrl: environment.ownerListUrl
};
