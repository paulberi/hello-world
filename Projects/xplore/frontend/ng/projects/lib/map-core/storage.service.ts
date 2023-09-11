import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class StorageService {

  constructor() {
  }

  setItem(item: StorageItem, value): void {
    sessionStorage.setItem(item, JSON.stringify(value));
  }

  getItem(item: StorageItem, fallback?): any {
    if (sessionStorage.getItem(item)) {
      return JSON.parse(sessionStorage.getItem(item));
    }

    return fallback ? fallback : undefined;
  }

  updateItem(item: StorageItem, updatedItem) {
    const existingItem = this.getItem(item, {});
    for (const property in updatedItem) {
      if (updatedItem.hasOwnProperty(property)) {
        existingItem[property] = updatedItem[property];
      }
    }
    this.setItem(item, existingItem);
  }

  removeItem(item: StorageItem) {
    sessionStorage.removeItem(item);
  }
}

export enum StorageItem {
  QUERY_PARAMETERS = "xp.query.parameters"
}

