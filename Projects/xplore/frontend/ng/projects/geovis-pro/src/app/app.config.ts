import {environment} from "../environments/environment";
import {AppConfig, UrlConfig} from "../../../lib/config/config.service";
import {AuthConfig} from "angular-oauth2-oidc";

export const appConfig: AppConfig = {
  appName: "geovis-pro",
  fastighetsSelection: true,
  selectFastighetOnlyIfLayerIsActive: true,
  layerEditing: true,
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
