import {Rapport, RapportService} from "../services/rapport.service";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: "mdb-rapport-page",
  template: `
    <mdb-rapport *ngIf="rapport" [rapport]="rapport"></mdb-rapport>
  `
})

export class RapportPageComponent implements OnInit {
  rapport: Rapport;

  constructor(private rapportService: RapportService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get("id");
    this.rapportService.getRapport(id).subscribe(rapport => this.rapport = rapport);
  }
}
