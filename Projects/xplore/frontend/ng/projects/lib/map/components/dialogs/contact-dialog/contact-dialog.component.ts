import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {ConfigService} from "../../../../config/config.service";

@Component({
  selector: 'xp-contact-dialog',
  templateUrl: "./contact-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./contact-dialog.component.scss"],
})
export class ContactDialogComponent {
  customerName?: string;
  address?: string;
  postalCode?: string;
  supportEmail?: string;
  supportTel?: string;
  extraInfo?: string;
  addressExists: boolean;
  supportEmailExists: boolean;
  supportTelExists: boolean;
  extraInfoExists: boolean;
  newValuesExist: boolean;

  constructor(configService: ConfigService, public dialogRef: MatDialogRef<ContactDialogComponent>) {
    var contactDef = configService.config.app.contactDef;
    this.customerName = "Metria AB";
    this.address = "";
    this.postalCode = "801 83 Gävle, Sverige";
    this.supportEmail = "support@metria.se";
    this.supportTel = "010 - 121 81 00";
    this.extraInfo = "";
    this.addressExists = false;
    this.supportEmailExists = false;
    this.supportTelExists = false;
    this.extraInfoExists = false;
    this.newValuesExist = false;

    if (configService.config.app.contactDef !== undefined)
    {
      if (configService.config.app.contactDef.hasOwnProperty.length > 0) {
        this.newValuesExist = true;
        this.customerName = "";
        this.address = "";
        this.postalCode = "";
        this.supportEmail = "";
        this.supportTel = "";
        this.extraInfo = "";
      }
      else {
        this.supportEmailExists = true;
        this.supportTelExists = true;
      }
    }
    else {
      this.supportEmailExists = true;
      this.supportTelExists = true;
    }

    if (this.newValuesExist) {
      if (contactDef.customerName !== undefined && contactDef.customerName !== "")
        this.customerName = contactDef.customerName;

      if (contactDef.address !== undefined && contactDef.address !== "") {
        this.address = contactDef.address;
        this.addressExists = true;
      }

      if (contactDef.postalCode !== undefined && contactDef.postalCode !== "")
        this.postalCode = contactDef.postalCode;

      if (contactDef.supportEmail !== undefined && contactDef.supportEmail !== "") {
        this.supportEmail = contactDef.supportEmail;
        this.supportEmailExists = true;
      }

      if (contactDef.supportTel !== undefined && contactDef.supportTel !== "") {
        this.supportTel = contactDef.supportTel;
        this.supportTelExists = true;
      }

      if (contactDef.extraInfo !== undefined && contactDef.extraInfo !== "") {
        this.extraInfo = contactDef.extraInfo;
        this.extraInfoExists = true;
      }
    }
  }

  close(): void {
    this.dialogRef.close();
  }
}
