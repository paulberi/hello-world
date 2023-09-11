import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { MarkkollUser, RoleType } from "../../../../../../../generated/markkoll-api";
import { UserWithRoll } from "./projekt-behorighet.container";

export class MkProjektBehorighetPresenter {
  change = new EventEmitter<UserWithRoll[]>();
  valid = new EventEmitter<boolean>(false);

  private _form: UntypedFormGroup;
  private _usersWithNewRoll = [];
  private _loggedInUser: MarkkollUser;
  private _originalProjektUsers: UserWithRoll[] = [];


  get form(): UntypedFormGroup {
    return this._form;
  }

  get usersWithNewRoll(): UserWithRoll[] {
    return this._usersWithNewRoll;
  }

  initializeForm(loggedInUser: MarkkollUser) {
    this._loggedInUser = loggedInUser;
    this._form = new UntypedFormGroup({
      anvandare: new UntypedFormControl(this._originalProjektUsers),
     });
    this.change.emit(this.getFormData());
    this.registerOnChange();
  }

  updateUserRoleValue(roll: RoleType, index) {
    const formValue = this._form.controls.anvandare.value;
    formValue[index] = {user: formValue[index].user, roll: roll};
    this._form.controls['anvandare'].setValue(formValue);
  }

  removeUserAtIndex(index) {
    const formValue: UserWithRoll[] = this._form.controls.anvandare.value;
    formValue.splice(index, 1);
    this._form.controls['anvandare'].setValue(formValue);
  }

  setupUsers(users: MarkkollUser[], originalProjektUsers: UserWithRoll[], loggedInUser: MarkkollUser, readonly: boolean) {
    let currentUser: UserWithRoll;
    const kundadmins: UserWithRoll[] = [];
    this._originalProjektUsers = [];

    users.forEach(user => {
      if (user.id !== loggedInUser.id) {
        const originalUser = originalProjektUsers.find(userWithRoll => userWithRoll.user.id === user.id);
        if (!this.isKundAdmin(user)) {
          if (originalUser) {
            this._originalProjektUsers.push({user: user, roll: originalUser.roll});
            this._usersWithNewRoll.push({user: user, roll: originalUser.roll});
          } else {
            this._usersWithNewRoll.push({user: user, roll: RoleType.PROJEKTHANDLAGGARE});
          }
        } else {
          kundadmins.push({user: user, roll: RoleType.KUNDADMIN} as UserWithRoll);
        }
      } else {
        if (readonly) {
          currentUser = {user: user, roll: RoleType.PROJEKTHANDLAGGARE};
        } else {
          currentUser = {user: user, roll: this.isKundAdmin(user) ? RoleType.KUNDADMIN : RoleType.PROJEKTADMIN};
        }
      }
    });

    // Lägger under foreach-loopen för att se till att de hamnar längst ner i listan.
    kundadmins.forEach(user => {
      this._usersWithNewRoll.push(user);
      this._originalProjektUsers.push(user);
    });

    // Och inloggade användaren allra sist
    if (!!currentUser) {
      this._usersWithNewRoll.push(currentUser);
      this._originalProjektUsers.push(currentUser);
    }

    return this._usersWithNewRoll;
  }

  canSave(): boolean {
    return this._form.valid;
  }

  registerOnChange() {
    this._form.valueChanges.subscribe(form => {
      this.change.emit(this.getFormData());
      this.valid.emit(this.canSave());
    });
  }

  getFormData(): UserWithRoll[] {
    const controls = this.form.controls;
    const value: UserWithRoll[] = [...controls.anvandare.value];
    return value.filter(userWithRoll => !this.isKundAdmin(userWithRoll.user) && userWithRoll.user.id !== this._loggedInUser.id);
  }

  private isKundAdmin(user: MarkkollUser): boolean {
    return !!user.roles
      .find(role => role.roleType === RoleType.KUNDADMIN && role.objectId === user.kundId);
  }
}
