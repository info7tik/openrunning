import { Component, Input } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { RunService } from '../run.service';
@Component({
    selector: 'app-run-stats',
    templateUrl: './run-stats.component.html',
    styleUrls: ['./run-stats.component.css']
})

export class RunStatsComponent {
    private _runId: string = "";

    @Input()
    get runId(): string {
        return this._runId;
    }

    set runId(id: string) {
        let idTrimmed = id.trim();
        if (idTrimmed.length > 0) {
            this._runId = idTrimmed;
        } else {
            this._runId = "";
        }
        this.buildCharts();
    }

    @Input() runDate: string = "";

    constructor(private runService: RunService) {
        Chart.register(...registerables);
    }

    buildCharts(){
        this.runService.getRunSamples(parseInt(this._runId)).subscribe(
            (runSamples) => {
                let aggregatedDistances: number[] = [];
                let sum = 0;
                runSamples.distances.forEach(distance => {
                    sum += distance;
                    aggregatedDistances.push(sum);
                });
                let startTime = runSamples.timestamps[0];
                let relativeTimes: string[] = [];
                runSamples.timestamps.forEach(time => {
                    relativeTimes.push(this.formatTime(time - startTime));
                });
                this.buildDistanceChart(relativeTimes, aggregatedDistances, runSamples.distanceUnit, runSamples.paces);
                this.buildPaceChart(relativeTimes, runSamples.paces, aggregatedDistances, runSamples.distanceUnit);
            });
    }

    buildDistanceChart(times: string[], distances: number[], distanceUnit: string, paces: number[]): void {
        let chartId = "distance-chart";
        this.destroyExistingChart(chartId);
        new Chart(chartId, {
            type: 'bar',
            data: {
                labels: times,
                datasets: [{
                    label: 'Distance (' + distanceUnit + ")",
                    data: distances,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    buildPaceChart(times: string[], paces: number[], distances: number[], distanceUnit: string) {
        let chartId = "pace-chart";
        this.destroyExistingChart(chartId);
        new Chart(chartId, {
            type: 'bar',
            data: {
                labels: times,
                datasets: [{
                    label: 'Pace (per km)',
                    data: paces,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1,
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: (context) => {
                                let result = [
                                    "pace: " + this.formatTime(context.raw),
                                    "distance: " + distances[context.dataIndex] + " " + distanceUnit.toLocaleLowerCase()
                                ];
                                return result;
                            }
                        }
                    }
                }
            }
        });
    }

    destroyExistingChart(chartId: string) {
        let existingChart = Chart.getChart(chartId);
        if (existingChart !== undefined) {
            existingChart.destroy();
        }
    }

    formatTime(timeInSeconds: any): string {
        let hours = Math.floor(timeInSeconds / 3600);
        let remaindedSeconds = timeInSeconds % 3600;
        let minutes = Math.floor(remaindedSeconds / 60);
        let seconds = remaindedSeconds % 60;
        if (hours > 0) {
            return hours + "h" + this.always2Digits(minutes) + "min" + this.always2Digits(seconds) + "s";
        } else if (minutes > 0) {
            return minutes + "min" + this.always2Digits(seconds) + "s";
        } else {
            return seconds + "s";
        }
    }

    always2Digits(digit: number): string {
        return ("0" + digit).slice(-2);
    }
}
