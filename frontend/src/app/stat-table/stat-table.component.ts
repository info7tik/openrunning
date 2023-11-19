import { Component, Input } from '@angular/core';
import { RunService } from '../run.service';
import { RecordType } from '../type/RecordType';
import { RunInformation } from '../type/RunInformation';
import { UtilService } from '../util.service';
import { Record } from './Record';

@Component({
    selector: 'app-stat-table',
    templateUrl: './stat-table.component.html',
    styleUrls: ['./stat-table.component.css']
})
export class StatTableComponent {
    @Input() title: string = RecordType.PERSONAL;
    public records: Record[] = [];

    constructor(private runService: RunService, private tools: UtilService) { }

    ngOnInit(): void {
        this.displayRecords();
        this.runService.isSelectedRunChanged.subscribe((isChanged) => this.displayRecords());
    }

    private displayRecords() {
        switch (this.title) {
            case RecordType.PERSONAL:
                this.loadPersonalRecords();
                break;
            case RecordType.TRACK:
                this.loadTrackRecords();
                break;
        }
    }

    private loadPersonalRecords() {
        this.runService.getPersonalRecords().subscribe(recordData => {
            this.records = [];
            recordData.forEach(record => this.records.push(new Record(record, this.tools)));
        });
    }

    private loadTrackRecords() {
        let selectedRunInfo: RunInformation = this.runService.getSelectedRunInfo();
        this.runService.getTrackRecords(selectedRunInfo.identifier).subscribe(recordData => {
            this.records = [];
            recordData.forEach(record => this.records.push(new Record(record, this.tools)));
        });
    }
}
