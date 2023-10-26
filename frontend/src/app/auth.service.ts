import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiRequestService } from './api-request.service';
import { IDefaultMessage } from './interface/IDefaultMessage';
import { ILoggedInformation } from './interface/ILoggedInformation';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(private apiRequest: ApiRequestService) { }

    signup(email: string, password: string): Observable<IDefaultMessage> {
        return this.apiRequest.postWithJsonHeader('user/signup', { "email": email, "password": password });
    }

    signin(email: string, password: string): Observable<ILoggedInformation> {
        return this.apiRequest.postWithJsonHeader('user/signin', { "email": email, "password": password });
    }
}
