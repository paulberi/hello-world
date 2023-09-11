import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { RoleType } from "../../../../../generated/kundconfig-api";
import { MarkkollUser } from "../../../../../generated/markkoll-api";
import { UserOption, UserRoleEntry } from "../markhandlaggning/projekt/user-roles/user-roles.component";
import { ProjektRolesService } from "./projekt-roles.service";
import { MkUserService } from "./user.service";

describe(ProjektRolesService.name, () => {
  let mkUserService: MkUserService;
  let projektRolesService: ProjektRolesService;

  const LOGGED_IN_USER: MarkkollUser = {
      id: "2",
      fornamn: "Bertrand",
      efternamn: "Russell",
      kundId: "kundId",
      roles: [
        {
          objectId: "projektId",
          roleType: RoleType.PROJEKTADMIN
        }
      ]
  }

  const KUND_USERS: MarkkollUser[] = [
    {
      id: "1",
      fornamn: "Jean",
      efternamn: "Baudrillard",
      kundId: "kundId",
      roles: [
        {
          objectId: "kundId",
          roleType: RoleType.KUNDADMIN
        },
        {
          objectId: "projektId",
          roleType: RoleType.PROJEKTHANDLAGGARE
        }
      ]
    },
    {
      id: "2",
      fornamn: "Bertrand",
      efternamn: "Russell",
      kundId: "kundId",
      roles: [
        {
          objectId: "projektId",
          roleType: RoleType.PROJEKTADMIN
        }
      ]
    },
    {
      id: "3",
      fornamn: "Baruch",
      efternamn: "Spinoza",
      kundId: "kundId",
      roles: [
        {
          objectId: "projektId",
          roleType: RoleType.PROJEKTHANDLAGGARE
        }
      ]
    },
    {
      id: "4",
      fornamn: "Immanuel",
      efternamn: "Kant",
      kundId: "kundId",
      roles: [
        {
          objectId: "kundId",
          roleType: RoleType.KUNDANVANDARE
        }
      ]
    }
  ]

  beforeEach(() => {
    mkUserService = MockService(MkUserService, {
      getMarkkollUser: jest.fn().mockReturnValue(LOGGED_IN_USER),
      getKundUsers: jest.fn().mockReturnValue(of(KUND_USERS))
    });
    projektRolesService = new ProjektRolesService(mkUserService);
  });

  it("Ska hämta projektroller", async () => {
    // Given
    const projektId = "projektId";

    const userOptions: UserOption[] = [
      { user: KUND_USERS[0], disabled: true },
      { user: KUND_USERS[1], disabled: true },
      { user: KUND_USERS[2], disabled: false },
      { user: KUND_USERS[3], disabled: false }
    ];

    const entries: UserRoleEntry[] = [
      { user: KUND_USERS[0], roll: RoleType.KUNDADMIN, disabled: true },
      { user: KUND_USERS[1], roll: RoleType.PROJEKTADMIN, disabled: true },
      { user: KUND_USERS[2], roll: RoleType.PROJEKTHANDLAGGARE, disabled: false }
    ];

    // when
    const roles = await projektRolesService.getProjektRoles(projektId).toPromise();

    // Then
    expect(roles.userOptions).toEqual(userOptions);
    expect(roles.entries).toEqual(entries);
  });
});
