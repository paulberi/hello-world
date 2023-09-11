import { MkAvtalsfilter } from "./avtalsfilter";

export interface MkAvtalPageEvent {
  filter: MkAvtalsfilter;
  page: number;
  size: number;
  type?: PageTyp;
}

export enum PageTyp {
  FAST = "FAST",
  SAMF = "SAMF"
}
