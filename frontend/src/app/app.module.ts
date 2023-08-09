import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HttpInterceptorService } from './http-interceptor.service';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { RunFilterComponent } from './run-filter/run-filter.component';
import { RunListComponent } from './run-list/run-list.component';
import { RunStatsComponent } from './run-stats/run-stats.component';
import { UploadComponent } from './upload/upload.component';
import { SignupComponent } from './signup/signup.component';

@NgModule({
    declarations: [
        AppComponent,
        RunFilterComponent,
        RunListComponent,
        RunStatsComponent,
        UploadComponent,
        HomeComponent,
        LoginComponent,
        LogoutComponent,
        SignupComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        AppRoutingModule
    ],
    providers: [HttpClientModule, {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpInterceptorService,
        multi: true
    }],
    bootstrap: [AppComponent]
})
export class AppModule { }
