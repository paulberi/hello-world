import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { AvtalMetadata, FiberVarderingConfig, FiberVarderingsprotokoll } from "../../../../../../generated/markkoll-api";
import { FiberIntrangAkerOchSkogsmark } from "../../../../../../generated/markkoll-api/model/fiberIntrangAkerOchSkogsmark";
import { FiberMarkledning } from "../../../../../../generated/markkoll-api/model/fiberMarkledning";
import { FiberOvrigIntrangsersattning } from "../../../../../../generated/markkoll-api/model/fiberOvrigIntrangsersattning";
import { FiberPunktersattning } from "../../../../../../generated/markkoll-api/model/fiberPunktersattning";
import { FiberPunktersattningTyp } from "../../../../../../generated/markkoll-api/model/fiberPunktersattningTyp";
import { FiberVarderingsprotokollConfig } from "../../../../../../generated/markkoll-api/model/fiberVarderingsprotokollConfig";
import { FiberVarderingsprotokollMetadata } from "../../../../../../generated/markkoll-api/model/fiberVarderingsprotokollMetadata";
import { AvtalService } from "../../services/avtal.service";
import { FiberErsattning, MkFiberVarderingService } from "../../services/vardering-fiber.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";
import { MkFiberVarderingsprotokollContainerComponent } from "./varderingsprotokoll-fiber.container";

describe(MkFiberVarderingsprotokollContainerComponent.name, () => {
  let container: MkFiberVarderingsprotokollContainerComponent;
  let fiberVarderingsprotokollService: MkFiberVarderingsprotokollService;
  let avtalService: AvtalService;

  const vpMetadata: FiberVarderingsprotokollMetadata = {
    varderingstidpunkt: "varderingstidpunkt",
    varderingsmanOchForetag: "varderingsmanOchForetag"
  };

  const config: FiberVarderingsprotokollConfig = {
    sarskildErsattning: 25.0
  };

  const markledning: FiberMarkledning = {
    beskrivning: "beskrivning",
    langd: 1,
    bredd: 2,
  };

  const punktersattning: FiberPunktersattning = {
    antal: 1,
    beskrivning: "beskrivning",
    typ: FiberPunktersattningTyp.MARKSKAPOVRIGMARK,
  };

  const avtalMetadata: AvtalMetadata = {
    stationsnamn: "stationsnamn",
    matandeStation: "matandeStation",
    franStation: "franStation",
    tillStation: "tillStation",
    markslag: "markslag",
  };

  const intrangAkerOchSkogsmark: FiberIntrangAkerOchSkogsmark = {
    beskrivning: "beskrivning",
    ersattning: 0,
  };

  const ovrigIntrangsersattning: FiberOvrigIntrangsersattning = {
    beskrivning: "beskrivning",
    ersattning: 0,
  };

  const vp: FiberVarderingsprotokoll = {
    config: config,
    metadata: vpMetadata,
    markledning: [markledning],
    punktersattning: [punktersattning],
    intrangAkerOchSkogsmark: [intrangAkerOchSkogsmark],
    ovrigIntrangsersattning: [ovrigIntrangsersattning],
  };

  const fiberVarderingConfig: FiberVarderingConfig = {
    markskap: {
      skog: 750,
      jordbruksimpediment: 750,
      ovrigMark: 750,
    },

    optobrunn: {
      skog: 750,
      jordbruksimpediment: 750,
      ovrigMark: 750,
    },

    teknikbod: {
      skog6x6m: 3450,
      skog8x8m: 4200,
      skog10x10m: 5100,

      jordbruksimpediment6x6m: 4050,
      jordbruksimpediment8x8m: 4500,
      jordbruksimpediment10x10m: 5100,

      ovrigMark6x6m: 4050,
      ovrigMark8x8m: 4050,
      ovrigMark10x10m: 4050,
    },

    schablonersattning: {
      optoror1m: 6.29,
      optoror2m: 7.86,
    },

    minimiersattning: 2415,
    grundersattning: 0,
    tillaggExpropriationslagen: 0,
    sarskildErsattning: 25,
    sarskildErsattningMaxbelopp: 0,
    minimiersattningEnbartMarkledning: false
  };

  const ersattning: FiberErsattning = {
    punktersattning: [750],
    markledning: [8],
    intrangAkerOchSkogsmark: [0],
    ovrigIntrangsersattning: [0],
    summaIntrangsersattning: 758,
    grundersattning: 0,
    tillaggExpropriationslagen: 0,
    sarskildErsattning: 190,
    totalErsattning: 2415
  };

  beforeEach(() => {
    fiberVarderingsprotokollService = MockService(MkFiberVarderingsprotokollService, {
      updateVp: jest.fn().mockReturnValue(of(null)),
      getFiberVarderingConfig: jest.fn().mockReturnValue(fiberVarderingConfig)
    });

    avtalService = MockService(AvtalService, {
      setAvtalMetadata: jest.fn((_avtalId, _avtalMetadata) => of(_avtalMetadata))
    });

    container = new MkFiberVarderingsprotokollContainerComponent(fiberVarderingsprotokollService, avtalService);
    container.vp = {
      markledning: [markledning],
      punktersattning: [punktersattning],
    };
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

  it("ska uppdatera värderingsprotokoll", () => {
    // Given
    container.vp = null;
    container.ersattning = null;
    container.varderingService = new MkFiberVarderingService(fiberVarderingConfig);

    // When
    container.onVpChange(vp)

    // Then
    expect(fiberVarderingsprotokollService.updateVp).toHaveBeenCalledWith(container.projektId, vp);
    expect(container.vp).toEqual(vp);
    expect(container.ersattning).toEqual(ersattning);
  })
});
