import { InjectionToken } from "@angular/core";

export interface XpTranslateConfig {
  availableLanguages: string[];
  defaultLanguage: string;
}

export const XpTranslateConfigService = new InjectionToken<XpTranslateConfig>("TranslateConfig");