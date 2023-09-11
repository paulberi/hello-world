// Förenklad type av JSONType som finns i ajv-paketet (tagit bort integer)
declare const _jsonTypes: readonly ["string", "number", "boolean", "null", "object", "array"];
export declare type JSONType = typeof _jsonTypes[number];
export declare function isJSONType(x: unknown): x is JSONType;

// Förenklad class av UncheckedJSONSchemaType som finns i ajv-paketet
export class Property<T> {
  value?: T | undefined;
  key: string;
  title: string;
  description?: string;
  type?: JSONType;
  items?: Property<T>[];
  enum?: any[];
  minItems?: number;
  maxItems?: number;
  minimum?: number;
  maximum?: number;
  properties?: Property<T>;
  const?: string | number | boolean | null;

  constructor(options: {
    value?: T;
    key?: string;
    title?: string;
    description?: string;
    type?: JSONType;
    items?: Property<T>[];
    enum?: any[];
    minItems?: number;
    maxItems?: number;
    minimum?: number;
    maximum?: number;
    properties?: Property<T>;
    const?: string | number | boolean | null;
  } = {}) {
    this.value = options.value;
    this.key = options.key || "";
    this.title = options.title || "";
    this.description = options.description || "";
    this.type = options.type;
    this.items = options.items || [];
    this.enum = options.enum || [];
    this.minItems = options.minItems || null;
    this.maxItems = options.maxItems || null;
    this.minimum = options.minimum || null;
    this.maximum = options.maximum || null;
    this.properties = options.properties || null;
    this.const = options.const || null;
  }
}

export function hasProperty(schema: Property<string>, property: string): boolean {
  return schema.properties?.hasOwnProperty(property);
}

export function getEnumFromProperties(properties: Property<string>[], key: string): string[] {
  const object = properties.find(property => property.key === key);
  return object.enum;
}