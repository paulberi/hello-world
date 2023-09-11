import { Server } from "miragejs";
import { realms, users, reports } from "./mock";

/**
 * Creates a mock server using MirageJs, https://miragejs.com
 * @param environment Default environment is "test", which disables logging,
 * removes artificial latency and ignores initial seeds.
 * Set to "development" to use locally.
 */

export default (environment: string = "test") => {
  return new Server({
    environment: environment,
    seeds(server) {
      server.db.loadData({
        realms,
        users,
        reports
      });
    },
    routes() {
      this.namespace = "/api/admin";

      // Get list of realms
      this.get("/keycloak/realms", schema => ({ realms: schema.db.realms}));

      // Get list of users from realm
      this.get("/keycloak/realms/:realm/users", schema => ({ users: schema.db.users }));

      // Post users to realm and return validation report
      this.post("/keycloak/realms/:realm/users", (schema, request) => {

        // Insert users if dry-run is false
        if (request.queryParams["dry-run"] === "false") {
          schema.db.users.insert({
            username: "Knatte Anka",
            email: "knatte.anka@metria.se",
            enabled: true,
            roles: [
              { name: "markkoll_markagare", description: "Markägare i Markkoll" }
            ],
          });
          schema.db.users.insert({
            username: "Fnatte Anka",
            email: "fnatte.anka@metria.se",
            enabled: true,
            roles: [
              { name: "markkoll_markagare", description: "Markägare i Markkoll" }
            ],
          });
          schema.db.users.insert({
            username: "Tjatte Anka",
            email: "tjatte.anka@metria.se",
            enabled: true,
            roles: [
              { name: "markkoll_markagare", description: "Markägare i Markkoll" }
            ],
          });
        }
        return {
          message: schema.db.reports[0].message,
          statuses: schema.db.reports[0].statuses
        };
      });

      // Let requests to Keycloak in utv through
      this.passthrough("https://keycloak-utv.prodstod.se/**");
    }
  });
};
