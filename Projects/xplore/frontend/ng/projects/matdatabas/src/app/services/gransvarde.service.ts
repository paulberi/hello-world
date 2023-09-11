import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { map } from "rxjs/operators";

export const MAX = 0;
export const MIN = 1;

export interface Gransvarde {
  aktiv: boolean;
  delete: boolean;
  id: number;
  matningstypId: number;
  typAvKontroll: number;
  gransvarde: string;
  larmnivaId: number;
  larmTillAnvandargruppId: number;
  matobjektNamn: string;
  larmnivaNamn: string;
  canDeactivate: boolean;
}

@Injectable({
  providedIn: "root"
})

export class GransvardeService {
  constructor(private http: HttpClient) {
  }

  get(id: number): Observable<Gransvarde> {
    return this.http.get<Gransvarde>("/api/gransvarde/" + id);
  }

  getGransvardenForMatningstyp(matningstypId: number): Observable<Gransvarde[]> {
    return this.http.get<Gransvarde[]>("/api/matningstyper/" + matningstypId + "/gransvarden");
  }

  put(id: number, save: Partial<Gransvarde>) {
    return this.http.put("/api/gransvarde/" + id, save);
  }

  post(save: Partial<Gransvarde>) {
    return this.http.post("/api/gransvarde", save).pipe(
      map(response => {
        return response as Gransvarde;
      })
    );
  }

  delete(id: number) {
    return this.http.delete("/api/gransvarde/" + id);
  }

  canDelete(id: number): Observable<boolean> {
    return this.http.options("/api/gransvarde/" + id, { observe: "response" }).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }

  canDeactivate(id: number) {
    return this.http.get("/api/gransvarde/" + id + "/canDeactivate");
  }
}
