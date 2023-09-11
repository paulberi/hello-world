import { Component, ViewChild, AfterViewInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSnackBar } from "@angular/material/snack-bar";
import { MatTableDataSource } from "@angular/material/table";
import { ProjektService } from "../../services/projekt.service";
import { ConfirmationDialogComponent } from "../confirmation-dialog/confirmation-dialog.component";
import { saveAs } from "file-saver";
import { AvtalService } from "../../services/avtal.service";
import { finalize } from "rxjs/operators";



@Component({
  selector: "mh-projektlista",
  templateUrl: "./projektlista.component.html",
  styleUrls: ["./projektlista.component.scss"]
})
export class ProjektlistaComponent implements AfterViewInit {
  displayedColumns: string[] = ["projektnr", "created_at", "status", "action"];
  dataSource = new MatTableDataSource<Projekt>();
  totalElements = 0;


  @ViewChild(MatPaginator) paginator: MatPaginator;
  constructor(
    private projektService: ProjektService,
    private avtalService: AvtalService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
    ) {
      this.getPage(0, 10);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  public getPage(pageIndex, pageSize) {
    this.projektService.getProjects(pageIndex, pageSize).subscribe((projekt: Page<Projekt>) => {
      this.totalElements = projekt.totalElements;
      this.dataSource = new MatTableDataSource<Projekt>(projekt.content);
    },
    error => {
      this.errorNotification(error.status);
    });
  }

  errorNotification(status) {
    let message = "Något gick snett";
    switch (status) {
      case 504:
        message = "Lyckades inte nå backendtjänsten";
        break;
      case 500:
        message = "Något gick fel i backendtjänsten";
        break;
      case 401:
          message = "Din session har gått ut, ladda om sidan";
          break;
      default:
        message = "Ett oväntat fel inträffade";
        break;
    }
    this.snackBar.open(message, "X", 
    {
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: ["mat-orangewarn", "errorSnackbar"],
    });
  }

  restartJob(id) {
    this.projektService.updateProjektStatus(id, "skapat").subscribe(() => {
      this.getPage(this.paginator.pageIndex, this.paginator.pageSize);
      this.projektService.submitJob(id).pipe(
        finalize(() => {
          this.getPage(0, this.paginator.pageSize);
        })
      ).subscribe();
    });
  }
  
  openDownloadDialog(projekt) {
    this.dialog.open(ConfirmationDialogComponent, {data: {
      title: "Ladda ner avtal",
      body: "Är du säker på att du vill ladda ner avtalet?",
      button: "Ladda ner",
      color: "accent"
      },
      autoFocus: false
    }).afterClosed().subscribe(result => {
      if (result) {
          this.snackBar.open("Nedladdningen påbörjas snart...", "Ok");
        this.hamtaAvtal(projekt);
      }
    });
  }
  
  hamtaAvtal(projekt) {
    this.avtalService.getAvtalURL(projekt.avtalsId).subscribe(res => {
      window.open(res);
      this.snackBar.dismiss();
    },
    err => {
      let message;
        switch (err.status) {
          case 401:
            message = "Din session har gått ut, ladda om sidan";
            break;
          case 404:
            message = "Avtalet hittades inte, vänligen försök igen om en stund";
            break;
          default:
            message = "Nedladdningen misslyckades, vänligen försök igen om en stund";
            break;
        }
      this.snackBar.open(message, "Ok", {
        duration: 5000,
        panelClass: ["mat-orangewarn"]
      });
    });
  }

  deleteProjekt(projekt) {
    return this.projektService.deleteProjekt(projekt.id);
  }
  refresh() {
    this.getPage(this.paginator.pageIndex, this.paginator.pageSize);
  }
  
  openConfirmationDialog(projekt) {
    
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {data: {
      title: "Ta bort projekt",
      body: "Är du säker på att du vill ta bort projektet " + projekt.projektnr + "?",
      button: "Ta bort",
      color: "orangewarn",
      await: true
      },
      disableClose: true,
      autoFocus: false
    });
    const sub = dialogRef.componentInstance.delete.subscribe(() => {
      this.deleteProjekt(projekt).subscribe(() => {
        this.getPage(this.paginator.pageIndex, this.paginator.pageSize);
        this.dialog.closeAll();
        },
        err => {
          let message;
          switch (err.status) {
            case 401:
              message = "Din session har gått ut, ladda om sidan";
              break;
            default:
              message = "Borttagningen misslyckades";
              break;
          }
          this.snackBar.open(message, "Ok", {
            duration: 5000,
            panelClass: ["mat-orangewarn"]
          });
        this.dialog.closeAll();
        }
      );    
    });
  }

}

export interface Projekt {
  id: string;
  projektnr: number;
  created_at: number;
  status: string;
}

export interface Page<T> {
  content: T[];
  numberOfElements: number;
  totalElements: number;
  totalPages: number;
}
