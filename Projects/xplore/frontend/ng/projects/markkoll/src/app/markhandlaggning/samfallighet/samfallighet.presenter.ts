import { GeometryType } from "../../../../../../generated/markkoll-api";
import { MkAvtalMap } from "../../model/avtalskarta";

export class MkSamfallighetPresenter {
  isMittlinjeRedovisad(avtalskarta: MkAvtalMap): boolean {
    if (avtalskarta == null || avtalskarta.omraden == null) {
      return false;
    }

    return avtalskarta.omraden.some(o => {
      return o.geometryType === GeometryType.LineString ||
        o.geometryType === GeometryType.MultiLineString;
    });
  }
}
