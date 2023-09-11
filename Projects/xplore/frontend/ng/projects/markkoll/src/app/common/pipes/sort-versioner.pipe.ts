import { Pipe, PipeTransform } from "@angular/core";
import moment from "moment";
import { Version } from "../../../../../../generated/markkoll-api";

@Pipe({ name: "sortVersioner" })
export class MkSortVersionerPipe implements PipeTransform {
  transform(array: Version[]) {
    let arrayCopy = [...array];
    return arrayCopy.sort(
      (a, b) => {
        const momentA = moment.utc(a.skapadDatum);
        const momentB = moment.utc(b.skapadDatum);
        return momentA.isAfter(momentB) ? -1 : 1;
      }
    );
  }
}
