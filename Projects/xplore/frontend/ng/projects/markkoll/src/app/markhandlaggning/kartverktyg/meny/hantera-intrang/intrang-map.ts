import { ProjektIntrang } from "../../../../../../../../generated/markkoll-api";

export class IntrangMap {
  private map = new Map<string, ProjektIntrang>();

  constructor(intrang?: ProjektIntrang[]) {
    this.setAll(intrang);
  }

  set(intrang: ProjektIntrang) {
    this.map.set(intrang.id, intrang);
  }

  setAll(intrang: ProjektIntrang[]) {
    intrang?.forEach(intrang => this.map.set(intrang.id, intrang));
  }

  delete(intrangId: string) {
    this.map.delete(intrangId);
  }

  get(intrangId: string): ProjektIntrang {
    return this.map.get(intrangId);
  }

  getAll(): ProjektIntrang[] {
    return [...this.map].map(intr => intr[1]);
  }
}
