import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Observable, of, BehaviorSubject } from "rxjs";
import { catchError, last, tap } from "rxjs/operators";
import { KeycloakService, UserList, UserDetails, CreateUsersReport, CreateUserStatus } from "../../../../../generated/admin-api";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

export class FileUploadModel {
  data: File;
}
@Component({
  selector: "adm-manage-users",
  templateUrl: "./manage-users.component.html",
  styleUrls: ["./manage-users.component.scss"],
})
export class AdmManageUsersComponent implements OnInit, AfterViewInit {
  dataSource: UserDetails[];
  displayedColumns: string[] = ["username", "email", "firstname", "lastname", "roles"];
  validationReport: BehaviorSubject<CreateUsersReport> = new BehaviorSubject(null);
  uploadError: BehaviorSubject<string> = new BehaviorSubject(null);
  realm: string;

  /** Tillåtna filtyper XLS, XLSX */
  accept = "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  private files: Array<FileUploadModel> = [];
  totalElements: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;


  constructor(private route: ActivatedRoute, private apiService: KeycloakService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.realm = params.get("realm");
    });
  }

  ngAfterViewInit() {
    this.updateUserList(this.paginator.pageIndex, this.paginator.pageSize);
  }

  /**
   * Kolla om fil har laddats upp
   */
  isFileUploaded(): boolean {
    return this.validationReport.getValue() ? true : false;
  }

  /**
   * Hämta valideringsrapport
   */
  getValidationReport$(): Observable<CreateUsersReport> {
    return this.validationReport.asObservable();
  }

  /**
   * Kolla om alla användaren har status SUCCCESS
   */
  isValidationSuccess(report: CreateUsersReport): boolean {
    return report.statuses != null && report.statuses.filter(statuses => statuses.status === "SUCCESS").length === report.statuses.length;
  }

  /**
   * Hämta användare med status FAIL
   */
  getFailedStatuses(report: CreateUsersReport): CreateUserStatus[] {
    if (report.statuses != null) {
      return report.statuses.filter(statuses => statuses.status === "FAIL");
    } else {
      return [];
    }
  }

  /**
   * Hämta fel vid uppladdning av fil
   */
  getUploadError$(): Observable<string> {
    return this.uploadError.asObservable();
  }

  /**
   * Ladda upp fil för validering vid klick på knapp
   */
  uploadFileOnClick() {
    const fileUpload = document.getElementById("fileUpload") as HTMLInputElement;
    fileUpload.onchange = () => {
      for (let index = 0; index < fileUpload.files.length; index++) {
        const file = fileUpload.files[index];
        this.files.push({
          data: file
        });
      }
      this.uploadFile(this.files[0], true);
    };
    fileUpload.click();
  }

  /**
   * Bekräfta import av användare
   */
  confirmImportUsers() {
    this.uploadFile(this.files[0], false);
  }

  /**
   * Avbryt uppladdning av fil
   */
  exitUploadFile() {
    this.removeFileFromArray(this.files[0]);
    this.validationReport.next(null);
    this.uploadError.next(null);
  }

  /**
   * Ladda upp fil till backend för validering eller import
   * @param file Fil att ladda upp
   * @param dryRun Om true körs en dry-run: inga användare importeras och en valideringsrapport skapas.
   * Om false importeras användarna till Keycloak.
   */
  private uploadFile(file: FileUploadModel, dryRun: boolean = true) {
    const request$: Observable<any> = this.apiService.keycloakRealmsRealmUsersPost(
      this.realm, dryRun, undefined, undefined, file.data, "response", true
    );

    request$.pipe(
      tap(message => { }),
      last(),
      catchError((error: any) => {
        return of("Ett fel uppstod vid uppladning av fil. Var god försök på nytt.");
      })
    ).subscribe(
      (response: any) => {
        if (typeof (response) === "object") {
          if (response.body.statuses == null) {
            this.uploadError.next("Kunde inte hitta användare att importera.");
          } else if (dryRun) {
            this.validationReport.next(response.body);
          } else {
            this.exitUploadFile();
            this.updateUserList(this.paginator.pageIndex, this.paginator.pageSize);
          }
        } else {
            this.uploadError.next(response);
        }
      },
      (error) => {

        this.uploadError.next(error);
      }
    );
  }

  /**
   * Ta bort fil
   * @param file Ta bort fil
   */
  private removeFileFromArray(file: FileUploadModel) {
    const index = this.files.indexOf(file);
    if (index > -1) {
      this.files.splice(index, 1);
    }
  }

  /**
   * Hämta en lista med användaren för vald realm
   */
  private updateUserList(pageIndex, pageSize) {
    let first = pageIndex*pageSize;

    let userList$: Observable<UserList> = this.apiService.keycloakRealmsRealmUsersGet(this.realm, first, pageSize);

    userList$.subscribe((response) => {
      this.dataSource = response.users;
      this.totalElements = response.totalElements;
    });
  }

  changePage(pageEvent: PageEvent) {
    this.updateUserList(this.paginator.pageIndex, this.paginator.pageSize);
  }
}
