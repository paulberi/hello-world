import { Avtalsstatus } from "../../../../../generated/markkoll-api";
import { MkNoteringar } from "./noteringar";
import { uuid } from "./uuid";

export interface MkAvtalSummary {
  fastighetId: uuid;
  fastighetsbeteckning: string;
  avtalsstatus: Avtalsstatus;
  information: MkNoteringar;
  notiser: string[];
}
