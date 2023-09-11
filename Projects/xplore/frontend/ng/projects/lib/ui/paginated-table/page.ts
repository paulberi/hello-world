/*
* Representation av en sida, med innehåll
*/
export class XpPage<T> {

  /** Sidinnehåll */
  content: T[];
  /** Sidnummer */
  number: number;
  /** Antalet element på sidan */
  numberOfElements: number;
  /** Sammanlagda antalet element över alla sidor */
  totalElements: number;
  /** Sammanlagda antalet sidor */
  totalPages: number;

  constructor() {
    this.content = [];
    this.number = 0;
    this.numberOfElements = 0;
    this.totalElements = 0;
    this.totalPages = 0;
  }
}
