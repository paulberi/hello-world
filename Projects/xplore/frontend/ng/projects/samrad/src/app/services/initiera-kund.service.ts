import { Injectable } from "@angular/core";
import { KundRsp as Kund } from "../../../../../generated/samrad-api";
import { SamradPublicService } from "./samrad-public.service";

@Injectable({
  providedIn: "root",
})
export class InitieraKundService {
  constructor(private samradPublicService: SamradPublicService) {}

  kundSlug: string;
  kundInfo: Kund;

  initKund() {
    const host = window.location.hostname;
    this.kundSlug = host.split(".")[0];

    return this.samradPublicService
      .getKundBySlug$(this.kundSlug)
      .toPromise()
      .then(async (kund) => {
        sessionStorage.setItem("kund", JSON.stringify(kund));
        this.kundInfo = JSON.parse(sessionStorage.getItem("kund"));
      });
  }
}