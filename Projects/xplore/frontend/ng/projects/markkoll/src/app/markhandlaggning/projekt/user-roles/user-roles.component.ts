import { Component, EventEmitter, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges, TemplateRef, ViewChild } from "@angular/core";
import { AbstractControl, ControlValueAccessor, NG_VALIDATORS, NG_VALUE_ACCESSOR, ValidationErrors } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MarkkollUser, RoleType } from "../../../../../../../generated/markkoll-api";
import { MkUserRolesPresenter } from "./user-roles.presenter";

export interface UserRoleEntry {
  user: MarkkollUser;
  roll: RoleType;
  disabled: boolean;
}

export interface UserOption {
  user: MarkkollUser;
  disabled: boolean
}

@Component({
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MkUserRolesComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: MkUserRolesComponent,
      multi: true
    },
    MkUserRolesPresenter
  ],
  selector: "mk-user-roles",
  templateUrl: "./user-roles.component.html",
  styleUrls: ["./user-roles.component.scss"]
})
export class MkUserRolesComponent implements ControlValueAccessor, OnChanges, OnInit {
  @Input() entries: UserRoleEntry[] = [];

  @Input() userOptions: UserOption[] = [];

  @Output() entriesChange = new EventEmitter<UserRoleEntry[]>();

  @ViewChild("infoDialog") infoDialog: TemplateRef<any>;

  constructor(private presenter: MkUserRolesPresenter,
              private matDialog: MatDialog) {}

  readonly roles = [RoleType.PROJEKTADMIN, RoleType.PROJEKTHANDLAGGARE];
  readonly RoleType = RoleType;

  optionValue = [];

  private _onChange: any = () => {};
  private _onTouched: any = () => {};

  ngOnInit() {
    this.presenter.initializeForm(this.entries, this.userOptions);
    this.entriesChange.subscribe(entries => this._onChange(entries))
    this.userOptions=[];
  }


  ngOnChanges(changes: SimpleChanges): void {
    
    if ((changes.entries && !changes.entries.isFirstChange()) ||
        (changes.userOptions && !changes.userOptions.isFirstChange())) {
        this.userOptions.sort((a,b)=>a.user.fornamn>b.user.fornamn? 1:-1); 

      this.presenter.updateSelectedOptions(this.entries, this.userOptions);
      
      this.sortUserOptions();
    }
    
  }
  
  sortUserOptions(){
    return this.userOptions.sort((a,b) => {
      if(a.user.fornamn==b.user.fornamn){
        if(a.user.efternamn==b.user.efternamn) return 0;
        return (a.user.efternamn<b.user.efternamn)? -1:1;
      }else{
        return (a.user.fornamn<b.user.fornamn)? -1:1;
      }
    });
  }

  compareUsers(user1: MarkkollUser, user2: MarkkollUser) {
    return user1 && user2 && user1.id === user2.id;
  }

  infoPopupClick() {
    this.matDialog.open(this.infoDialog, {
      width: "600px"
    });
  }

  onUserOptionsChange(selectedUsers: MarkkollUser[]) {
    this.entries = this.entriesFromUsers(selectedUsers);
    this.entriesChange.emit(this.entries);
  }

  removeUser(user: MarkkollUser) {
    this.entries = this.entries.filter(e => e.user.id !== user.id);
    this.presenter.updateSelectedOptions(this.entries, this.userOptions);
    this.entriesChange.emit(this.entries);
  }

  updateUserRoleValue(newRole: RoleType, entry: UserRoleEntry) {
    entry.roll = newRole;
    this.entriesChange.emit(this.entries);
  }

  writeValue(obj: any) {
    this.entries = obj;
    this.presenter.updateSelectedOptions(this.entries, this.userOptions);
  }

  validate(control: AbstractControl): ValidationErrors | null {
    return null;
  }

  registerOnChange(fn: any) {
    this._onChange = fn;
  }

  registerOnTouched(fn: any) {
    this._onTouched = fn;
  }

  setDisabledState(isDisabled: boolean) {
  }

  get form() {
    return this.presenter.form;
  }

  private entriesFromUsers(selectedUsers: MarkkollUser[]): UserRoleEntry[] {
    let updatedEntries = [...this.entries];

    const entriesIds = this.entries.map(e => e.user.id);
    const selectedIds = selectedUsers.map(u => u.id);

    const addedUsers = selectedUsers
      .filter(u => !entriesIds.includes(u.id))
      .map(u => this.newEntry(u));
    updatedEntries = updatedEntries.concat(addedUsers);

    const removedUsers = this.entries
      .filter(e => !selectedIds.includes(e.user.id))
      .map(e => e.user);
    updatedEntries = updatedEntries.filter(e => !removedUsers.includes(e.user));

    return updatedEntries;
  }

  private newEntry(user: MarkkollUser): UserRoleEntry {
    return {
      user: user,
      roll: RoleType.PROJEKTHANDLAGGARE,
      disabled: false
    }
  }
}
