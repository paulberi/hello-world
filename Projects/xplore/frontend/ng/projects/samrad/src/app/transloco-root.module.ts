import { HttpClient } from "@angular/common/http";
import { TRANSLOCO_LOADER, TRANSLOCO_CONFIG, translocoConfig, TranslocoModule } from "@ngneat/transloco";
import { Inject, ModuleWithProviders, NgModule } from "@angular/core";
import { XpTranslateHttpLoader } from "../../../lib/translate/translateHttpLoader.translate";
import { languageFilePaths } from "./app.config";
import translocoVersioner from "../../../../generated/samrad/langfileVersions.json";
import { XpTranslateConfig, XpTranslateConfigService } from "../../../lib/util/transloco.util";

function httpLoaderFactory(http: HttpClient) {
  return new XpTranslateHttpLoader(http, translocoVersioner, ...languageFilePaths);
}
@NgModule({
  imports: [ TranslocoModule ]
})

export class TranslocoRootModule {

  constructor(@Inject(XpTranslateConfigService) private config) {
  }

  static forRoot(config: XpTranslateConfig): ModuleWithProviders<TranslocoModule> {
    return {
      ngModule: TranslocoModule,
      providers: [
        {
          provide: TRANSLOCO_CONFIG,
          useValue: translocoConfig({
            availableLangs: config.availableLanguages,
            defaultLang: config.defaultLanguage,
          })
        },
        {
          provide: TRANSLOCO_LOADER,
          useFactory: httpLoaderFactory,
          deps: [HttpClient]
        }
      ]
    };
  }
}
