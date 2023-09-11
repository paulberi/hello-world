import { Component, OnInit } from "@angular/core";
import {LoginService} from "../../../../lib/oidc/login.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: "mdb-set-special-auth-component",
  template: `
    <p>
      set-special-auth
    </p>
  `,
  styles: []
})
export class SetSpecialAuthComponent implements OnInit {

  constructor(private loginService: LoginService, private route: ActivatedRoute) { }

  ngOnInit() {
    const specialAuthHeader = this.route.snapshot.queryParamMap.get("specialAuthHeader");

    if (specialAuthHeader) {
      sessionStorage.setItem("SpecialAuthHeader", specialAuthHeader);
    }
  }
}
