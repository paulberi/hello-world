import { UserDetails } from "../../../../../generated/admin-api";

export const users: Array<UserDetails> = [
  {
    username: "Kalle Anka",
    email: "kalle.anka@metria.se",
    enabled: true,
    roles: [
      { name: "markkoll_markagare", description: "Markägare i Markkoll" }
    ],
  },
  {
    username: "Kajsa Anka",
    email: "kajsa.anka@metria.se",
    enabled: true,
    roles: [
      { name: "markkoll_markagare", description: "Markägare i Markkoll" },
      { name: "markkoll_admin", description: "Admin i Markkoll" }],
  }
];
