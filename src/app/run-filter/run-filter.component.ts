import { Component } from '@angular/core';

@Component({
    selector: 'app-run-filter',
    templateUrl: './run-filter.component.html',
    styleUrls: ['./run-filter.component.css']
})
export class RunFilterComponent {
    private _propertyTagId = "property-tag";
    operator: string = "no_filter";
    property: string = "--";

    filterOperators = [
        {
            name: "Almost",
            value: "almost"
        },
        {
            name: "Greater than",
            value: "greater_than"

        },
        {
            name: "Less than",
            value: "less_than"
        }
    ];

    filterProperties = [
        {
            name: "Distance",
            value: "distance"
        },
        {
            name: "Pace",
            value: "pace"
        }
    ]

    updatePropertyTag(): void {
        if (this.operator === "no_filter") {
            this.property = "--";
            (<HTMLSelectElement>document.getElementById(this._propertyTagId)).disabled = true;
        } else {
            (<HTMLSelectElement>document.getElementById(this._propertyTagId)).disabled = false;
        }
    }
}
