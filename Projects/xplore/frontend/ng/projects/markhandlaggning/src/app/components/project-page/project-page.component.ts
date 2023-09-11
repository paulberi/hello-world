import { Component, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { MatButtonToggleGroup } from "@angular/material/button-toggle";
import { ProjektService } from "../../services/projekt.service";
import { finalize } from "rxjs/operators";
import { ProjektlistaComponent } from "../projektlista/projektlista.component";

@Component({
  selector: "mh-project-page",
  templateUrl: "./project-page.component.html",
  styleUrls: ["./project-page.component.scss"]
})
export class ProjectPageComponent implements OnInit {
  fileForm: any;
  projects: any;
  projektNr: string;
  avtalstyp: string;

  files: any[] = [];

  @ViewChild(ProjektlistaComponent, {static: true}) projektlista;


  constructor(
    private formBuilder: UntypedFormBuilder,
    private projektService: ProjektService
  ) {
    this.fileForm = this.formBuilder.group({
      projektNr: this.projektNr,
      avtalstyp: this.avtalstyp,
      files: this.files,
    });
  }

  ngOnInit() {
    this.avtalstyp = "el";
  }

  onSubmit(form: UntypedFormGroup): void {
    form.value.files = this.files;
    form.value.avtalstyp = this.avtalstyp;
    if (this.files.length > 1 && form.value.projektNr != null) {
      this.projektService.createProjekt(form.value)
      .subscribe((data: any) => {
        this.projektlista.getPage(0, 10);
        this.projektService.submitJob(data.id).pipe(
          finalize(() => {
            this.projektlista.getPage(0, this.projektlista.paginator.pageSize);
          })
        ).subscribe(() => {});
      },
      error => {
        this.projektlista.errorNotification(error.status);
      });
      this.clearFiles();
    }
  }
  avtalstypChange(value) {
    this.avtalstyp = value;
  }

  /**
   * on file drop handler
   */
  onFileDropped(event) {
    this.prepareFilesList(event);
  }

  clearFiles() {
    this.fileForm.reset();
    this.files = [];
    this.avtalstyp = "el";
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(file) {
    this.prepareFilesList(file);
  }

  /**
   * Convert Files list to normal array list
   * @param files (Files List)
   */
  prepareFilesList(files: Array<any>) {
    this.files = [];

    for (const item of files) {
      item.progress = 0;
      this.files.push(item);
    }
  }
}
