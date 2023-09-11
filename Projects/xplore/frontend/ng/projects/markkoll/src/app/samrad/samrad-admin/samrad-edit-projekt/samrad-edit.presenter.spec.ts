import { SamradProjektinformationPresenter } from "./samrad-edit.presenter";

describe("Slug validate pattern", () => {
  let fixture: SamradProjektinformationPresenter;
  beforeEach(async () => {
    fixture = new SamradProjektinformationPresenter();
    fixture.initializeForm({ rubrik: "Samrad" });
  });

  it("Ä,Å,Ö, should return invalid", () => {
    fixture.form.patchValue({
      slug: "äåö",
    });
    expect(fixture.form.get("slug").invalid).toEqual(true);
  });

  it("Special characters, should return invalid", () => {
    fixture.form.patchValue({
      slug: "aaa?",
    });
    expect(fixture.form.get("slug").invalid).toEqual(true);
  });

  it("Spaces, should return invalid", () => {
    fixture.form.patchValue({
      slug: "aa a",
    });
    expect(fixture.form.get("slug").invalid).toEqual(true);
  });

  it("LowerCase letters and no special characters or spaces, should retun valid", () => {
    fixture.form.patchValue({
      slug: "aaa",
    });
    expect(fixture.form.get("slug").valid).toEqual(true);
  });

  it("Hyphen between letters, should retun valid", () => {
    fixture.form.patchValue({
      slug: "a-aa",
    });
    expect(fixture.form.get("slug").valid).toEqual(true);
  });

});