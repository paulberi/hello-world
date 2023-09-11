import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ name: "andelFormat" })
export class MkAndelFormatPipe implements PipeTransform {
  transform(value: string) {
    if(!value) {
      return "";
    }
    return value.replace("/", " / ");
  }
}
