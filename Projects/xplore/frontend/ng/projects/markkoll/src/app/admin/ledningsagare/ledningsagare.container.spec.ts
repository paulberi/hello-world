import { SimpleChange } from "@angular/core";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { Ledningsagare } from "../../../../../../generated/markkoll-api";
import { MkLedningsagareService } from "../../services/ledningsagare.service";
import { MkLedningsagareContainer } from "./ledningsagare.container";

describe(MkLedningsagareContainer.name, () => {
  let container: MkLedningsagareContainer;
  let ledningsagareService: MkLedningsagareService;

  const LEDNINGSAGARE: Ledningsagare = {
    id: "id",
    namn: "namn"
  }

  beforeEach(() => {
    ledningsagareService = MockService(MkLedningsagareService, {
      addLedningsagare: jest.fn().mockReturnValue(of(LEDNINGSAGARE)),
      getLedningsagare: jest.fn().mockReturnValue(of([LEDNINGSAGARE])),
      deleteLedningsagare: jest.fn().mockReturnValue(of(null))
    });

    container = new MkLedningsagareContainer(ledningsagareService);
  });

  it("Ska uppdatera ledningsägare när kundId ändras", () => {
    // Given
    var kundId = "kundId";

    // When
    container.ngOnChanges({kundId: new SimpleChange(null, kundId, false)});

    // Then
    expect(ledningsagareService.getLedningsagare).toHaveBeenCalledWith(kundId);
    expect(container.ledningsagare).toEqual([LEDNINGSAGARE]);
  });

  it("Ska gå att lägga till ledningsägare", () => {
    // Given
    var namn = "Ledningsägare";
    container.kundId = "kundId";
    expect(container.ledningsagare).toEqual([]);

    // When
    container.onLedningsagareAdd(namn);

    // Then
    expect(ledningsagareService.addLedningsagare).toHaveBeenCalledWith(namn, container.kundId);
    expect(container.ledningsagare).toEqual([LEDNINGSAGARE]);
  });

  it("Ska gå att ta bort en ledningsägare", () => {
    // Given
    const kundId = "kundId";
    const ledningsagareId = LEDNINGSAGARE.id;

    container.kundId = kundId;
    container.onLedningsagareAdd("foo");
    expect(container.ledningsagare).toEqual([LEDNINGSAGARE]);

    // When
    container.onLedningsagareDelete(ledningsagareId);

    // Then
    expect(ledningsagareService.deleteLedningsagare).toHaveBeenCalledWith(ledningsagareId, kundId);
    expect(container.ledningsagare).toEqual([]);
  })
});
