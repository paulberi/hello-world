import { Component, OnInit } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: "mh-sidfot",
  templateUrl: "./sidfot.component.html",
  styleUrls: ["./sidfot.component.scss"]
})
export class SidfotComponent implements OnInit {

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      "logo",
      sanitizer.bypassSecurityTrustResourceUrl("../../assets/images/logo.svg")
    );
   }

  ngOnInit() {
  }

}
