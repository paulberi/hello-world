import {MatningstypMatobjekt} from "../../../services/matobjekt.service";

export function createAktivHeaderTooltip(): string {
  return "Anger om både mätningstypen och mätobjektet är aktiva";
}

export function createAktivTooltip(mm: MatningstypMatobjekt): string {
  if (!mm.matningstypAktiv && !mm.matobjektAktiv) {
    return "Både mätningstypen och mätobjektet är inaktiva";
  } else if (!mm.matningstypAktiv) {
    return "Mätningstypen är inaktiv";
  } else if (!mm.matobjektAktiv) {
    return "Mätobjektet är inaktivt";
  }
  return "";
}
