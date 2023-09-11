export default class ExtractorUtil {
  static extractStringValue(path: string, object: any, fallback?: string): string {
    const obj = ExtractorUtil.extractObject(path, object);
    return (obj && (typeof obj === "string")) ? obj as string : fallback;
  }

  static extractNumberValue(path: string, object: any, fallback?: number): number {
    const obj = ExtractorUtil.extractObject(path, object);
    return (obj && (typeof obj === "number")) ? obj as number : fallback;
  }

  static extractBooleanValue(path: string, object: any, fallback?: boolean): boolean {
    const obj = ExtractorUtil.extractObject(path, object);
    return (!(obj === null || obj === undefined) && (typeof obj === "boolean")) ? obj as boolean : fallback;
  }

  static extractObject(path: string, object: any, fallback?: Object): Object {
    const parts = path.split(".");
    let obj = object[parts.shift()];
    while (obj && parts.length) {
      obj = obj[parts.shift()];
    }
    return !(obj === undefined || obj === null) ? obj : fallback;
  }
}
