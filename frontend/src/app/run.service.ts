import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ApiRequestService } from './api-request.service';
import { IRunWithTimestamp } from './common/run/IRunWithTimestamp';
import { IRunStats } from './common/stats/IRunStats';

@Injectable({
    providedIn: 'root'
})
export class RunService {

    constructor(private apiRequest: ApiRequestService) { }

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

    getRuns(): Observable<IRunWithTimestamp[]> {
        return this.apiRequest.get<IRunWithTimestamp[]>("run/last/DAILY/1448795579");
    }

    getRunById(runId: string): Observable<IRunStats> {
        let foundRun = this.RUN_STATS.filter(run => run.id == runId);
        if (foundRun.length == 0) {
            foundRun = this.RUN_STATS;
        }
        return of(foundRun[0]);
    }
}
