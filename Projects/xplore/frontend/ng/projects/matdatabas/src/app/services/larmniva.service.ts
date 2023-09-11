import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

export interface Larmniva {
  id: number;
  namn: string;
  beskrivning: string;
  delete: boolean;
}

@Injectable({
  providedIn: "root"
})
export class LarmnivaService {
  constructor(private http: HttpClient) {
  }

  getLarmnivaer(): Observable<Larmniva[]> {
    return this.http.get<Larmniva[]>("/api/larmniva");
  }

  put(id: number, save: Partial<Larmniva>) {
    return this.http.put("/api/larmniva/" + id, save);
  }

  post(save: Partial<Larmniva>) {
    return this.http.post("/api/larmniva", save).pipe(
      map(response => {
        return response as Larmniva;
      })
    );
  }

  delete(id: number) {
    return this.http.delete("/api/larmniva/" + id);
  }

  canDelete(id: number): Observable<boolean> {
    return this.http.options("/api/larmniva/" + id, {observe: "response"}).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }
}
