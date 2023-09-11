import {AppConfig, UrlConfig} from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "admin",
};

export const urlConfig: UrlConfig = {
  defaultConfigurationUrl: "/config",
  configurationUrl: "/config/app-config"
};

export const languageFilePaths: string[] = [
  "/assets/locales/xp/",
  "/assets/locales/"
];
