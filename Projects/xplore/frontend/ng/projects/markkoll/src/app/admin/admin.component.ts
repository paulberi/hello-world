import { Component, Input } from "@angular/core";
import { Kund } from "../../../../../generated/kundconfig-api";
import { MarkkollUser } from "../../../../../generated/markkoll-api";

@Component({
  selector: "mk-admin-ui",
  templateUrl: "./admin.component.html",
  styleUrls: ["./admin.component.scss"],
  providers: []
})
export class MkAdminComponent {
  @Input() loggedInUser: MarkkollUser;

  @Input() activeKund: Kund;

  constructor() { }
}
