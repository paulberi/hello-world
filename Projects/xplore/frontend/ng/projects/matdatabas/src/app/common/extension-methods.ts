export { }

declare global {
  interface Array<T> {
    removeAtIndex(index: number): Array<T>;
    add(elem: T): Array<T>;
    has(elem: T): boolean;
  }
}

Array.prototype.removeAtIndex = function <T>(index: number): T[] {
  const this_out = [...this];
  this_out.splice(index, 1);
  return this_out;
}

Array.prototype.add = function <T>(elem: T): T[] {
  return [...this, elem];
}

Array.prototype.has = function <T>(elem: T): boolean {
  return this.some(i => i === elem);
}
