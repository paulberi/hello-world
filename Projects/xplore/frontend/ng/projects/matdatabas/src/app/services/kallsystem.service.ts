import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface Kallsystem {
  namn: string;
  beskrivning: string;
  defaultGodkand: boolean;
  manuellImport: boolean;
  tips: string;
}

@Injectable({
  providedIn: "root"
})
export class KallsystemService {
  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Kallsystem[]>  {
    return this.http.get<Kallsystem[]>("/api/kallsystem");
  }
}
