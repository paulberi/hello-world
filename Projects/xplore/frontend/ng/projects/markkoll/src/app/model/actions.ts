import { DokumentTyp } from "../../../../../generated/markkoll-api";

export type ActionTyp = DokumentTyp | "STATUS" | "HAGLOF_HMS" | "EDIT_CONTENTS";
export const ActionTyp = {
  MARKUPPLATELSEAVTAL: "MARKUPPLATELSEAVTAL" as ActionTyp,
  INFOBREV: "INFOBREV" as ActionTyp,
  STATUS: "STATUS" as ActionTyp,
  HAGLOF_HMS: "HAGLOF_HMS" as ActionTyp,
  FORTECKNING: "FORTECKNING" as ActionTyp,
  EDIT_CONTENTS: "EDIT_CONTENTS" as ActionTyp
};

export type SelectionTyp = "ALL" | "FILTERED" | "SELECTION";
export const SelectionTyp = {
  ALL: "ALL" as SelectionTyp,
  FILTERED: "FILTERED" as SelectionTyp,
  SELECTION: "SELECTION" as SelectionTyp,
};
