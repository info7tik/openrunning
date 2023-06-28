import { IRun } from "./IRun";

export class Run implements IRun {
    id: string;
    date: string;
    distanceMeters: number;
    timeSeconds: number;
    paceSeconds: number;

    constructor(runData: IRun) {
        this.id = runData.id;
        this.date = runData.date;
        this.distanceMeters = runData.distanceMeters;
        this.timeSeconds = runData.timeSeconds;
        this.paceSeconds = runData.paceSeconds;
    }

    formatDistance(): string {
        return (this.distanceMeters / 1000).toFixed(2);
    }

    formatTime(): string {
        let minutes = Math.floor(this.timeSeconds / 60);
        let seconds = Math.floor(this.timeSeconds % 60);
        return minutes + "min" + seconds + "s"
    }

    formatPace(): string {
        let minutes = Math.floor(this.timeSeconds / 60);
        let seconds = Math.floor(this.timeSeconds % 60);
        return minutes + ":" + seconds
    }
}