import {environment} from "../environments/environment";
import {AppConfig, UrlConfig} from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "brandbränslekartan",
  configuration: "brandbränslekartan",
  fastighetsSelection: false
};

export const urlConfig: UrlConfig = {
  configurationUrl: environment.configurationUrl,
  wmsUrl: environment.wmsUrl,
  sokServiceUrl: environment.sokServiceUrl
};
