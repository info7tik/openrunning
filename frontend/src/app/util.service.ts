import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UtilService {

    constructor() { }

    public formatDistance(distanceInMeters: number): string {
        return (distanceInMeters / 1000).toFixed(2) + " km";
    }

    public formatTime(timeInSeconds: number): string {
        return this.timeToString(timeInSeconds);
    }

    public formatPace(paceInSeconds: number): string {
        return this.timeToString(paceInSeconds);
    }

    private timeToString(timeInSeconds: number): string {
        let minutes = Math.floor(timeInSeconds / 60);
        let seconds = Math.floor(timeInSeconds % 60);
        let hours = Math.floor(minutes / 60);
        minutes = Math.floor(minutes % 60);
        if (hours > 0) {
            return hours + "h" +
                this.displayWithTwoDigits(minutes) + "min" +
                this.displayWithTwoDigits(seconds) + "s";
        } else {
            return minutes + "min" +
                this.displayWithTwoDigits(seconds) + "s";
        }
    }

    private displayWithTwoDigits(number: number) {
        return number.toString().padStart(2, "0");
    }
}
