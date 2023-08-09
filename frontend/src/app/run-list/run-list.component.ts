import { Component } from '@angular/core';
import { Run } from '../common/run/Run';
import { RunService } from '../run.service';

@Component({
    selector: 'app-run-list',
    templateUrl: './run-list.component.html',
    styleUrls: ['./run-list.component.css']
})
export class RunListComponent {
    runs: Run[] = [];
    showStartIndex: number = 0;
    showEndIndex: number = 5;
    previousRunNb: number = 0;
    nextRunNb: number = 0;

    selectedRunId: string = "";
    selectedRunDate: string = "";

    constructor(private runService: RunService) { }

    ngOnInit(): void {
        this.getRuns();
        this.updateNextRunNb();
    }

    updatePreviousRunNb() : void {
        this.previousRunNb = this.showStartIndex;
    }

    updateNextRunNb() : void {
        this.nextRunNb = this.runs.length - this.showEndIndex;
    }

    updateRunNbCounters(): void {
        this.updatePreviousRunNb();
        this.updateNextRunNb();
    }

    getRuns(): void {
        this.runService.getRuns().subscribe(receivedRuns => {
            receivedRuns.forEach(receivedRun => this.runs.push(new Run(receivedRun)));
        });
    }

    showPreviousRun(): void {
        if (this.showStartIndex > 0) {

            this.showStartIndex--;
            this.showEndIndex--;
            this.updateRunNbCounters();
        }
    }

    showNextRun(): void {
        if (this.showEndIndex < this.runs.length) {
            this.showStartIndex++;
            this.showEndIndex++;
            this.updateRunNbCounters();
        }
    }

    setSelectedRunId(runId: string, runDate: string) {
        this.selectedRunId = runId;
        this.selectedRunDate = runDate;
    }
}