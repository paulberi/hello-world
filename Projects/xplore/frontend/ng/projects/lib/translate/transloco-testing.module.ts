import { TranslocoTestingModule, TranslocoTestingOptions } from '@ngneat/transloco';
import xp from "./locales/sv.json";
import mk from "../../markkoll/src/assets/locales/sv.json";
import adm from "../../admin/src/assets/locales/sv.json";
import mm from "../../mitt-metria/src/assets/locales/sv.json";

/**
 * Läs in Xplores översättningsfil för testning. Används för närvarande i Storybook.
 * Bör senare slås ihop med XpTranslocoTestModule.
 */

export function getTranslocoModule(options: TranslocoTestingOptions = {}) {
  return TranslocoTestingModule.forRoot({
    langs: { sv: {...xp, ...mk, ...adm, ...mm} },
    translocoConfig: {
      availableLangs: ['sv'],
      defaultLang: 'sv',
    },
    preloadLangs: true,
    ...options
  });
}