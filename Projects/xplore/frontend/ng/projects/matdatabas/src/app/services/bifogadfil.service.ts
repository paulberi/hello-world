import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

export interface BifogadFil {
  id: string;
  filnamn: string;
  mimeTyp: string;
  skapad_datum: string;
  link?: string;
}

@Injectable({
  providedIn: "root"
})
export class BifogadfilService {
  constructor(private http: HttpClient) {
  }

  get(id: number): Observable<BifogadFil> {
    return this.http.get<BifogadFil>(`/api/bifogadfil/${id}`).pipe(
      map(this.addLink)
    );
  }

  getList(ids: number[]): Observable<BifogadFil[]> {
    return this.http.get<BifogadFil[]>(`/api/bifogadfil?ids=${ids}`).pipe(
      map((bifogadeFiler: BifogadFil[]) => bifogadeFiler.map(this.addLink))
    );
  }

  post(file: File) {
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<BifogadFil>("/api/bifogadfil", formData).pipe(
      map(this.addLink)
    );
  }

  addLink(bifogadFil: BifogadFil): BifogadFil {
    bifogadFil.link =  `/api/bifogadfil/${bifogadFil.id}/data`;
    return bifogadFil;
  }

  getDataLink(id: string): string {
    return `/api/bifogadfil/${id}/data`;
  }
}
