import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { IRun } from './common/run/IRun';
import { IRunStats } from './common/stats/IRunStats';

@Injectable({
  providedIn: 'root'
})
export class RunService {

  constructor() { }
  RUN_STATS = [
    {
      "id": "run1",
      "date": ["11:32", "11:33", "11:35", "11:36", "11:37"],
      "distanceUnit": "m",
      "distance": [100, 300, 700, 400, 600],
      "paceInSeconds": [100, 400, 500, 200, 300]
    },
    {
      "id": "run2",
      "date": ["11:32", "11:33", "11:35", "11:36", "11:37"],
      "distanceUnit": "m",
      "distance": [300, 600, 100, 200, 300],
      "paceInSeconds": [100, 200, 300, 500, 300]
    },
    {
      "id": "run3",
      "date": ["11:32", "11:33", "11:35", "11:36", "11:37"],
      "distanceUnit": "m",
      "distance": [200, 600, 800, 200, 300],
      "paceInSeconds": [100, 400, 500, 200, 300]
    },
    {
      "id": "run4",
      "date": ["11:32", "11:33", "11:35", "11:36", "11:37"],
      "distanceUnit": "m",
      "distance": [100, 300, 700, 400, 600],
      "paceInSeconds": [200, 250, 300, 400, 250]
    }
  ]

  RUNS: IRun[] = [
    { "id": "run1", "date": "21/06/2023", "distanceMeters": 10000, "timeSeconds": 31000, "paceSeconds": 300 },
    { "id": "run2", "date": "22/06/2023", "distanceMeters": 11000, "timeSeconds": 32000, "paceSeconds": 300 },
    { "id": "run3", "date": "23/06/2023", "distanceMeters": 12000, "timeSeconds": 33000, "paceSeconds": 300 },
    { "id": "run4", "date": "24/06/2023", "distanceMeters": 13000, "timeSeconds": 34000, "paceSeconds": 300 },
    { "id": "run5", "date": "25/06/2023", "distanceMeters": 14000, "timeSeconds": 35000, "paceSeconds": 300 },
    { "id": "run6", "date": "26/06/2023", "distanceMeters": 15000, "timeSeconds": 36000, "paceSeconds": 300 },
    { "id": "run7", "date": "27/06/2023", "distanceMeters": 16000, "timeSeconds": 37000, "paceSeconds": 300 },
    { "id": "run8", "date": "28/06/2023", "distanceMeters": 17000, "timeSeconds": 38000, "paceSeconds": 300 },
    { "id": "run9", "date": "29/06/2023", "distanceMeters": 18000, "timeSeconds": 39000, "paceSeconds": 300 },
    { "id": "run10", "date": "30/06/2023", "distanceMeters": 19000, "timeSeconds": 40000, "paceSeconds": 300 },
    { "id": "run11", "date": "31/06/2023", "distanceMeters": 20000, "timeSeconds": 41000, "paceSeconds": 300 },
    { "id": "run12", "date": "32/06/2023", "distanceMeters": 21000, "timeSeconds": 42000, "paceSeconds": 300 },
    { "id": "run13", "date": "33/06/2023", "distanceMeters": 22000, "timeSeconds": 43000, "paceSeconds": 300 }
  ]

  getRuns(): Observable<IRun[]> {
    return of(this.RUNS);
  }

  getRunById(runId: string): Observable<IRunStats> {
    let foundRun = this.RUN_STATS.filter(run => run.id == runId);
    if(foundRun.length == 0) {
      foundRun = this.RUN_STATS;
    }
    return of(foundRun[0]);
  }
}
