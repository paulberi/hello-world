/**
 * Standard-felmeddelanden i Xplore.
 * Läs mer på https://wiki.metria.se/display/MX/Felhantering
 */
export enum XpDefaultErrorMessage {
  BadRequest = "xp.httpError.400",
  Unauthorized = "xp.httpError.401",
  Forbidden = "xp.httpError.403",
  NotFound = "xp.httpError.404",
  InternalServerError = "xp.httpError.500",
  BadGateway = "xp.httpError.502",
  ServiceUnavailable = "xp.httpError.503",
  GatewayTimeout = "xp.httpError.504",
}
