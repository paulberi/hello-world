import { CreateUsersReport } from "../../../../../generated/admin-api";

export const reports: CreateUsersReport = {
  "message": "Some users not created due to bad input",
  "statuses": [
    {
      "user": {
        "username": "knatte",
        "email": "knatte.anka@metria.se",
        "firstname": "Knatte",
        "lastname": "Anka",
        "roles": [
          {
            "name": "markkoll_markagare",
            "description": ""
          }
        ],
        "enabled": true,
      },
      "action": "CREATE",
      "status": "FAIL",
      "message": "Email is already in use.\\nRoles does not exist on server role_1, role_2, role_3."
    },
    {
      "user": {
        "username": "fnatte",
        "email": "fnatte.anka@metria.se",
        "firstname": "Fnatte",
        "lastname": "Anka",
        "roles": [
          {
            "name": "markkoll_markagare",
            "description": ""
          }
        ],
        "enabled": true,
      },
      "action": "CREATE",
      "status": "FAIL",
      "message": "Email is already in use.\\nRoles does not exist on server role_1, role_2, role_3."
    },
    {
      "user": {
        "username": "tjatte",
        "email": "tjatte.anka@metria.se",
        "firstname": "Tjatte",
        "lastname": "Anka",
        "roles": [
          {
            "name": "markkoll_markagare",
            "description": ""
          }
        ],
        "enabled": true,
      },
      "action": "CREATE",
      "status": "FAIL",
      "message": "Email is already in use.\\nRoles does not exist on server role_1, role_2, role_3."
    }
  ]
};

