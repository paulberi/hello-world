import { Observable } from "rxjs";

export interface MkIntrangVerktyg<T> {
  activate();

  inactivate();

  reset();

  get verktygUpdate$(): Observable<T>;
}
