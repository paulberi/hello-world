import { uuid } from "./uuid";

export interface MkAvtalsfilter {
  search: string;
  status: string;
  registerenhetsIds?: uuid[];
}
