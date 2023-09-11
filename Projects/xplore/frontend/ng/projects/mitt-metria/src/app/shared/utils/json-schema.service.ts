import { Injectable } from "@angular/core";
import Ajv2020, { JSONSchemaType } from "ajv/dist/2020";

@Injectable({
  providedIn: "root"
})
export class JsonSchemaService {

  constructor() { }

  public validateJSONSchema(schema: JSONSchemaType<any>, data: any): boolean {
    if (schema && data) {
      const ajv = new Ajv2020()
      ajv.addKeyword("key") //finns i itembase men inte i json-schemat, används för formuläret
      const validate = ajv.compile(schema);
      const valid = validate(data);
      if (!valid) {
        validate.errors?.forEach(error => {
          console.error("Ogiltig produkt. " + error?.message);
        })
        console.log(valid, validate.errors)
      }
      return valid;
    }
    return false;
  }
}
