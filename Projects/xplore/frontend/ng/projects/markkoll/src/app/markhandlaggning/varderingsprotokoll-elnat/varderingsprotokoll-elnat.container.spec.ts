import { MockService } from "ng-mocks";
import { of } from "rxjs";
import {
  AvtalMetadata,
  ElnatLedningSkogsmark,
  ElnatMarkledning,
  ElnatPrisomrade,
  ElnatPunktersattning,
  ElnatPunktersattningTyp,
  ElnatSsbSkogsmark,
  ElnatSsbVaganlaggning,
  ElnatVarderingsprotokoll,
  ElnatVarderingsprotokollConfig,
  ElnatVarderingsprotokollMetadata,
  ElnatZon,
} from "../../../../../../generated/markkoll-api";
import { AvtalService } from "../../services/avtal.service";
import { ElnatVarderingService } from "../../services/vardering-elnat.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { MkElnatVarderingsprotokollContainer } from "./varderingsprotokoll-elnat.container";

describe(MkElnatVarderingsprotokollContainer.name, () => {
  let container: MkElnatVarderingsprotokollContainer;
  let varderingsprotokollService: ElnatVarderingsprotokollService;
  let elnatVarderingService: ElnatVarderingService;
  let avtalService: AvtalService;

  const vpMetadata: ElnatVarderingsprotokollMetadata = {
    ledning: "ledning",
    koncessionslopnr: "koncessionslopnr",
    varderingstidpunkt: "varderingstidpunkt",
    varderingsmanOchForetag: "varderingsmanOchForetag"
  };

  const config: ElnatVarderingsprotokollConfig = {
    lagspanning: true,
    storskogsbruksavtalet: true,
    ingenGrundersattning: false,
    forhojdMinimumersattning: false
  };

  const ledningSkogsmark: ElnatLedningSkogsmark = {
    beskrivning: "beskrivning",
    ersattning: 1234
  };

  const markledning: ElnatMarkledning = {
    beskrivning: "beskrivning",
    langd: 1,
    bredd: 2,
  };

  const rotnetto = 500;

  const punktersattning: ElnatPunktersattning = {
    antal: 1,
    beskrivning: "beskrivning",
    typ: ElnatPunktersattningTyp.KABELSKAPSKOG,
  };

  const skogsmark: ElnatSsbSkogsmark = {
    beskrivning: "beskrivning",
    langd: 1,
    bredd: 2,
  };

  const vaganlaggning: ElnatSsbVaganlaggning = {
    beskrivning: "beskrivning",
    langd: 1,
    zon: ElnatZon.ZON_1,
  };

  const avtalMetadata: AvtalMetadata = {
    stationsnamn: "stationsnamn",
    matandeStation: "matandeStation",
    franStation: "franStation",
    tillStation: "tillStation",
    markslag: "markslag",
  };

  const vp: ElnatVarderingsprotokoll = {
    markledning: [markledning],
    punktersattning: [punktersattning],
    rotnetto: rotnetto,
    ssbSkogsmark: [skogsmark],
    ssbVaganlaggning: [vaganlaggning],
    prisomrade: ElnatPrisomrade.NORRLANDSINLAND,
    ledningSkogsmark: [ledningSkogsmark],
  };

  beforeEach(() => {
    varderingsprotokollService = MockService(ElnatVarderingsprotokollService, {
      updateElnatVP: jest.fn(() => of(null))
    });

    elnatVarderingService = MockService(ElnatVarderingService, {
      getSummering: jest.fn()
    });

    avtalService = MockService(AvtalService, {
      setAvtalMetadata: jest.fn((_avtalId, _avtalMetadata) => of(_avtalMetadata))
    });

    container = new MkElnatVarderingsprotokollContainer(varderingsprotokollService,
      elnatVarderingService, avtalService);
  });

  it(`Ska anropa ${ElnatVarderingsprotokollService.name} när man uppdaterar VPt`, () => {
    // When
    container.onVpChange(vp);

    // Then
    expect(varderingsprotokollService.updateElnatVP).toHaveBeenCalledWith(container.projektId,
      vp.id, vp);
  });

  it(`Ska uppdatera ersättningen när man uppdaterar VPt`, () => {
    // When
    container.onVpChange(vp);

    // Then
    expect(elnatVarderingService.getSummering).toHaveBeenCalledWith(vp);
  });

  it(`Ska anropa ${AvtalService.name} när man uppdaterar avtalets metadata`, () => {
    // When
    container.onAvtalMetadataChange(avtalMetadata);

    // Then
    expect(avtalService.setAvtalMetadata).toHaveBeenCalledWith(container.avtalId, avtalMetadata);
  });

  it(`Ska uppdatera avtalets metadata`, () => {
    // When
    container.onAvtalMetadataChange(avtalMetadata);

    // Then
    expect(container.avtalMetadata).toEqual(avtalMetadata);
  });
});
