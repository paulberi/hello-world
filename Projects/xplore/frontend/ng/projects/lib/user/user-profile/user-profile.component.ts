import { Component, Input, OnInit } from "@angular/core";
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";

import { User } from "../../oidc/login.service";
import { PasswordChange, XpUserService } from "../user.service";

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: UntypedFormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);

    return control.parent.errors && control.parent.errors  && control.touched && ( invalidCtrl || invalidParent );
  }
}

@Component({
  selector: "xp-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"]
})
export class XpUserProfileComponent implements OnInit {
  user: User;
  emailStatus: string;
  changePasswordStatus: string;

  emailForm: UntypedFormGroup;
  email = new UntypedFormControl(null, [Validators.required, Validators.email]);

  passwordForm: UntypedFormGroup;
  currentPassword = new UntypedFormControl(null, [Validators.required]);
  newPassword = new UntypedFormControl(null, [Validators.required, Validators.minLength(16)]);
  confirmation = new UntypedFormControl(null, [Validators.required]);
  passwordMatcher = new MyErrorStateMatcher();

  hideCurrent = true;
  hideNew = true;
  hideConfirm = true;

  @Input() authIssuer?: string = "/configAuthIssuer"

  constructor(private formBuilder: UntypedFormBuilder, private xpUserService: XpUserService) {
    this.emailForm = formBuilder.group({
      email: this.email
    });
    this.passwordForm = formBuilder.group({
      currentPassword: this.currentPassword,
      newPassword: this.newPassword,
      confirmation: this.confirmation
    }, {validator: [this.checkPasswords] });
  }

  ngOnInit() {
    this.xpUserService.getUser$().subscribe(user => {
      this.user = user;
    });
    this.email.setValue(this.user.claims.email);
  }

  changePassword(passwordData: PasswordChange) {
    this.xpUserService.updateKeyCloakPassword$(passwordData, this.authIssuer).subscribe(() => {
      this.changePasswordStatus = "Lyckades!";
      this.passwordForm.reset();
    },
    err => {
      this.changePasswordStatus = "Misslyckades!";
      if (err.status === 400) {
        switch (err.error.errorMessage) {
          case "invalidPasswordMinLengthMessage":
            this.newPassword.setErrors({toShort: true});
            break;
          case "invalidPasswordExistingMessage":
            this.currentPassword.setErrors({wrongPass: true});
            break;
        }
      }
    });
  }

  changeEmail(email: string) {
    this.xpUserService.updateKeyCloakEmail$(email, this.authIssuer)
    .subscribe(() => {
      this.emailStatus = "Lyckades!";
    },
    error => {
      this.emailStatus = "Misslyckades!";
    });
  }

  checkPasswords(group: UntypedFormGroup) {
    const pass = group.get("newPassword").value;
    const confirmPass = group.get("confirmation").value;

    return pass === confirmPass ? null : { notSame: true };
  }
}
