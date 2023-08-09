import { HttpErrorResponse, HttpEvent, HttpHandler, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, map } from 'rxjs';
import { TokenManagerService } from './token-manager.service';

@Injectable({
    providedIn: 'root'
})
export class HttpInterceptorService {

    constructor(private tokenManager: TokenManagerService, private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let modifiedRequest = req;
        let token = this.tokenManager.getToken();
        if (token.length > 0) {
            modifiedRequest = req.clone({
            });
        }
        return next.handle(modifiedRequest).pipe(
            map(res => {
                return res;
            }),
            catchError((error: HttpErrorResponse) => {
                if (error.status == 401) {
                    this.tokenManager.deleteToken();
                    this.router.navigate(['/']);
                }
                throw error;
            })
        );
    }
}
