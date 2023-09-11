import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import "./editable-feature";

describe("Feature", () => {
  let feature;
  beforeEach(() => { feature = new Feature(); });

  describe("isEditing", () => {
    it("should return false on a fresh object", () => {
      expect(feature.isEditing()).toBe(false);
    });
    it("should return true on an object under edit", () => {
      feature.startEdit();
      expect(feature.isEditing()).toBe(true);
    });
    it("should return false after an edit is finished", () => {
      feature.startEdit();
      expect(feature.isEditing()).toBe(true);
      feature.finishEdit();
      expect(feature.isEditing()).toBe(false);
    });
  });

  describe("revert", () => {
    it("should revert to the state when startEdit was called", () => {
      feature.set("testProp", "abc");
      feature.startEdit();
      feature.set("testProp", "123");
      feature.revert();
      expect(feature.get("testProp")).toBe("abc");
    });
    it("should revert geometries", () => {
      feature.setGeometry(new Point([1, 2]));
      feature.startEdit();
      const initialCoordinates = feature.getGeometry().getCoordinates();
      feature.setGeometry(new Point([3, 4]));
      feature.revert();
      expect(feature.getGeometry().getCoordinates()).toEqual(initialCoordinates);
    });
  });
});
