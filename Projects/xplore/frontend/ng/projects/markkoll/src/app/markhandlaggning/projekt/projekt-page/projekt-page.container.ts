import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { switchMap } from "rxjs/operators";
import { Abonnemang } from "../../../../../../../generated/markkoll-api";
import { uuid } from "../../../model/uuid";
import { ProjektService } from "../../../services/projekt.service";
import { MkProjektkartaLayer, MkProjektkartaService } from "../../../services/projektkarta.service";
import { MkUserService } from "../../../services/user.service";

@Component({
  selector: "mk-projekt-page",
  providers: [],
  templateUrl: "./projekt-page.container.html",
  styleUrls: []
})
export class MkProjektPageContainer implements OnInit {
  projektId: uuid = this.activatedRoute.snapshot.params.projektId;
  projektTyp: string = this.activatedRoute.snapshot.params.projektTyp;

  projektnamn$: Observable<string>;
  samradAbonnemang: Abonnemang[];

  constructor(private activatedRoute: ActivatedRoute,
              private projektService: ProjektService,
              private userService: MkUserService) {}

  ngOnInit() {
    this.projektnamn$ = this.projektService.getProjektName(this.projektId);

    this.userService.getMarkkollUser$()
    .pipe(
      switchMap(user => {
        return this.userService.getKund(user.kundId);
      })
    ).subscribe(kund => {
      this.samradAbonnemang = kund.abonnemang.filter((abonnemang) => abonnemang.produkt === "SAMRAD")
    });
  }
}
