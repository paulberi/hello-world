import { Avtalsstatus } from "../../../../../generated/markkoll-api";
import { ActionTyp, SelectionTyp } from "./actions";
import { uuid } from "./uuid";

export interface MkAvtalsAction {
  actionTyp: ActionTyp;
  selection?: SelectionTyp;
  dokumentmallId?: uuid;
  avtalsstatus?: Avtalsstatus;
}
