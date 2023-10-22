import { IRunWithDate } from "./IRunWithDate";
import { IRunWithTimestamp } from "./IRunWithTimestamp";

export class Run implements IRunWithDate {
    id: number;
    date: string;
    distanceMeters: number;
    timeSeconds: number;
    paceSeconds: number;

    constructor(runData: IRunWithTimestamp) {
        this.id = runData.timestampInSeconds;
        console.log(runData);
        let date = new Date(runData.timestampInSeconds * 1000);
        this.date = date.toLocaleDateString();
        this.distanceMeters = runData.distanceMeters;
        this.timeSeconds = runData.timeSeconds;
        this.paceSeconds = runData.paceSeconds;
    }

    formatDistance(): string {
        return (this.distanceMeters / 1000).toFixed(2) + " km";
    }

    formatTime(): string {
        return this.timeToString(this.timeSeconds);
    }

    formatPace(): string {
        return this.timeToString(this.paceSeconds);
    }

    private timeToString(timeInSeconds: number): string {
        let minutes = Math.floor(timeInSeconds / 60);
        let seconds = Math.floor(timeInSeconds % 60);
        let hours = Math.floor(minutes / 60);
        minutes = Math.floor(minutes % 60);
        if (hours > 0) {
            return this.displayWithTwoDigits(hours) + "h" +
                this.displayWithTwoDigits(minutes) + "min" +
                this.displayWithTwoDigits(seconds) + "s";
        } else {
            return this.displayWithTwoDigits(minutes) + "min" +
                this.displayWithTwoDigits(seconds) + "s";
        }
    }

    private displayWithTwoDigits(number: number) {
        return number.toString().padStart(2, "0");
    }

}