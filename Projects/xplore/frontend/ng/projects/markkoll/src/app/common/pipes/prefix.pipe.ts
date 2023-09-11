import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ name: "prefix" })
export class MkPrefixPipe implements PipeTransform {
  transform(value: string, prefix: string) {
    return prefix + value;
  }
}
