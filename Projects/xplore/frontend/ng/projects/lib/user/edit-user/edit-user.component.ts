import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { MarkkollUser, UserInfo } from "../../../../generated/kundconfig-api";
import { XpEditUserPresenter } from "./edit-user.presenter";

/**
 * Den här komponenten ska främst användas för att skapa och redigera kundadministratörer 
 * i Xplore-Admin och "vanliga" användare inuti applikationerna. För att redigera en
 * avändare så skickar man in det User-objekt man vill redigera, så fylls formuläret i automatiskt.
 */

@Component({
  providers: [XpEditUserPresenter],
  selector: "xp-edit-user",
  templateUrl: "./edit-user.component.html",
  styleUrls: ["./edit-user.component.scss"]
})
export class XpEditUserComponent implements OnInit {

  /**
   * Användare att editera
   */
  @Input() user: MarkkollUser;
  
  @Input() spinnerActive = false;
  
  /**
   * Avbryt
   */
  @Output() cancel = new EventEmitter();

  /**
   * Submitta ett User-objekt
   */ 
  @Output() submit: EventEmitter<UserInfo> = this.presenter.submit;

  constructor(private presenter: XpEditUserPresenter) { }

  ngOnInit(): void {
    this.presenter.initializeForm(this.user);
  }

  canSave() {
    return this.presenter.canSave();
  }

  onSubmit() {
    this.presenter.onSubmit();
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

}
