import { Component, OnInit } from "@angular/core";
import { DialogService } from "../../services/dialog.service";
import { UpToDateBuildService } from "../../services/up-to-date-build.service";

@Component({
  selector: 'mk-app-up-to-date',
  template: ''
})
export class MkUpToDateComponent implements OnInit {
    private dialogOpened = false;

    constructor(private dialogService: DialogService, private upToDateService: UpToDateBuildService) {
    }

    ngOnInit() {
      this.upToDateService.buildIsUpToDate.subscribe(buildIsUpToDate => {
        if (!buildIsUpToDate && !this.dialogOpened) {
          this.dialogOpened = true;
          this.dialogService.appNotUpToDateDialog().subscribe();
        }
      });
    }
}
