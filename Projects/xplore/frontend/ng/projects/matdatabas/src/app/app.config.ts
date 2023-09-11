import {AppConfig, MapConfig, UrlConfig} from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "Mätdatabas",
  configuration: "mätdatabas",
  fastighetsSelection: false,
};

export const urlConfig: UrlConfig = {
  defaultConfigurationUrl: "/config",
  configurationUrl: "/config/app-config"
};
