import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ApiRequestService } from './api-request.service';
import { FilterOperator } from './type/FilterOperator';
import { Frequency } from './type/Frequency';
import { IRunStats } from './type/IRunStats';
import { IRunWithTimestamp } from './type/IRunWithTimestamp';
import { RecordData } from './type/RecordData';
import { RunInformation } from './type/RunInformation';

@Injectable({
    providedIn: 'root'
})
export class RunService {
    private THIRTY_DAYS_IN_SECONDS = 2592000;
    private beginningTimestampStorageKey = "timestampInSeconds";
    private beginningTimestamp: number;
    private frequency: Frequency = Frequency.DAILY;
    private filterOperator: FilterOperator = FilterOperator.NO_OPERATOR;
    private filterDistanceInMeters: number = -1;
    private filterPaceInSeconds: number = -1;
    private selectedRunInfo: RunInformation = { identifier: 0, date: "" };

    public isRunFilterChanged: Subject<boolean> = new Subject<boolean>();
    public isSelectedRunChanged: Subject<boolean> = new Subject<boolean>();

    constructor(private apiRequest: ApiRequestService) {
        let savedTimestamp = localStorage.getItem(this.beginningTimestampStorageKey);
        if (savedTimestamp == null) {
            this.beginningTimestamp = Math.floor(new Date().getTime() / 1000) - this.THIRTY_DAYS_IN_SECONDS;
        } else {
            this.beginningTimestamp = parseInt(savedTimestamp);
        }
    }

    public setRunFilter(timestampInSeconds: number, frequency: Frequency, operator: FilterOperator,
        distanceInMeters: number, paceInSeconds: number) {
        localStorage.setItem(this.beginningTimestampStorageKey, timestampInSeconds.toString());
        this.beginningTimestamp = timestampInSeconds;
        this.frequency = frequency;
        this.filterOperator = operator;
        this.filterDistanceInMeters = distanceInMeters;
        this.filterPaceInSeconds = paceInSeconds;
        this.isRunFilterChanged.next(true);
    }

    public setSelectedRunInfo(runInfo: RunInformation) {
        this.selectedRunInfo = runInfo;
        this.isSelectedRunChanged.next(true);
    }

    public getSelectedRunInfo(): RunInformation {
        return this.selectedRunInfo;
    }

    public getRuns(): Observable<IRunWithTimestamp[]> {
        return this.apiRequest.get<IRunWithTimestamp[]>("run/last/" + this.frequency + "/" + this.beginningTimestamp);
    }

    public getRunSamples(runTimestamp: number): Observable<IRunStats> {
        return this.apiRequest.get<IRunStats>("run/sample/" + runTimestamp);
    }

    public getPersonalRecords(): Observable<RecordData[]> {
        return this.apiRequest.get<RecordData[]>("run/records");
    }

    public getTrackRecords(runId: number): Observable<RecordData[]> {
        return this.apiRequest.get<RecordData[]>("run/records/" + runId);
    }
}
