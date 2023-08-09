import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenManagerService } from './token-manager.service';


@Injectable({
    providedIn: 'root'
})
export class ApiRequestService {

    constructor(private http: HttpClient, private tokenManager: TokenManagerService) { }
    private API_URL: string = 'http://localhost:8080';

    get(uri: string): Observable<any> {
        let jsonHeader = {
            headers: new HttpHeaders({
                'Authorization': `Token ${this.tokenManager.getToken()}`
            })
        };
        if (uri.startsWith("/")) {
            return this.http.get(this.API_URL + uri, jsonHeader);
        } else {
            return this.http.get(this.API_URL + "/" + uri, jsonHeader);
        }
    }

    postWithDefaultHeaders(uri: string, body: any): Observable<any> {
        let headerWithToken = {
            headers: new HttpHeaders({
                'Authorization': `Token ${this.tokenManager.getToken()}`
            })
        };
        return this.post(uri, body, headerWithToken);
    }

    postWithJsonHeader(uri: string, body: any): Observable<any> {
        let jsonHeader = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': `Token ${this.tokenManager.getToken()}`
            })
        };
        return this.post(uri, body, jsonHeader);
    }
    private post(uri: string, body: any, customHeaders: { headers: HttpHeaders }) {
        if (uri.startsWith("/")) {
            return this.http.post(this.API_URL + uri, body, customHeaders);
        } else {
            return this.http.post(this.API_URL + "/" + uri, body, customHeaders);
        }
    }
}
