import {AuthConfig} from 'angular-oauth2-oidc';
import {environment} from "../environments/environment";
import {AppConfig, UrlConfig} from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "skogsanalys",
  configuration: "skogsanalys",
  fastighetsSelection: true,
  multiSelect: true
};

export const urlConfig: UrlConfig = {
  configurationUrl: environment.configurationUrl,
  wmsUrl: environment.wmsUrl,
  sokServiceUrl: environment.sokServiceUrl
};

export const languageFilePaths: string[] = [
  "/assets/locales/xp/",
  "/assets/locales/",
];
