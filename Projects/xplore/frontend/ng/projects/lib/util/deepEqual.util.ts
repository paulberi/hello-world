import {Moment} from "moment";

/* Functions for testing for deep equality in commonly used packages do not account for instances of moments. Just like
* with Date, we can have moment objects with the same 'external' value, but with different internal representations. */

export function deepEqual(x: any, y: any): boolean {
  if (x === y) {
    return true;
  } else if ((typeof x === "object" && (x !== null || x !== "undefined")) &&
    (typeof y === "object" && (y !== null || y !== "undefined"))) {
    if (Object.keys(x).length !== Object.keys(y).length) {
      return false;
    }
    for (const prop in x) {
      if (prop in y) {
        if (compareProperty(x[prop], y[prop]) === false) {
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  } else if (x !== y) {
    return false;
  } else {
    return true;
  }
}

function compareProperty(xProperty: any, yProperty: any): boolean {
  if (!xProperty && !yProperty) {
    return true;
  } else if (xProperty === null || yProperty === null) {
    return false;
  } else if (xProperty instanceof Date && yProperty instanceof Date) {
    return xProperty.getTime() === yProperty.getTime();
  } else if (xProperty._isAMomentObject && yProperty._isAMomentObject) {
    const xMoment = xProperty as Moment;
    const yMoment = yProperty as Moment;
    return xMoment.isSame(yMoment);
  } else {
    return deepEqual(xProperty, yProperty);
  }
}
