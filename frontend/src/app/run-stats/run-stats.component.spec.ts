import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RunStatsComponent } from './run-stats.component';

describe('RunStatsComponent', () => {
  let component: RunStatsComponent;
  let fixture: ComponentFixture<RunStatsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RunStatsComponent]
    });
    fixture = TestBed.createComponent(RunStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
