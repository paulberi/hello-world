/**
 * Script som genererar versionsnumrering (MD5 hash) av översättningsfiler, och skriver ut till
 * json-fil.
 *
 * Versionsnummret kan t.ex. användas som queryparameter vid hämtning av översättningsfiler, för att
 * invalidera cachen om filerna ändras.
 *
 * Frikostigt modifierat efter exemplet på
 * https://netbasal.com/strategies-for-cache-busting-translation-files-in-angular-86143ee14c3c
 */
const crypto = require('crypto');
const fs = require('fs');
const glob = require('glob');

/**
 * langfile_paths: Sökvägar till foldrar med språkfiler. Den genererade versionen fås av att man slår
 *                 ihop språkfilerna i vardera folder, och genererar en hash av det.
 * version_path:   Var den resulterande json-filerna ska sparas.
 */
const config = [
  // Markkoll
  {
    langfile_paths: [
      "projects/markkoll/src/assets/locales/",
      "projects/lib/translate/locales/"
    ],
    versions_path: "generated/markkoll"
  },
  //Admin
  {
    langfile_paths: [
      "projects/admin/src/assets/locales/",
      "projects/lib/translate/locales/"
    ],
    versions_path: "generated/admin"
  },
  //Skogsanalys
  {
    langfile_paths: [
      "projects/skogsanalys/src/assets/locales/",
      "projects/lib/translate/locales/"
    ],
    versions_path: "generated/skogsanalys"
  },
  //Mitt Metria
  {
    langfile_paths: [
      "projects/mitt-metria/src/assets/locales/",
      "projects/lib/translate/locales/"
    ],
    versions_path: "generated/mitt-metria"
  },
  //Samråd
  {
    langfile_paths: [
      "projects/samrad/src/assets/locales/",
      "projects/lib/translate/locales/"
    ],
    versions_path: "generated/samrad"
  }
];

function generateChecksum(str, algorithm, encoding) {
  return crypto
    .createHash(algorithm || 'md5')
    .update(str, 'utf8')
    .digest(encoding || 'hex');
}

config.forEach(cfg => {
  const result = {};
  cfg.langfile_paths.forEach(langfile_path => {
    glob.sync(langfile_path + "*.json").forEach(path => {
      const [_, lang] = path.split(langfile_path);
      const content = fs.readFileSync(path, { encoding: 'utf-8' });

      const locale = lang.replace('.json', '');
      if (!result[locale]) {
        result[locale] = content;
      } else {
        result[locale] += content;
      }
    });
  });

  for (const [key, value] of Object.entries(result)) {
    result[key] = generateChecksum(value);
  }

  fs.mkdirSync(cfg.versions_path, { recursive: true });
  fs.writeFileSync(cfg.versions_path+"/langfileVersions.json", JSON.stringify(result));
});
