import { Component } from '@angular/core';
import { RunService } from '../run.service';
import { FilterOperator } from '../type/FilterOperator';
import { filterProperty as FilterProperty } from '../type/FilterProperty';
import { Frequency } from '../type/Frequency';

@Component({
    selector: 'app-run-filter',
    templateUrl: './run-filter.component.html',
    styleUrls: ['./run-filter.component.css']
})
export class RunFilterComponent {
    private propertyTagId = "property-tag";

    public beginningDate: string = new Date().toISOString().split('T')[0];

    public frequencyTypes = Object.values(Frequency).filter(value => value !== Frequency.DAILY);
    public defaultFrequency: Frequency = Frequency.DAILY;
    public frequency: Frequency = this.defaultFrequency;

    public operatorTypes = Object.values(FilterOperator).filter(value => value !== FilterOperator.NO_OPERATOR);
    public defaultOperator: FilterOperator = FilterOperator.NO_OPERATOR;
    public operator: FilterOperator = this.defaultOperator;

    public propertyTypes = Object.values(FilterProperty).filter(value => value !== FilterProperty.NO_PROPERTY);
    public defaultProperty: string = FilterProperty.NO_PROPERTY;
    public property: string = this.defaultProperty;

    public distancePropertyValue = FilterProperty.DISTANCE;
    public distanceInMeters = 10000;

    public pacePropertyValue = FilterProperty.PACE;
    public paceMinutes = 6;
    public paceSeconds = 30;

    constructor(private runService: RunService) { }

    updatePropertyTag(): void {
        if (this.operator === FilterOperator.NO_OPERATOR) {
            this.property = FilterProperty.NO_PROPERTY;
            (<HTMLSelectElement>document.getElementById(this.propertyTagId)).disabled = true;
        } else {
            (<HTMLSelectElement>document.getElementById(this.propertyTagId)).disabled = false;
        }
    }

    showMyRuns(): void {
        let dayBeginning = new Date(this.beginningDate);
        dayBeginning.setHours(0, 0, 0, 0);
        let timestampInSeconds = Math.floor(dayBeginning.getTime() / 1000);
        let totalPace = this.paceMinutes * 60 + this.paceSeconds;
        this.runService.setRunFilter(timestampInSeconds, this.frequency, this.operator, this.distanceInMeters, totalPace);
    }
}
