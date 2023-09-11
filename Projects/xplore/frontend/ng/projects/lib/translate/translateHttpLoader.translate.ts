import { HttpClient } from "@angular/common/http";
import { Observable, forkJoin, } from "rxjs";
import { mergeAll, reduce } from "rxjs/operators";
import { TranslocoLoader } from '@ngneat/transloco';

/**
 * Implementation av TranslateLoader som möjliggör att man kan ladda in ett godtyckligt antal
 * språkfiler med ngx-translate.
 *
 * Språkfilerna läses in i samma ordning som i parameterlistan i konstruktorn. Om två filer
 * innehåller samma key, så blir den key som man angav i första språkfilen överskriven av den key som
 * finns i andra språkfilen. Användbart om du vill overrida något meddelande.
 */
class XpTranslateHttpLoader implements TranslocoLoader {
  private prefixes: string[];
  private versioner: any;

  /**
   * @param http Http-klient
   * @param prefixes Sökvägar till foldrar med språkfiler
   */
  constructor(private http: HttpClient, versioner: any, ...prefixes: string[]) {
    this.prefixes = prefixes;
    this.versioner = versioner;
  }

  getTranslation(lang: string): Observable<Object> {

    const translations$ = this.prefixes.map(prefix =>
      this.http.get(this.languageFileUrl(prefix, lang))
    );

    return forkJoin(translations$)
      .pipe(
        mergeAll(),
        reduce((acc, translation) => MergeRecursive(acc, translation), {})
      );
  }

  private languageFileUrl(prefix: string, lang: string): string | null {
    if (this.versioner && this.versioner[lang]) {
      return `${prefix}${lang}.json?v=${this.versioner[lang]}`;
    } else {
      return `${prefix}${lang}.json`;
    }
  }
}

// https://stackoverflow.com/questions/171251/how-can-i-merge-properties-of-two-javascript-objects-dynamically
function MergeRecursive(obj1: object, obj2: object): object {

  for (var p in obj2) {
    try {
      // Property in destination object set; update its value.
      if (obj2[p].constructor == Object) {
        obj1[p] = MergeRecursive(obj1[p], obj2[p]);
      } else {
        obj1[p] = obj2[p];
      }

    } catch(e) {
      // Property in destination object not set; create it and set its value.
      obj1[p] = obj2[p];
    }
  }

  return obj1;
}

export { XpTranslateHttpLoader };
