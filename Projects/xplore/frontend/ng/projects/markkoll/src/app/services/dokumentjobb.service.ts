import { Observable } from "rxjs";
import { AvtalsjobbProgress } from "../../../../../generated/markkoll-api";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import { uuid } from "../model/uuid";

export interface DokumentjobbService {
  cancel(projektId: uuid, jobbId: uuid): Observable<void>;
  create(projektId: uuid, filter: MkAvtalsfilter, template: uuid, interval: number): Observable<AvtalsjobbProgress>;
  getData(projektId: uuid, jobbId: uuid);
  getProgress(projektId: uuid, interval: number): Observable<AvtalsjobbProgress>;
  reset(projektId: uuid);
}
