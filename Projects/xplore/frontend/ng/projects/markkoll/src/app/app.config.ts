import { AppConfig, UrlConfig } from "../../../lib/config/config.service";

export const appConfig: AppConfig = {
  appName: "Markkoll",
  configuration: "markkoll",
};

export const urlConfig: UrlConfig = {
  configurationUrl: null
};

export const languageFilePaths: string[] = [
  "/assets/locales/xp/",
  "/assets/locales/",
];

/**
 * Lista med felmeddelanden från backend. Motsvarar enums i backend med samma namn
 * och finns översatta i "mk.errors".
 */
export enum MarkkollError {
  PROJEKT_ERROR,
  FASTIGHET_ERROR,
  DOKUMENT_ERROR,
  SAMFALLIGHET_ERROR,
  MARKAGARE_ERROR,
  AVTAL_ERROR,
  IMPORT_ERROR,
  IMPORT_ERROR_SHAPE_FILE_MISSING,
  IMPORT_ERROR_NO_OBJECT,
  IMPORT_ERROR_NEW_VERSION,
  IMPORT_INVALID_CRS,
  UPLOAD_ERROR_MAX_FILE_SIZE,
  FORM_ERROR,
  FORM_ERROR_INVALID_PERSONUMMER
}
