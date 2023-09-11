import { XpUiSearchFieldPresenter } from "./search-field.presenter";

describe("SearchFieldPresenter", () => {
  const debounceTime = 50;

  it("Så ska värdet i emittas efter en viss tid", (done) => {    
    const value = "söksök";

    const presenter = new XpUiSearchFieldPresenter();
    presenter.debounceTime = debounceTime;
    const start = Date.now();

    presenter.searchQuery$.subscribe(query => {
      const end = Date.now();
      expect(end - start).toBeGreaterThanOrEqual(debounceTime);
      expect(query).toEqual(value);
      done();
    });

    presenter.search(value);
  });
});
