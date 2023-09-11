import { Markagare, Avtalsstatus, ProjektTyp, Agartyp, FastighetDelomrade,
  ProjektInfo,
  ElnatPunktersattningTyp,
  ElnatVarderingsprotokoll,
  ElnatZon,
  ElnatPrisomrade
} from "../../../../generated/markkoll-api/";
import { MkAgare } from "../app/model/agare";
import { MkIntrang } from "../app/model/intrang";
import { MkAvtalMap } from "../app/model/avtalskarta";

export function mockProjektInfo(): ProjektInfo {
  return {
    id: "b5a1770f-8a52-4881-bc2e-983691e24cc7",
    namn: "test",
    ort: "Öjebyn",
    projektTyp: ProjektTyp.FIBER,
    beskrivning: "beskrivning",
    startDatum: "2020-12-05",
  };
}

export function mockAgare(): Markagare {
  return {
    id: "123",
    namn: "Test",
    adress: "adress",
    postnummer: "12345",
    postort: "Öjebyn",
    andel: "1/2",
    kontaktperson: true,
    agartyp: Agartyp.LF,
    avtalsstatus: Avtalsstatus.EJBEHANDLAT,
  } as Markagare;
}

export function testIntrang(): MkIntrang {
  return {
    markstrak: 100,
    luftstrak: 50,
  };
}

export function testOmbud(): MkAgare {
  return {
    id: "id",
    namn: "Ett Ombud",
    adress: "Ombudsvägen 8",
    postnummer: "420 69",
    ort: "Luleå",
    telefon: "073 235813",
    ePost: "ombud@metria.se",
    status: Avtalsstatus.EJBEHANDLAT,
    kontaktperson: false,
    bankkonto: "1234-5678",
    andel: "1/1",
    fodelsedatumEllerOrgnummer: null,
    inkluderaIAvtal: true,
    labels: {},
    agartyp: Agartyp.OMBUD,
    utbetalningsdatum: null,
    land: "Swaziland"
  };
}

export const nyttOmbud: MkAgare = {
  id: "",
  namn: "",
  adress: "",
  postnummer: "",
  ort: "",
  telefon: "",
  ePost: "",
  status: Avtalsstatus.EJBEHANDLAT,
  kontaktperson: false,
  bankkonto: "",
  andel: "",
  fodelsedatumEllerOrgnummer: null,
  inkluderaIAvtal: true,
  labels: {},
  agartyp: Agartyp.OMBUD,
  utbetalningsdatum: null,
  land: "Swaziland"
};

export function testOmbud2(): MkAgare {
  return {
    id: "000-000-000",
    namn: "Ett till ombud",
    adress: null,
    postnummer: null,
    ort: null,
    telefon: null,
    ePost: null,
    status: Avtalsstatus.AVTALSIGNERAT,
    inkluderaIAvtal: true,
    andel: null,
    labels: {},
    kontaktperson: false,
    bankkonto: "1342-7586",
    fodelsedatumEllerOrgnummer: null,
    agartyp: Agartyp.OMBUD,
    utbetalningsdatum: null,
    land: "Swaziland"
  };
}
export function testMarkagare(): MkAgare {
  return {
    id: "123581321",
    adress: "Metriagatan 27",
    bankkonto: "123456",
    ePost: "jag@metria.se",
    kontaktperson: true,
    namn: "Metria Metriasson",
    ort: "Luleå",
    postnummer: "12345",
    telefon: "070-123 456",
    status: Avtalsstatus.AVTALSIGNERAT,
    andel: "1/2",
    fodelsedatumEllerOrgnummer: null,
    inkluderaIAvtal: true,
    labels: { avtalsstatusGammal: true },
    agartyp: Agartyp.LF,
    utbetalningsdatum: null,
    land: "Swaziland"
  };
}

export function testMarkagare2(): MkAgare {
  return {
    id: "1234567890",
    namn: "Christoffer Karlsson",
    kontaktperson: false,
    adress: "Jannegatan 6",
    postnummer: "94335",
    ort: "Öjebyn",
    telefon: "012-34567",
    bankkonto: "111-111-111",
    ePost: "chka@metria.se",
    status: Avtalsstatus.AVTALSKICKAT,
    agartyp: Agartyp.LF,
    andel: "1/2",
    fodelsedatumEllerOrgnummer: "860304-8979",
    inkluderaIAvtal: false,
    labels: {},
    utbetalningsdatum: null,
    land: "Swaziland"
  };
}

export const ettFastighetsomrade: FastighetDelomrade[] = [
  {
    "fastighetsId": "909a6a72-2df6-90ec-e040-ed8f66444c3f",
    "omradeNr": 1,
    "extent": [
      372627.88211673184,
      6751904.320876143,
      372656.72898555524,
      6751929.944452422
    ],
    "geometryType": null
  }
];

export const tvaFastighetsomraden: FastighetDelomrade[] = [
  {
    "fastighetsId": "909a6a72-3ce3-90ec-e040-ed8f66444c3f",
    "omradeNr": 2,
    "extent": [
      374111.3524036708,
      6745155.763890282,
      374147.8271985023,
      6745193.820940259
    ],
    "geometryType": null
  },
  {
    "fastighetsId": "909a6a72-3ce3-90ec-e040-ed8f66444c3f",
    "omradeNr": 4,
    "extent": [
      373792.5845554406,
      6745477.853805506,
      373964.2848201338,
      6745830.642580124
    ],
    "geometryType": null
  }
];

export const omradeLineString: FastighetDelomrade[] = [
  {
    "geometryType": "ST_LineString"
  }
];

export const omradeMultiLineString: FastighetDelomrade[] = [
  {
    "geometryType": "ST_MultiLineString"
  }
];

const OmradenInteMittlinjeredovisade: FastighetDelomrade[] = [
  {
    "geometryType": "ST_Polygon"
  },
  {
    "geometryType": "ST_MultiPolygon"
  },
  {
    "geometryType": "ST_Point"
  },
  {
    "geometryType": "ST_MultiPoint"
  },
  {
    "geometryType": "ST_GeometryCollection"
  },
  {
    "geometryType": null
  }
];

export const testAvtalskartaLineString: MkAvtalMap = {
  fastighetsId: "909a6a72-2e15-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "BÅTSTAD 1:141",
  extent: [ 373963.49399998, 6745009.10499952, 374240.92799998, 6745278.44399953 ],
  projektId: mockProjektInfo().id,
  omraden: omradeLineString,
};

export const testAvtalskartaMultiLineString: MkAvtalMap = {
  fastighetsId: "909a6a72-2e15-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "BÅTSTAD 1:141",
  extent: [ 373963.49399998, 6745009.10499952, 374240.92799998, 6745278.44399953 ],
  projektId: mockProjektInfo().id,
  omraden: omradeMultiLineString,
};

export const testAvtalskartaInteMittlinjeredovisad: MkAvtalMap = {
  fastighetsId: "909a6a72-2e15-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "BÅTSTAD 1:141",
  extent: [ 373963.49399998, 6745009.10499952, 374240.92799998, 6745278.44399953 ],
  projektId: mockProjektInfo().id,
  omraden: OmradenInteMittlinjeredovisade,
};

export const testAvtalskartaMedEttFastighetsomrade: MkAvtalMap = {
  fastighetsId: "909a6a72-2df6-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "BÅTSTAD 1:110",
  extent: [ 373963.49399998, 6745009.10499952, 374240.92799998, 6745278.44399953 ],
  projektId: mockProjektInfo().id,
  omraden: ettFastighetsomrade,
};

export const testAvtalskartaTvaFastighetsomraden: MkAvtalMap = {
  fastighetsId: "909a6a72-3ce3-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "KÄRRBACKSTRAND 1:32",
  extent: [ 373963.49399998, 6745009.10499952, 374240.92799998, 6745278.44399953 ],
  projektId: mockProjektInfo().id,
  omraden: tvaFastighetsomraden,
};

export const testVarderingsprotokoll: ElnatVarderingsprotokoll = {
  ledningSkogsmark: [
    { ersattning: 432 },
    { ersattning: 68 }
  ],
  rotnetto: 500,
  markledning: [
    { langd: 1, bredd: 2},
    { langd: 4, bredd: 1 }
  ],
  punktersattning: [
    {typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 1 }
  ],
  ssbSkogsmark: [
    { langd: 5, bredd: 7 },
    { langd: 6, bredd: 8 }
  ],
  ssbVaganlaggning: [
    { langd: 5, zon: ElnatZon.ZON_1 },
    { langd: 10, zon: ElnatZon.ZON_2 }
  ],
  config: {
    lagspanning: false,
    storskogsbruksavtalet: false,
    ingenGrundersattning: false,
    forhojdMinimumersattning: false
  },
  prisomrade: ElnatPrisomrade.NORRLANDSINLAND
};

export const testVarderingsprotokoll2: ElnatVarderingsprotokoll = {
  markledning: [
    { langd: 10, bredd: 2},
    { langd: 40, bredd: 1 }
  ],
  punktersattning: [
    { typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 2 },
    { typ: ElnatPunktersattningTyp.NATSTATIONSKOG10X10M, antal: 3 }
  ],
  ssbSkogsmark: [
    { langd: 2000, bredd: 2 },
    { langd: 1000, bredd: 3 }
  ],
  ssbVaganlaggning: [
    { langd: 500, zon: ElnatZon.ZON_1 },
    { langd: 1000, zon: ElnatZon.ZON_2 }
  ],
  config: {
    lagspanning: false,
    storskogsbruksavtalet: false,
    ingenGrundersattning: false,
    forhojdMinimumersattning: false
  },
  prisomrade: ElnatPrisomrade.TILLVAXTOMRADE4A
};

export const testVarderingsprotokoll3: ElnatVarderingsprotokoll = {
  ssbSkogsmark: [
    { langd: 50, bredd: 50 },
  ],
  config: {
    lagspanning: false,
    storskogsbruksavtalet: false,
    ingenGrundersattning: false,
    forhojdMinimumersattning: false
  },
  prisomrade: ElnatPrisomrade.TILLVAXTOMRADE4B
};

export const testVarderingsprotokollMedExtraAllt: ElnatVarderingsprotokoll = {
  markledning: [
    { langd: 1, bredd: 2 },
    { langd: 4, bredd: 1 },
    { langd: 8, bredd: 2 },
    { langd: 16, bredd: 1 }
  ],
  punktersattning: [
      { typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 1 },
      { typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 2 },
      { typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 3 },
      { typ: ElnatPunktersattningTyp.KABELSKAPOVRIGMARK, antal: 4 }
  ],
  hinderAkermark: [
      { ersattning: 100 },
      { ersattning: 200}
  ],
  ledningSkogsmark: [
      { ersattning: 432 },
      { ersattning: 68 }
  ],
  rotnetto: 500,
  ovrigtIntrang: [
      { ersattning: 300 },
      { ersattning: 400 }
  ],
  ssbSkogsmark: [
      { langd: 5, bredd: 7 },
      { langd: 6, bredd: 8 },
      { langd: 9, bredd: 10 },
      { langd: 11, bredd: 12 }
  ],
  ssbVaganlaggning: [
      { langd: 5, zon: ElnatZon.ZON_1 },
      { langd: 10, zon: ElnatZon.ZON_2 }
  ],
  config: {
      lagspanning: false,
      storskogsbruksavtalet: false,
      ingenGrundersattning: false,
      forhojdMinimumersattning: false
  },
  prisomrade: ElnatPrisomrade.NORRLANDSINLAND
}
