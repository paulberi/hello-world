import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { MarkkollUser, UserInfo } from "../../../../../../generated/markkoll-api";
import { XpSelectOption } from "../../../../../lib/ui/model/selectOption";
import { MkUserFormPresenter } from "./user-form.presenter";

@Component({
  providers: [MkUserFormPresenter],
  selector: "mk-user-form",
  templateUrl: "./user-form.component.html",
  styleUrls: ["./user-form.component.scss"]
})
export class MkUserFormComponent implements OnInit {

  /**
   * Användare man vill editera, om inget stoppas in så förväntar komponenten sig att man vill skapa en ny användare
   */
  @Input() editUser: MarkkollUser;

  /**
   * Inloggade användaren
   */
  @Input() loggedInUser: MarkkollUser;

  /**
   * Submitta ett User-objekt
   */ 
  @Output() submitClick = new EventEmitter<UserInfo | MarkkollUser>();

  /**
   * Ta bort en användare
   */
  @Output() deleteClick = new EventEmitter<MarkkollUser>();
  
  @ViewChild("deleteDialog") deleteDialog: TemplateRef<any>;

  constructor(private presenter: MkUserFormPresenter, private matDialog: MatDialog) { }

  ngOnInit(): void {
    this.presenter.initializeForm(this.editUser);
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  onSubmit() {
    this.submitClick.emit(this.presenter.onSubmit());
  }

  onSave() {
    this.submitClick.emit(this.presenter.onSave());
  }

  cancel() {
    this.presenter.initializeForm(this.editUser);
  }

  deleteUser(user: MarkkollUser) {
    this.matDialog.open(this.deleteDialog).afterClosed()
      .subscribe(shouldDelete => {
        if (shouldDelete) {
          this.deleteClick.emit(user);
        }
      });
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

}
