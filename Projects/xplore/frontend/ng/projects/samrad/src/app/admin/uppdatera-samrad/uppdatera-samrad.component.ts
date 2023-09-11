import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { mergeMap } from "rxjs/internal/operators/mergeMap";
import { ProjektRsp as Projekt } from "../../../../../../generated/samrad-api";
import { InitieraKundService } from "../../services/initiera-kund.service";
import { SamradService } from "../../services/samrad-admin.service";

@Component({
  selector: "sr-uppdatera-samrad",
  templateUrl: "./uppdatera-samrad.component.html",
  styleUrls: ["./uppdatera-samrad.component.scss"],
})
export class UppdateraSamradComponent implements OnInit {
  constructor(
    private samradService: SamradService,
    private route: ActivatedRoute,
    private initieraKundService: InitieraKundService
  ) {}

  samrad: Projekt;

  ngOnInit(): void {
    this.route.params
      .pipe(
        mergeMap((params) =>
          this.samradService.getSamradById$(
            this.initieraKundService.kundInfo.id,
            params.samradId
          )
        )
      )
      .subscribe((samrad) => (this.samrad = samrad));
  }
}
