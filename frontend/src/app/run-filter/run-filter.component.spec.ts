import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RunFilterComponent } from './run-filter.component';

describe('RunFilterComponent', () => {
  let component: RunFilterComponent;
  let fixture: ComponentFixture<RunFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RunFilterComponent]
    });
    fixture = TestBed.createComponent(RunFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
