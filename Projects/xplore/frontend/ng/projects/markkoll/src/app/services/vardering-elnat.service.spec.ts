import {
  ElnatVarderingsprotokoll,
} from "../../../../../generated/markkoll-api";
import { ElnatVarderingService } from "./vardering-elnat.service";
import xlsx from "xlsx";
import * as vpConfig from "../vp_config.json";
import fs from "fs";
import path from "path";

describe(ElnatVarderingService.name, () => {
  let varderingService: ElnatVarderingService;
  const basePath = process.cwd() + "/projects/markkoll/src/test/vptestdata/";

  beforeEach(() => {
    varderingService = new ElnatVarderingService(vpConfig);
  });

  const rootFolderTestArgs = getTestArgs(basePath);
  const vpConfigPermutationsTestArgs = getTestArgs(basePath + "vp_config_permutations/");

  it.each(getTestArgs(basePath + "markledning/"))
  ("Värdering markledning: %s", (xlsxFile, vp) => {
    // Given
    const markledning = vp.markledning[0];

    // When
    const ersattning = varderingService.getErsattningMarkledning(markledning);

    // Then
    const expected = readVPCell(basePath + "markledning/" + xlsxFile, "J11");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(getTestArgs(basePath + "punktersättning/"))
  ("Värdering punktersättning: %s", (xlsxFile, vp) => {
    // Given
    const punktersattning = vp.punktersattning[0];

    // When
    const ersattning = varderingService.getErsattningPunktersattning(punktersattning);

    // Then
    const expected = readVPCell(basePath + "punktersättning/" + xlsxFile, "J18");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(getTestArgs(basePath + "skogsmark/"))
  ("Värdering skogsmark: %s", (xlsxFile, vp) => {
    // Given
    const skogsmark = vp.ssbSkogsmark[0];
    const prisomrade = vp.prisomrade;

    // When
    const ersattning = varderingService.getErsattningSsbSkogsmark(skogsmark, prisomrade);

    // Then
    const expected = readVPCell(basePath + "skogsmark/" + xlsxFile, "J43");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(getTestArgs(basePath + "väganläggning/"))
  ("Värdering väganläggning: %s", (xlsxFile, vp) => {
    // Given
    const vaganlaggning = vp.ssbVaganlaggning[0];

    // When
    const ersattning = varderingService.getErsattningSsbVaganlaggning(vaganlaggning);

    // Then
    const expected = readVPCell(basePath + "väganläggning/" + xlsxFile, "J50");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Summa markledning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getErsattningMarkledningSumma(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J15");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Summa punktersättning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getErsattningPunktersattningSumma(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J22");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Summa skogsmark: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getErsattningSsbSkogsmarkSumma(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J47");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Summa väganläggning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getErsattningSsbVaganlaggningSumma(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J52");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Summa intrångsersättning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getSummaIntrangsersattning(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J54");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(rootFolderTestArgs)
  ("Tillägg enligt expropriationslagen: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getTillaggExpropriationslagen(vp);

    // Then
    const expected = readVPCell(basePath + xlsxFile, "J55");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(getTestArgs(basePath + "särskild_ersättning/"))
  ("Särskild ersättning vid överrenskommelse: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getSarskildErsattning(vp);

    // Then
    const expected = readVPCell(basePath + "särskild_ersättning/" + xlsxFile, "J56");
    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(vpConfigPermutationsTestArgs)
  ("Grundersättning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getGrundersattning(vp);

    // Then
    let expected = readVPCell(basePath + "vp_config_permutations/" + xlsxFile, "J57");

    // Det avrundade värdet är det som visas i formuläret (men inte i cellen), och är det värde
    // vi är intresserade av.
    expected = Math.round(expected);

    expect(ersattning).toBeCloseTo(expected);
  });

  it.each(vpConfigPermutationsTestArgs)
  ("Total ersättning: %s", (xlsxFile, vp) => {
    // When
    const ersattning = varderingService.getTotalErsattning(vp);

    // Then
    const expected = readVPCell(basePath + "vp_config_permutations/" + xlsxFile, "J58");
    expect(ersattning).toBeCloseTo(expected);
    });
});

function getTestArgs(basePath): [string, ElnatVarderingsprotokoll][] {
  return fs.readdirSync(basePath)
    .filter(file => path.parse(file).ext === ".xlsx")
    .map(file => [file, jsonFromXlsxPath(basePath + file)]);
}

function jsonFromXlsxPath(xlsxPath): ElnatVarderingsprotokoll {
  const dir = path.parse(xlsxPath).dir;
  const baseName = path.parse(xlsxPath).name;
  const jsonPath = dir + "/" + baseName + ".json";

  return parseVPJson(jsonPath);
}

function readVPCell(xlsxPath: string, cell: string): number {
  const workbook = xlsx.readFile(xlsxPath);
  const sheet = workbook.Sheets["Värderingsprotokoll"];
  return sheet[cell].v;
}

function parseVPJson(jsonPath): ElnatVarderingsprotokoll {
  return JSON.parse(fs.readFileSync(jsonPath).toLocaleString()).varderingsprotokoll;
}
