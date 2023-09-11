import { NgModule } from "@angular/core";
import { TranslocoLoader, TranslocoTranspiler, HashMap, TranslocoModule, TRANSLOCO_CONFIG, translocoConfig, TRANSLOCO_LOADER, TRANSLOCO_TRANSPILER } from "@ngneat/transloco";
import { Observable, of } from "rxjs";

class XpMockHttpLoader implements TranslocoLoader {
  constructor() {}

  getTranslation(lang: string): Observable<Object> {
    return of({});
  }
}

class XpMockTranspiler implements TranslocoTranspiler {
  transpile(value: any, params: HashMap<any>, translation: HashMap<any>) {
    return value;
  }

  onLangChanged?(lang: string): void {}
}

@NgModule({
  exports: [ TranslocoModule ],
  providers: [
    {
      provide: TRANSLOCO_CONFIG,
      useValue: translocoConfig({
        availableLangs: ["sv"],
        defaultLang: "sv",
        missingHandler: {
          logMissingKey: false
        }
      })
    },
    {
      provide: TRANSLOCO_LOADER,
      useClass: XpMockHttpLoader
    },
    {
      provide: TRANSLOCO_TRANSPILER,
      useClass: XpMockTranspiler
    }
  ]
})
export class XpTranslocoTestModule {}
