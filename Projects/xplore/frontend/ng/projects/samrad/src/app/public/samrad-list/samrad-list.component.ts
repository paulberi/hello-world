import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { map } from "rxjs/operators";
import { ProjektRsp as Projekt, PubliceringStatus } from "../../../../../../generated/samrad-api";
import { InitieraKundService } from "../../services/initiera-kund.service";
import { SamradPublicService } from "../../services/samrad-public.service";

@Component({
  selector: "sr-samrad-list",
  templateUrl: "./samrad-list.component.html",
  styleUrls: ["./samrad-list.component.scss"],
})
export class SamradListComponent implements OnInit {
  constructor(
    private router: Router,
    private samradPublicService: SamradPublicService,
    private initieraKundService: InitieraKundService
  ) {}

  aktuellaSamradList: Projekt[];
  arkiveradeSamradList: Projekt[];
  title: string;

  ngOnInit(): void {
    this.title = this.initieraKundService.kundInfo.namn;
    this.samradPublicService
      .getSamradList$(this.initieraKundService.kundInfo.id)
      .pipe(
        map((samrad) =>
          samrad.filter((samrad) => samrad.publiceringStatus === PubliceringStatus.PUBLICERAD)
        )
      )
      .subscribe((samrad) => (this.aktuellaSamradList = samrad));

      this.samradPublicService
      .getSamradList$(this.initieraKundService.kundInfo.id)
      .pipe(
        map((samrad) =>
          samrad.filter((samrad) => samrad.publiceringStatus === PubliceringStatus.ARKIVERAD)
        )
      )
      .subscribe((samrad) => (this.arkiveradeSamradList = samrad));
  }

  navigateToSamrad(slug: string) {
    this.router.navigate([slug]);
  }
}
