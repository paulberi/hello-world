import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { catchError, mergeMap } from "rxjs/operators";
import { InitieraKundService } from "../../services/initiera-kund.service";
import { NavigationService } from "../../services/navigation.service";
import { ProjektRsp as Projekt } from "../../../../../../generated/samrad-api";
import { SamradPublicService } from "../../services/samrad-public.service";
import { of } from "rxjs";

@Component({
  selector: "sr-samrad-sida",
  templateUrl: "./samrad-sida.component.html",
  styleUrls: ["./samrad-sida.component.scss"],
})
export class SamradSidaComponent implements OnInit {
  constructor(
    private samradPublicService: SamradPublicService,
    private route: ActivatedRoute,
    private navigationService: NavigationService,
    private initieraKundService: InitieraKundService,
    private router: Router
  ) {}

  samrad: Projekt;
  projektId: string;

  ngOnInit(): void {
    this.route.params
      .pipe(
        mergeMap((params) => 
          this.samradPublicService.getSamrad$(
            this.initieraKundService.kundInfo.id,
            params.samradId
          )
        ),
        catchError(err => {
          return this.handleError(err);
        })
      )
      .subscribe((samrad) =>  {
        samrad.brodtext = samrad.brodtext.replace("/samrad/api/admin", "/api");
        this.samrad = samrad
        this.projektId = samrad.id;
      });
  }

  navigateBack() {
    this.navigationService.back();
  }

  private handleError = (error: any) => {
    this.router.navigate(['404']);
    return of(error);
 }
}
