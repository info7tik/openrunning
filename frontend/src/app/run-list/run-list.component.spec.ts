import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RunListComponent } from './run-list.component';

describe('RunListComponent', () => {
  let component: RunListComponent;
  let fixture: ComponentFixture<RunListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RunListComponent]
    });
    fixture = TestBed.createComponent(RunListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
