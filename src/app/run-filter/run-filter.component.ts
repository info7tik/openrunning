import { Component } from '@angular/core';

@Component({
  selector: 'app-run-filter',
  templateUrl: './run-filter.component.html',
  styleUrls: ['./run-filter.component.css']
})
export class RunFilterComponent {
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
      value:"distance"
    },
    {
      name: "Pace",
      value:"pace"
    }
  ]
}
