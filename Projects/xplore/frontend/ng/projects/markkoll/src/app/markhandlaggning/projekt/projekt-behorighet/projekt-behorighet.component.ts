import { Component, Input, OnInit, Output, TemplateRef, ViewChild } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { forkJoin, Observable } from "rxjs";
import { MarkkollUser, RoleType } from "../../../../../../../generated/markkoll-api";
import { UserWithRoll } from "./projekt-behorighet.container";
import { MkProjektBehorighetPresenter } from "./projekt-behorighet.presenter";

@Component({
  providers: [MkProjektBehorighetPresenter],
  selector: "mk-projekt-behorighet-ui",
  templateUrl: "./projekt-behorighet.component.html",
  styleUrls: ["./projekt-behorighet.component.scss"]
})
export class MkProjektBehorighetComponent implements OnInit {

  roles = [RoleType.PROJEKTADMIN, RoleType.PROJEKTHANDLAGGARE];

  /**
   * Array med användare man kan lägga till till projektet
   */
  @Input() users: Observable<MarkkollUser[]>;

  @Input() loggedInUser: MarkkollUser;

  @Input() originalProjektUsers$: Observable<UserWithRoll[]>;

  @Input() createProjekt: boolean;

  @Input() readonly = false;

  @Input() label = this.translateService.translate("mk.createProjekt.addUsersToProject");

  /** 
   * Event när värden i formuläret ändras.
   **/
  @Output() formChange =  this.presenter.change;

  /** 
   * Event med en flagga om formuläret är rätt ifyllt.
   **/
  @Output() valid =  this.presenter.valid;

  readonly RoleType = RoleType;

  @ViewChild("infoDialog") infoDialog: TemplateRef<any>;
  
  constructor(private translateService: TranslocoService,
              private matDialog: MatDialog,
              private presenter: MkProjektBehorighetPresenter) { }

  ngOnInit(): void {
    if (this.createProjekt) {
      this.users.subscribe(selectableUsers => {
        this.presenter.setupUsers(selectableUsers, [], this.loggedInUser, this.readonly);
        this.presenter.initializeForm(this.loggedInUser);
      });
    } else {
      forkJoin([this.users, this.originalProjektUsers$]).subscribe(result => {
          this.presenter.setupUsers(result[0], result[1], this.loggedInUser, this.readonly);
          
          this.presenter.initializeForm(this.loggedInUser);
        });
    }
  }

  infoPopupClick() {
    this.matDialog.open(this.infoDialog, {
      width: "600px"
    });
  }

  updateUserRoleValue(event, index) {
    this.presenter.updateUserRoleValue(event, index);
  }

  isUserDisabled(user: MarkkollUser): boolean {
    return user.id === this.loggedInUser.id ||
    !!user.roles.find(role => role.roleType === RoleType.KUNDADMIN && role.objectId === user.kundId);
  }
  
  removeUser(index) {
    this.presenter.removeUserAtIndex(index);
  }

  get usersWithNewRoll(): UserWithRoll[] {
    return this.presenter.usersWithNewRoll;
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  attributeDisplay(attribute1: UserWithRoll, attribute2: UserWithRoll) {
    if (attribute1.user.id === attribute2.user.id) {
      return attribute1;
    } else {
      return "";
    }
  }
}
