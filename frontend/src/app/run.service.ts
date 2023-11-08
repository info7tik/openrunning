import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
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

    getRunSamples(runTimestamp: number): Observable<IRunStats> {
        return this.apiRequest.get<IRunStats>("run/sample/" + runTimestamp);
    }
}
