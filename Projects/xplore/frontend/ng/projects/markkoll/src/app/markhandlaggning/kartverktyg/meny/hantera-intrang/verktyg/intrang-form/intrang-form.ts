import { IntrangOption } from "./intrang-form.component";

export interface MkIntrangForm {
  get intrangOptions(): IntrangOption[];

  isValid(allowDashes: boolean): boolean;
  hasUnsavedChanges(): boolean;
}
