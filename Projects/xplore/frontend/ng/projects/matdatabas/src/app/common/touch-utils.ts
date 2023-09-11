export function isTouch() {
  return window.matchMedia("(pointer: coarse)").matches;
}
