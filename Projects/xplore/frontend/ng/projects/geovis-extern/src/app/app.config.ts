import {environment} from "../environments/environment";
import {AppConfig, UrlConfig} from "../../../lib/config/config.service";
import {AuthConfig} from "angular-oauth2-oidc";

export const appConfig: AppConfig = {
  appName: "geovis-publik",
  fastighetsSelection: false,
  exportCopyrightText: "© Lantmäteriet, Metria"  /* Copyright text specific for this application */
};

export const urlConfig: UrlConfig = {
  authUrl: environment.authUrl,
  defaultConfigurationUrl: environment.defaultConfigurationUrl,
  configurationUrl: environment.configurationUrl,
  wmsUrl: environment.wmsUrl,
  sokServiceUrl: environment.sokServiceUrl
};
