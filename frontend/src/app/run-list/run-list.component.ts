import { Component } from '@angular/core';
import { RunService } from '../run.service';
import { Run } from './Run';

@Component({
    selector: 'app-run-list',
    templateUrl: './run-list.component.html',
    styleUrls: ['./run-list.component.css']
})
export class RunListComponent {
    public runs: Run[] = [];
    public showStartIndex: number = 0;
    public showEndIndex: number = 5;
    public previousRunNb: number = 0;
    public nextRunNb: number = 0;
    public selectedRunId: number = 0;

    constructor(private runService: RunService) { }

    ngOnInit(): void {
        this.runService.isRunFilterChanged.subscribe((isChanged) => this.getRuns());
        this.getRuns();
    }

    updatePreviousRunNb(): void {
        this.previousRunNb = this.showStartIndex;
    }

    updateNextRunNb(): void {
        if (this.runs.length > this.showEndIndex) {
            this.nextRunNb = this.runs.length - this.showEndIndex;
        } else {
            this.nextRunNb = 0;
        }
    }

    updateRunNbCounters(): void {
        this.updatePreviousRunNb();
        this.updateNextRunNb();
    }

    getRuns(): void {
        this.runService.getRuns().subscribe(receivedRuns => {
            this.runs = [];
            receivedRuns.forEach(receivedRun => this.runs.push(new Run(receivedRun)));
            this.updateNextRunNb();
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

    onClickSelectRunId(runId: number, runDate: string) {
        this.runService.setSelectedRunInfo({ identifier: runId, date: runDate });
        this.selectedRunId = runId;
    }
}
