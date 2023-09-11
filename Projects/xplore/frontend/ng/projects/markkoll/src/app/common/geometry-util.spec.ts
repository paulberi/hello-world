import { Coordinate } from "ol/coordinate";
import { mergeLines } from "./geometry-util";

interface MergeLinesCase {
  lines: Coordinate[][];
  expectedLine: Coordinate[];
}

describe("GeometryUtil", () => {
  const cases: MergeLinesCase[] = [
    {
      lines: [
        [[0, 0], [1, 1]],
        [[2, 2], [3, 3]],
        [[-2, -2], [-1, -1]],
        [[5, 5], [4, 4]],
        [[-3, -3], [-4, -4]]
      ],
      expectedLine: [[-4, -4], [-3, -3], [-2, -2], [-1, -1], [0, 0], [1, 1], [2, 2], [3, 3], [4, 4], [5, 5]]
    }
  ];

  it.each(cases)
  ("ska slå ihop linjer korrekt", (testCase: MergeLinesCase) => {
    // When
    const actualLine = mergeLines(testCase.lines);

    // Then
    expect(actualLine).toEqual(testCase.expectedLine);
  });
});
