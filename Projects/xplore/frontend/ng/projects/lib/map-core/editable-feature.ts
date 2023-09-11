import Feature from "ol/Feature";

(<any> Feature.prototype).startEdit = function () {
  this.initialState = this.clone();
  this.initialState.setId(this.getId());
};

(<any> Feature.prototype).finishEdit = function () {
  this.initialState = null;
};

(<any> Feature.prototype).revert = function () {
  if (this.initialState != null) {
    this.setProperties(this.initialState.getProperties());
    this.setGeometry(this.initialState.getGeometry());
    this.initialState = null;
  }
};

(<any> Feature.prototype).isEditing = function () {
  return this.initialState != null;
};

(<any> Feature.prototype).hasChanged = function () {
  if (!this.initialState) {
    return false;
  }

  for (const prop in this.getProperties()) {
    if (prop === this.getGeometryName()) {
      if (this.getGeometry().getCoordinates().toString() !== this.initialState.getGeometry().getCoordinates().toString()) {
        return true;
      }
    } else {
      const old = this.initialState.get(prop);
      const now = this.get(prop);
      if (typeof now === "string") {
        if ((now || old) && (!now || !old || now.trim() !== old.trim())) {
          return true;
        }
      } else if (now !== old) {
        return true;
      }
    }
  }

  return false;
};

(<any> Feature.prototype).isPersisted = function () {
  return this.getId().match(/^[^\.]+\.\d+$/) != null;
};

