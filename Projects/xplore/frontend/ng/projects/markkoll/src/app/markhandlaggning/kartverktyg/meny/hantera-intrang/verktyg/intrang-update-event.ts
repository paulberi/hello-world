import { IntrangMap } from "../intrang-map";

export class IntrangUpdateEvent {
  updateFn: (IntrangMap) => void;
  message: string;
}
