import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RunFilterComponent } from './run-filter/run-filter.component';
import { FormsModule } from '@angular/forms';
import { RunListComponent } from './run-list/run-list.component';
import { RunStatsComponent } from './run-stats/run-stats.component';

@NgModule({
  declarations: [
    AppComponent,
    RunFilterComponent,
    RunListComponent,
    RunStatsComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
