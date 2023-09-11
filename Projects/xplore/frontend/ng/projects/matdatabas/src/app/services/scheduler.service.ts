import { Injectable } from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";

export interface SchedulerJob {
  id: number;
  jobName: string;
  jobGroup: string;
  cronExpression: string;
  latestOk: string;
  latestRun: string;
  latestStatus: number;
}

@Injectable({
  providedIn: "root"
})
export class SchedulerService {

  constructor(private http: HttpClient) { }

  getSchedulerJobs() {
    return this.http.get<SchedulerJob[]>("/api/scheduler/jobs", {});
  }

  startJob(id: number) {
    let params = new HttpParams();

    params = params.append("id", id.toString());

    return this.http.post("/api/scheduler/startJob", null, {params: params});
  }
}
