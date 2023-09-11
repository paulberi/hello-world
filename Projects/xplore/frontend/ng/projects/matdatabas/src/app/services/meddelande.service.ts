import { Injectable } from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";

export interface Meddelande {
  id: number;
  rubrik: string;
  datum: string;
  meddelande: string;
  url: string;
}

@Injectable({
  providedIn: "root"
})
export class MeddelandeService {

  constructor(private http: HttpClient) { }

  getPage(page: number, size: number = 3): Observable<Page<Meddelande>> {
    return this.http.get<Page<Meddelande>>("/api/meddelande?page=" + page + "&size=" + size);
  }

  getMeddelande(id: number): Observable<Meddelande> {
    return this.http.get<Meddelande>("/api/meddelande/" + id);
  }

  putMeddelande(id: number, meddelande: Meddelande) {
    return this.http.put<Meddelande>("/api/meddelande/" + id, meddelande);
  }

  deleteMeddelande(id) {
    return this.http.delete("/api/meddelande/" + id);
  }

  postMeddelande(meddelande: Meddelande) {
    return this.http.post<Meddelande>("/api/meddelande", meddelande);
  }
}
