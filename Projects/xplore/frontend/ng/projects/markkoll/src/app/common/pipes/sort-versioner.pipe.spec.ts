import { Version } from "../../../../../../generated/markkoll-api";
import { MkSortVersionerPipe } from "./sort-versioner.pipe";

describe(MkSortVersionerPipe.name, () => {
  let pipe = new MkSortVersionerPipe();

  it("Ska sortera versioner efter datum", () => {
    // Given
    let versioner: Version[] = [
      {
        id: "id",
        filnamn: "filnamn",
        skapadDatum: "2014-11-12",
        buffert: 0
      },
      {
        id: "id",
        filnamn: "filnamn",
        skapadDatum: "2003-01-02",
        buffert: 0
      },
      {
        id: "id",
        filnamn: "filnamn",
        skapadDatum: "2008-06-07",
        buffert: 0
      },
    ];

    // When
    const sorted = pipe.transform(versioner);

    // Then
    expect(sorted.map(v => v.skapadDatum)).toEqual(["2014-11-12", "2008-06-07", "2003-01-02"]);
  })
});
