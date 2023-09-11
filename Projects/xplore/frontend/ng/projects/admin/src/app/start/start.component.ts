import { Component, OnInit } from "@angular/core";
import { Observable, of } from "rxjs";
import { Router } from "@angular/router";
import { KeycloakService, RealmList } from "../../../../../generated/admin-api";

@Component({
  selector: "adm-start",
  templateUrl: "./start.component.html",
  styleUrls: ["./start.component.scss"]
})
export class AdmStartComponent implements OnInit {

  realmList$: Observable<RealmList>;
  selectedRealm: string;

  constructor(private router: Router, private apiService: KeycloakService) {
  }

  ngOnInit() {
    this.realmList$ = this.apiService.keycloakRealmsGet();
  }

  onRealmChange(obj) {
    this.selectedRealm = obj.value;
  }

  onButtonClick() {
    this.router.navigate([this.selectedRealm, "anvandare"]);
  }
}
