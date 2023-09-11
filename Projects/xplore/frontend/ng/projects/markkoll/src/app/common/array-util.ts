import { array } from "@amcharts/amcharts4/core";

export interface Identifiable {
  id?: any;
}

export function replace<T>(array: T[], shouldReplaceFn: (a: T) => boolean, elem: T): T[] {
  return array.map(e => shouldReplaceFn(e) ? elem : e);
}

export function replaceInPlace<T>(array: T[], shouldReplaceFn: (a: T) => boolean, elem: T) {
  array.forEach((item, index) => {
    if (shouldReplaceFn(item)) {
      Object.assign(array[index], elem);
    }
  });
}

export function replaceForId(array: Identifiable[], element: Identifiable) {
  const index = array.findIndex(obj => obj.id === element.id);
  array[index] = element;
}

export function removeForId(array: Identifiable[], id: any): Identifiable[] {
  const newArray = [...array];
  const index = newArray.findIndex(obj => obj.id === id);
  if (index === -1) {
    throw Error("Hittar inget objekt med id " + id + " i arrayen " + array);
  }
  newArray.splice(index, 1);
  return newArray;
}
