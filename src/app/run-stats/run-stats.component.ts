import { Component, Input } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { RunService } from '../run.service';
import { IRunStats } from '../stats/IRunStats';
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
            this.displayRunStats();
        } else {
            this._runId = "";
        }
    }

    @Input() runDate: string = "";

    constructor(private runService: RunService) {
        Chart.register(...registerables);
    }

    displayRunStats(): void {
        if (this._runId.length > 0) {
            let chartResource = this.runService.getRunById(this._runId);
            chartResource.subscribe(chartData => {
                this.buildDistanceChart(chartData);
                this.buildPaceChart(chartData);
            })
        }
    }

    buildDistanceChart(chartData: IRunStats): void {
        let chartId = "distance-chart";
        this.destroyExistingChart(chartId);
        new Chart(chartId, {
            type: 'bar',
            data: {
                labels: chartData.date,
                datasets: [{
                    label: 'Distance (' + chartData.distanceUnit + ")",
                    data: chartData.distance,
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

    buildPaceChart(chartData: IRunStats) {
        let chartId = "pace-chart";
        this.destroyExistingChart(chartId);
        new Chart(chartId, {
            type: 'bar',
            data: {
                labels: chartData.date,
                datasets: [{
                    label: 'Pace (per km)',
                    data: chartData.paceInSeconds,
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
                                    "pace: " + this.formatPaceInSeconds(context.raw)
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

    formatPaceInSeconds(pace: any): string {
        let minutes = Math.floor(pace / 60);
        let seconds = pace % 60;
        return minutes + "min" + this.always2Digits(seconds) + "s";
    }

    always2Digits(digit: number): string {
        return ("0" + digit).slice(-2);
    }
}
