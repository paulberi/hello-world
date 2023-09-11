import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { TillvaratagandeTyp } from "../../../../../../generated/markkoll-api";
import { AvtalService } from "../../services/avtal.service";
import { DokumentService } from "../../services/dokument.service";
import { MkVirkesinlosenContainer } from "./virkesinlosen.container";

describe(MkVirkesinlosenContainer.name, () => {
  let container: MkVirkesinlosenContainer;
  let avtalService: AvtalService;
  let dokumentService: DokumentService;

  beforeEach(() => {
    avtalService = MockService(AvtalService, {
      setSkogsfastighet: jest.fn().mockReturnValue(of(null)),
      setTillvaratagandeTyp: jest.fn().mockReturnValue(of(null))
    });

    dokumentService = MockService(DokumentService, {
      getVarderingSkogsmarkXlsx: jest.fn(),
    });

    container = new MkVirkesinlosenContainer(avtalService, dokumentService);
  });

  it.each([true, false])(`Ska anropa ${AvtalService.name} när man togglar skoglig värdering`, (skogligVardering: boolean) => {
    // When
    container.onSkogligVarderingChange(skogligVardering);

    // Then
    expect(avtalService.setSkogsfastighet)
      .toHaveBeenCalledWith(container.projektId, container.fastighetId, skogligVardering);
  });

  it.each([true, false])("Ska uppdatera vid toggling av skoglig värdering", (skogligVardering: boolean) => {
    // Given
    container.skogligVardering = null;

    // When
    container.onSkogligVarderingChange(skogligVardering);

    // Then
    expect(container.skogligVardering).toEqual(skogligVardering);
  });

  it(`Ska anropa ${AvtalService.name} när man sätter tillvaratagandetyp`, () => {
    // Given
    const tillvaratagandeTyp = TillvaratagandeTyp.EGETTILLVARATAGANDE;

    // When
    container.onTillvaratagandeTypChange(tillvaratagandeTyp);

    // Then
    expect(avtalService.setTillvaratagandeTyp)
      .toHaveBeenCalledWith(container.projektId, container.fastighetId, tillvaratagandeTyp);
  });

  it(`Ska uppdatera när man sätter tillvaratagandetyp`, () => {
    // Given
    container.tillvaratagandeTyp = null;
    const tillvaratagandeTyp = TillvaratagandeTyp.EGETTILLVARATAGANDE;

    // When
    container.onTillvaratagandeTypChange(tillvaratagandeTyp);

    // Then
    expect(container.tillvaratagandeTyp).toEqual(tillvaratagandeTyp);
  });
});
