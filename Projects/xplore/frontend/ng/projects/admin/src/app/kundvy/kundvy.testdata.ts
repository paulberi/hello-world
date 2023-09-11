import { Kund, XpUser } from "../../../../../generated/kundconfig-api";

export const kunder: Kund[] = [
  {
    id: "5c30c458-04cf-44b7-9d81-820379325b81",
    namn: "Metria AB",
    kontaktperson: "Emil Edberg",
    epost: "emil@metria.se",
    telefon: "0701231212",
    skapadAv: "Emil Edberg",
    skapadDatum: "2021-06-18T12:37:18.948284",
    andradAv: "Emil Edberg",
    andradDatum: "2021-06-18T12:37:18.948284"
  },
  {
    id: "ec19cb80-6003-4d3b-b52c-89289fa634ff",
    namn: "Oneco",
    kontaktperson: "Emil Edberg",
    epost: "magnus@oneco.se",
    telefon: "0701231212",
    skapadAv: "Emil Edberg",
    skapadDatum: "2021-06-18T15:37:02.391246",
    andradAv: "Emil Edberg",
    andradDatum: "2021-06-18T15:37:02.391246"
  },
  {
    id: "12345-6789-abc",
    namn: "Piteå Fiber AB",
    kontaktperson: "Christoffer Karlsson",
    epost: "christoffer.karlsson@pitefiber.se",
    telefon: "0703659146",
    skapadAv: "Christoffer Karlsson",
    skapadDatum: "2021-08-30T16:19:27.947801",
    andradAv: "Christoffer Karlsson",
    andradDatum: "2021-08-30T16:19:27.947801"
  }
];
export const users: XpUser[] = [
  {
    id: "test@test.se",
    firstName: "Test",
    lastName: "Användare",
    email: "test@test.se",
    permissions: [{
      userId: "test@test.se",
      roll: "KUNDADMIN",
      produkt: "MARKKOLL",
      kundId: "41e89b06-3c57-4d7a-9cf6-7b2737637757"
    }]
  },
  {
    id: "test@test.se",
    firstName: "Test",
    lastName: "Användare",
    email: "test@test.se",
    permissions: [{
      userId: "test@test.se",
      roll: "KUNDADMIN",
      produkt: "MARKKOLL",
      kundId: "41e89b06-3c57-4d7a-9cf6-7b2737637757"
    }]
  }
];
