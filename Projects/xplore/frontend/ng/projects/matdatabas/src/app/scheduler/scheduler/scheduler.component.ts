import { Component, OnInit } from "@angular/core";
import {SchedulerJob, SchedulerService} from "../../services/scheduler.service";

@Component({
  selector: "mdb-scheduler",
  template: `
    <div *ngIf="schedulerJobs">
      <table style="border: 1px solid black;">
        <thead>
        <tr>
          <th>Id</th>
          <th>jobName</th>
          <th>jobGroup</th>
          <th>cronExpression</th>
          <th>latestOk</th>
          <th>latestRun</th>
          <th>latestStatus</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr *ngFor="let job of schedulerJobs">
          <td>{{job.id}}</td>
          <td>{{job.jobName}}</td>
          <td>{{job.jobGroup}}</td>
          <td>{{job.cronExpression}}</td>
          <td>{{job.latestOk}}</td>
          <td>{{job.latestRun}}</td>
          <td>{{job.latestStatus}}</td>
          <td><button type="button" (click)="startJob(job)">Start</button></td>
        </tr>
        </tbody>

      </table>
    </div>
  `,
  styles: [
    `
      table {
        border-collapse: collapse;
      }

      table, th, td {
        border: 1px solid black;
      }
    `
  ]
})
export class SchedulerComponent implements OnInit {
  public schedulerJobs: SchedulerJob[];

  constructor(private schedulerService: SchedulerService) { }

  ngOnInit() {
    this.schedulerService.getSchedulerJobs().subscribe((jobs) => {
      this.schedulerJobs = jobs;
    });
  }

  startJob(job: SchedulerJob) {
    this.schedulerService.startJob(job.id).subscribe((response) => null);
  }
}
