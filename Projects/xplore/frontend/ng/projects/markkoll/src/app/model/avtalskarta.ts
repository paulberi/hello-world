import { FastighetDelomrade } from "../../../../../generated/markkoll-api";

export interface MkAvtalMap {
  fastighetsId: string;
  fastighetsbeteckning: string;
  extent: Array<number>;
  projektId: string;
  omraden: FastighetDelomrade[];
}
