import {AvtalMetadata, Avtalsstatus, Geometristatus, Samfallighet, TillvaratagandeTyp} from "../../../../../generated/markkoll-api";
import { MkAgare } from "./agare";
import { MkAvtalMap } from "./avtalskarta";
import { MkIntrang } from "./intrang";
import { MkLoggItem } from "./loggItem";
import { uuid } from "./uuid";

/** TODO: Det här borde bli en del av REST-api:t istället. */
export interface MkAvtal {
  id: uuid;
  lagfarnaAgare: MkAgare[];
  tomtrattsinnehavare: MkAgare[];
  ombud: MkAgare[];
  status: Avtalsstatus;
  outredd: boolean;
  intrang: MkIntrang;
  ersattning: number;
  avtalskarta: MkAvtalMap;
  loggbok: MkLoggItem[];
  anteckning: string;
  geometristatus: Geometristatus;
  skogsfastighet: boolean;
  tillvaratagandeTyp: TillvaratagandeTyp;
  metadata: AvtalMetadata;
  egetTillvaratagande: number;
  samfallighet: Samfallighet;
}
