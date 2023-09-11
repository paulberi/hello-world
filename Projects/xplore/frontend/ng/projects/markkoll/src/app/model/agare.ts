import { Agartyp, AvtalspartLabels, Avtalsstatus } from "../../../../../generated/markkoll-api";
import { uuid } from "./uuid";

export interface MkAgare {
  id: uuid;
  namn: string;
  kontaktperson: boolean;
  adress: string;
  postnummer: string;
  ort: string;
  land: string;
  telefon: string;
  bankkonto: string;
  ePost: string;
  status: Avtalsstatus;
  andel: string;
  fodelsedatumEllerOrgnummer: string;
  inkluderaIAvtal: boolean;
  labels: AvtalspartLabels;
  agartyp: Agartyp;
  utbetalningsdatum: string;
}
