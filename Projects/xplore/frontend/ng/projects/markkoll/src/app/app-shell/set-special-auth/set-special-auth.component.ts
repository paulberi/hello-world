import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "mk-set-special-auth",
  templateUrl: "./set-special-auth.component.html",
  styleUrls: ["./set-special-auth.component.scss"]
})
export class MkSetSpecialAuthComponent implements OnInit {

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    const specialAuthHeader = this.route.snapshot.queryParamMap.get("specialAuthHeader");

    if (specialAuthHeader) {
      sessionStorage.setItem("SpecialAuthHeader", specialAuthHeader);
      sessionStorage.setItem("access_token", specialAuthHeader);

    }
  }
}
