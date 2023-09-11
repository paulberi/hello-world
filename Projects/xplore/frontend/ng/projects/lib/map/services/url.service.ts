import {Injectable} from "@angular/core";
import {Location} from "@angular/common";

@Injectable({
  providedIn: "root"
})
export class UrlService {

  constructor(private location: Location) {
  }

  getPath() {
    return this.location.path();
  }

  getFullPath() {
    return this.location.prepareExternalUrl(this.location.path());
  }

  getParams() {
    const path = this.getPath();
    return this.parseQuery(path.substring(path.indexOf("?")));
  }

  updateParams(params) {
    const path = this.getPath();
    const updatedQuery = `x=${params.x}&y=${params.y}&zoom=${params.zoom}`;
    this.location.replaceState(path.substring(0, path.indexOf("?")), updatedQuery);
  }

  private parseQuery(queryString) {
    const query = {};
    const pairs = (queryString[0] === "?" ? queryString.substr(1) : queryString).split("&");
    for (let i = 0; i < pairs.length; i++) {
      const pair = pairs[i].split("=");
      query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || "");
    }
    return query;
  }

  clearQuery() {
    const path = this.getPath();
    if (path) {
      this.location.replaceState(path.substring(0, path.indexOf("?")), "");
    }
  }
}
