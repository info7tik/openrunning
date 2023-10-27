import { Injectable } from '@angular/core';
import { Observable, Subject, of } from 'rxjs';
import { ApiRequestService } from './api-request.service';
import { FilterOperator } from './type/FilterOperator';
import { Frequency } from './type/Frequency';
import { IRunStats } from './type/IRunStats';
import { IRunWithTimestamp } from './type/IRunWithTimestamp';

@Injectable({
    providedIn: 'root'
})
export class RunService {
    public isRunFilterChanged: Subject<boolean> = new Subject<boolean>();
    private THIRTY_DAYS_IN_SECONDS = 2592000;
    private beginningTimestampStorageKey = "timestampInSeconds";
    private beginningTimestamp: number;
    private frequency: Frequency = Frequency.DAILY;
    private filterOperator: FilterOperator = FilterOperator.NO_OPERATOR;
    private filterDistanceInMeters: number = -1;
    private filterPaceInSeconds: number = -1;

    constructor(private apiRequest: ApiRequestService) {
        let savedTimestamp = localStorage.getItem(this.beginningTimestampStorageKey);
        if (savedTimestamp == null) {
            this.beginningTimestamp = Math.floor(new Date().getTime() / 1000) - this.THIRTY_DAYS_IN_SECONDS;
        } else {
            this.beginningTimestamp = parseInt(savedTimestamp);
        }
    }

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
    setRunFilter(timestampInSeconds: number, frequency: Frequency, operator: FilterOperator,
        distanceInMeters: number, paceInSeconds: number) {
        localStorage.setItem(this.beginningTimestampStorageKey, timestampInSeconds.toString());
        this.beginningTimestamp = timestampInSeconds;
        this.frequency = frequency;
        this.filterOperator = operator;
        this.filterDistanceInMeters = distanceInMeters;
        this.filterPaceInSeconds = paceInSeconds;
        this.isRunFilterChanged.next(true);
    }

    getRuns(): Observable<IRunWithTimestamp[]> {
        return this.apiRequest.get<IRunWithTimestamp[]>("run/last/" + this.frequency + "/" + this.beginningTimestamp);
    }

    getRunById(runId: string): Observable<IRunStats> {
        let foundRun = this.RUN_STATS.filter(run => run.id == runId);
        if (foundRun.length == 0) {
            foundRun = this.RUN_STATS;
        }
        return of(foundRun[0]);
    }
}
