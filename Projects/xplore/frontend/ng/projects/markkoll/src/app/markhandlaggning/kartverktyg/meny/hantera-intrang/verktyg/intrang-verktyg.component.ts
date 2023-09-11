import { Observable } from "rxjs";
import { IntrangUpdateEvent } from "./intrang-update-event";

export interface IntrangVerktygComponent<T> {
  get intrangUpdate$(): Observable<IntrangUpdateEvent>;

  onVerktygUpdate(event: T);
}
