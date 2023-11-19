import { RecordData } from "../type/RecordData";
import { UtilService } from "../util.service";

export class Record {
    timestamp: number;
    distance: string;
    time: string;
    firstSampleIndex: number;
    lastSampleIndex: number;

    constructor(record: RecordData, tools: UtilService) {
        this.timestamp = record.timestamp;
        this.distance = record.distance + " m";
        this.time = tools.formatTime(record.time);
        this.firstSampleIndex = record.firstSampleIndex;
        this.lastSampleIndex = record.lastSampleIndex;
    }
}