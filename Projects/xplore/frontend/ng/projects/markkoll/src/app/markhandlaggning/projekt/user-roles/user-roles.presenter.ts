import { FormControl, FormGroup } from "@angular/forms";
import { UserRoleEntry, UserOption } from "./user-roles.component";

export class MkUserRolesPresenter {
  form: FormGroup;

  initializeForm(entries: UserRoleEntry[], userOptions: UserOption[]) {
    this.form = new FormGroup({
      userOptions: new FormControl(this.selectedOptions(entries, userOptions))
    });
  }

  updateSelectedOptions(entries: UserRoleEntry[], userOptions: UserOption[]) {
    this.form.controls.userOptions.setValue(this.selectedOptions(entries, userOptions));
  }

  private selectedOptions(entries: UserRoleEntry[], userOptions: UserOption[]) {
    const entriesIds = entries?.map(e => e.user.id) || [];
    const ret = userOptions?.map(opt => opt.user)
      .filter(user => entriesIds.includes(user.id)) || [];
    return ret;
  }
}
